package CifrarioIbrido;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class Incapsula {

	private String fileToSend = "fileToSend.txt";
	private String pubKeyFile = "publicKeyFile.txt";
	private String digKeysFile = "digKeysFile.txt";
	private String pvtKeysFile = "pvtKeysFile.txt";
	private String pvtDigitalKeysFile = "pvtDigitalKeysFile.txt";
	
	private HashSet<User> utenti = new HashSet<User>();
	private SymmetricCipher symCipher = new SymmetricCipher();
	private AsymmetricCipher asymCipher = new AsymmetricCipher();
	private DigitalSign digSign = new DigitalSign();
	
	/**
	 * @return the utenti
	 */
	public HashSet <User> getUtenti() {
		return utenti;
	}

	public boolean addUser (String name, int dimKey, String padding, String password) throws NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		asymCipher.setDimKey(dimKey);
		asymCipher.setPadding(padding);
		
		KeyPair pair = asymCipher.genKeyPair();
		User utente = new User(name, password);
		boolean success = utenti.add(utente);
		
		if (success){
			FileManagement.savePublicKey(pubKeyFile, utente.getName(), pair.getPublic(), padding);
			String pvtKeyString = keyRingEncoding(utente, pair.getPrivate());
			FileManagement.savePrivateKey(pvtKeysFile, utente.getName(), pvtKeyString);
		}
		
		return success;
	}
	
	public boolean deleteUser (String name) throws IOException {
		boolean success = utenti.remove(new User(name));
		
		if (success) {
			FileManagement.removeUserFromFile(name, pubKeyFile);
			FileManagement.removeUserFromFile(name, pvtKeysFile);
			FileManagement.removeUserFromFile(name, digKeysFile);
			FileManagement.removeUserFromFile(name, pvtDigitalKeysFile );
		}
	    return success;

}
	
	
	//encoding
	public void messageToSend (String sender, String recipient, String cipherType, String mode, String padding, String messagePath) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		symCipher.setCipherType(cipherType);
		symCipher.setMode(mode);
		
		SecretKey secKey = symCipher.genSecretKey();
		IvParameterSpec iv = symCipher.genIV ();
		
		String key="";
		
		BufferedReader in = new BufferedReader(new FileReader(pubKeyFile));
	    String read = null;
	    while ((read = in.readLine()) != null) {
	        String[] splited = read.split("\\s+");
	        if (splited[0].compareTo(recipient)==0) {
	        	key = splited[1];
	        	asymCipher.setPadding(splited[2]);
	        	break;
	        	}
	    }
	    in.close();

		byte [] keyBytes = Base64.getDecoder().decode(key);
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		
		String cipheredInfo = this.encodeInfo(pubKey, cipherType, mode, padding, iv);
		//y
		String cipheredKey = this.encodeSymmetricPrivateKey(pubKey, recipient, cipherType, mode, padding, secKey, iv);
		//z
		String cipheredMessage = this.encodeMessage (messagePath, secKey, mode, iv);
		
		FileManagement.createFileToSend (fileToSend, cipheredInfo, cipheredKey, cipheredMessage, sender, recipient, cipherType, mode, padding);
		
	}
	
	public void messageToSend (String sender, String recipient, String cipherType, String mode, String padding, String messagePath, int dimSignKey, String signType) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, SignatureException {
		
		
		symCipher.setCipherType(cipherType);
		symCipher.setMode(mode);
		
		SecretKey secKey = symCipher.genSecretKey();
		IvParameterSpec iv = symCipher.genIV ();
		
		String key="";
		
		BufferedReader in = new BufferedReader(new FileReader(pubKeyFile));
	    String read = null;
	    while ((read = in.readLine()) != null) {
	        String[] splited = read.split("\\s+");
	        if (splited[0].compareTo(recipient)==0) {
	        	key = splited[1];
	        	asymCipher.setPadding(splited[2]);
	        	break;
	        	}
	    }
	    in.close();
	    
		byte [] keyBytes = Base64.getDecoder().decode(key);
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		
		String cipheredInfo = this.encodeInfo(pubKey, cipherType, mode, padding, iv);
		//y
		String cipheredKey = this.encodeSymmetricPrivateKey(pubKey, recipient, cipherType, mode, padding, secKey, iv);
		//z
		String cipheredMessage = this.encodeMessage (messagePath, secKey, mode, iv);
		
		String sign = this.digitalSign(messagePath, sender, dimSignKey, signType);
		
		FileManagement.createFileToSend (fileToSend, cipheredInfo, cipheredKey, cipheredMessage, sender, recipient, cipherType, mode, padding, sign);
	}
	
	private String encodeInfo(PublicKey pubKey, String cipherType, String mode, String padding, IvParameterSpec iv) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String symmetricCipherInfo = cipherType+" "+mode+" "+padding;
		if (iv !=null) {
			symmetricCipherInfo+=" "+Base64.getEncoder().encodeToString(iv.getIV());
		}
		return asymCipher.asymmetricEncoding (symmetricCipherInfo, pubKey);
	}

	private String digitalSign(String messagePath, String sender, int dimSignKey, String signType) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		digSign.setDimKey(dimSignKey);
		digSign.setType(signType);
		
		KeyPair pair = digSign.genKeyPair();
		String cipheredDigitalKey = "";
		
		for(User utente: utenti) {
			if(utente.getName().compareTo(sender)==0) {
				cipheredDigitalKey += this.DigitalkeyRingEncoding(utente, pair.getPrivate());
				break;
			}
		}
		
		FileManagement.saveDigitalPrivateKey(pvtDigitalKeysFile, sender, cipheredDigitalKey);
		
		FileManagement.saveDigitalKeysFile(digKeysFile, sender , pair.getPublic(), signType );

		
		return digSign.sign (messagePath, pair.getPrivate());
		
	}


	private String encodeMessage(String messagePath, SecretKey secKey, String mode, IvParameterSpec iv) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		Path path = Paths.get(messagePath);
		byte[] data = Files.readAllBytes(path);
		return symCipher.symmetricEncoding(data, secKey, mode, iv);	
	}

	private String encodeSymmetricPrivateKey (PublicKey pubKey, String destination, String cipherType, String mode, String padding, SecretKey secKey, IvParameterSpec iv) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		return asymCipher.asymmetricEncoding (secKey, pubKey);
	}
	

	
	
	// decoding
	public void decodeMessage (String destinationPath) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SignatureException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		
		BufferedReader in = new BufferedReader(new FileReader(fileToSend));

	    ArrayList <String> fields = new ArrayList <String> ();
	    String read = null;
	    while ((read = in.readLine())!=null)
	    	fields.add(read);
	    
	    in.close();
 	    
	    User user = new User (fields.get(1));
	    
	    String padding = "";
		for (User utente : utenti) {
			if (utente.getName().compareTo(fields.get(1))==0) {
				padding += utente.getPadding();
				user.setPassword(utente.getPassword());
				user.setSaltRSA(utente.getSaltRSA());
				break;
			}
		}

		PrivateKey pvtKey = this.keyRingDecoding(user);
		
	    String [] info = obtainInfo (fields.get(2), padding, pvtKey);
	    SecretKey secKey = this.decodeSymmetricKey (fields.get(3), fields.get(1), info [0], pvtKey);
	    if (info[1].compareTo("CBC")==0 || info[1].compareTo("CFB")==0) 
	    	obtainMessage (info[0], info[1], info[2], fields.get(4), secKey, destinationPath, info[3]);
	    else
	    	obtainMessage (info[0], info[1], info[2], fields.get(4), secKey, destinationPath);
	    

	    if (fields.size() >= 6)
	    	if (this.verify(destinationPath, fields.get(5), fields.get(0))) {
	    		//System.out.println("Message verified");
	    		JOptionPane.showMessageDialog(null,"Message verified! " ,"Digital Sign",JOptionPane.INFORMATION_MESSAGE, new ImageIcon(GUI.class.getResource("/progetto2/resources/Ok-icon.png")));
	    	}
	    	else {
	    		//System.out.println("Message NOT verified");
	    		JOptionPane.showMessageDialog(null,"Message NOT verified! " ,"Digital Sign",JOptionPane.ERROR_MESSAGE);
	    	}
	    }
	

	private void obtainMessage(String cipherType, String mode, String padding, String message, SecretKey secKey, String destinationPath, String iv) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException {
		// TODO Auto-generated method stub
		symCipher.setCipherType(cipherType);
		symCipher.setMode(mode);
		symCipher.setPadding(padding);
		symCipher.computeDimKey();
	
		byte [] decodedMessage = symCipher.symmetricDecoding (secKey, message, iv);
		Files.write(Paths.get(destinationPath), decodedMessage);

	}
	
	private void obtainMessage(String cipherType, String mode, String padding, String message, SecretKey secKey, String destinationPath) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		// TODO Auto-generated method stub
		symCipher.setCipherType(cipherType);
		symCipher.setMode(mode);
		symCipher.setPadding(padding);
		symCipher.computeDimKey();
		byte [] decodedMessage = symCipher.symmetricDecoding (secKey, message);
		Files.write(Paths.get(destinationPath), decodedMessage);
	}

	private String[] obtainInfo(String info, String padding, PrivateKey pvtKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		byte [] decodedInfoBytes = asymCipher.asymmetricDecoding(info, pvtKey);
		String decodedInfoString = new String(decodedInfoBytes);;
		return decodedInfoString.split("\\s+");

	}


	private SecretKey decodeSymmetricKey (String cipherSymmetricKey, String recipient, String cipherType, PrivateKey pvtKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		
		byte [] decodedKey = asymCipher.asymmetricDecoding(cipherSymmetricKey,pvtKey);
		return new SecretKeySpec(decodedKey, 0, decodedKey.length, cipherType);
		
	}

	private boolean verify(String decodifiedMessagePath, String sign, String sender) throws SignatureException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
		// TODO Auto-generated method stub
		String key="";
		
		BufferedReader in = new BufferedReader(new FileReader(digKeysFile));
	    String read = null;
	    while ((read = in.readLine()) != null) {
	        String[] splited = read.split("\\s+");
	        if (splited[0].compareTo(sender)==0) {
	        	key = splited[1];
	        	digSign.setType(splited[2]);
	        	break;
	        	}
	    }
	    in.close();
	    
		byte [] keyBytes = Base64.getDecoder().decode(key);
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		Signature dsa = Signature.getInstance(digSign.getType());
		dsa.initVerify(pubKey);
		Path path = Paths.get(decodifiedMessagePath);
		dsa.update(Files.readAllBytes(path));
		return dsa.verify(Base64.getDecoder().decode(sign));
	}
	
	private String keyRingEncoding(User user, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		
		char[] pass = user.getPassword().toCharArray();
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		
		user.setSaltRSA(salt);
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		KeySpec keySpec = new PBEKeySpec(pass, salt, 65536, 128);
		SecretKey tmp = factory.generateSecret(keySpec);
		SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
		
		Cipher ciph = Cipher.getInstance("AES");
		ciph.init(Cipher.ENCRYPT_MODE, secretKey);
		
		
		byte[] symKey = privateKey.getEncoded();
		byte[] cipheredKey = ciph.doFinal(symKey);
		String codifiedKey = Base64.getEncoder().encodeToString(cipheredKey);
		return codifiedKey;
	}
	
	private PrivateKey keyRingDecoding(User user) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException{
		
		char[] pass = user.getPassword().toCharArray();
		
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		KeySpec spec = new PBEKeySpec(pass, user.getSaltRSA(), 65536, 128);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
		
		byte[] ciphKey = Base64.getDecoder().decode(FileManagement.readPrivateKey(pvtKeysFile, user));
		Cipher ciph = Cipher.getInstance("AES");
		ciph.init(Cipher.DECRYPT_MODE, secretKey);
		byte [] decryptedText = ciph.doFinal(ciphKey);

		PKCS8EncodedKeySpec pubKeySpec = new PKCS8EncodedKeySpec(decryptedText);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey pvtKey = keyFactory.generatePrivate(pubKeySpec);
		
		return pvtKey;
	}
	
private String DigitalkeyRingEncoding(User user, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
		
		char[] pass = user.getPassword().toCharArray();
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		
		user.setSaltDSA(salt);
		
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		KeySpec keySpec = new PBEKeySpec(pass, salt, 65536, 128);
		SecretKey tmp = factory.generateSecret(keySpec);
		SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
		
		Cipher ciph = Cipher.getInstance("AES");
		ciph.init(Cipher.ENCRYPT_MODE, secretKey);
		
		
		byte[] symKey = privateKey.getEncoded();
		byte[] cipheredKey = ciph.doFinal(symKey);
		
		String codifiedKey = Base64.getEncoder().encodeToString(cipheredKey);
		
		return codifiedKey;
	}
	
}
