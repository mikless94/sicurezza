package CifrarioIbrido;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Incapsula {
	
	private String pubKeyFile = "publicKeyFile.txt";
	private String fileToSend = "fileToSend.txt";
	private String digKeysFile = "digKeysFile.txt";
	
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

	public boolean addUser (String name, int dimKey, String padding) throws NoSuchAlgorithmException, IOException {
		asymCipher.setDimKey(dimKey);
		asymCipher.setPadding(padding);
		
		KeyPair pair = asymCipher.genKeyPair();
		User utente = new User(name, pair.getPrivate(), padding);
		boolean success = utenti.add(utente);
		/*byte pubblica[] = pair.getPublic().getEncoded(); 
		String encodedPubKey = Base64.getEncoder().encodeToString(pubblica);
		System.out.println("Pubblica Base64 prima di salvare: " + encodedPubKey);*/
		if (success)
			FileManagement.savePublicKey(pubKeyFile, utente.getName(), pair.getPublic(), padding);
		
		return success;
	}
	
	public boolean deleteUser (String name) throws IOException {
		boolean success = utenti.remove(new User(name));
		if (success){
			String tempFile = "myTempFile.txt";
		     BufferedReader in = new BufferedReader(new FileReader(pubKeyFile));
		     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		     String read = null;
		     while ((read = in.readLine()) != null) {
		    	 String[] splited = read.split("\\s+");
		         if (splited[0].compareTo(name)==0)
		        	 continue;
		         writer.write(read + System.getProperty("line.separator"));
		        }
		     writer.close(); 
		     in.close();
		        
		     BufferedWriter writer2 = new BufferedWriter(new FileWriter(pubKeyFile));
		     BufferedReader in2 = new BufferedReader(new FileReader(tempFile));
		     String read2 = null;
		     while ((read2 = in2.readLine()) != null) {
		    	 writer2.write(read2 + System.getProperty("line.separator"));
		    
		     }
		     writer2.close();
		     in2.close();
		}
	    return success;
	}

	public void messageToSend (String sender, String recipient, String cipherType, String mode, String padding, String messagePath) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		symCipher.setCipherType(cipherType);
		symCipher.setMode(mode);
		
		SecretKey secKey = symCipher.genSecretKey(cipherType, mode);
		System.out.println("Secret key generata dal destinatario: " + secKey.toString());
		//y
		String cipheredKey = this.encodeSymmetricPrivateKey(recipient, cipherType, mode, secKey);
		//z
		String cipheredMessage = this.encodeMessage (messagePath, secKey, mode);
		FileManagement.createFileToSend (fileToSend, cipheredKey, cipheredMessage, sender, recipient, cipherType, mode, padding);
		
	}
	
	public void messageToSend (String sender, String recipient, String cipherType, String mode, String padding, String messagePath, int dimSignKey, String signType) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, SignatureException {
		symCipher.setCipherType(cipherType);
		symCipher.setMode(mode);
		
		
		SecretKey secKey = symCipher.genSecretKey(cipherType, mode);
		System.out.println("Secret key generata dal destinatario: " + secKey.toString());
		//y
		String cipheredKey = this.encodeSymmetricPrivateKey(recipient, cipherType, mode, secKey);
		//z
		String cipheredMessage = this.encodeMessage (messagePath, secKey, mode);
		
		String sign = this.digitalSign(messagePath, sender, dimSignKey, signType);
		FileManagement.createFileToSend (fileToSend, cipheredKey, cipheredMessage, sender, recipient, cipherType, mode, padding, sign);
		
	}
	
	private String digitalSign(String messagePath, String sender, int dimSignKey, String signType) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
		// TODO Auto-generated method stub
		digSign.setDimKey(dimSignKey);
		digSign.setType(signType);
		
		
		KeyPair pair = digSign.genKeyPair();
		FileManagement.saveDigitalKeysFile(digKeysFile, sender , pair.getPublic(), signType );
		
		for (User utente : utenti) {
			if (utente.getName().compareTo(sender)==0) {
				utente.setSignKey(pair.getPrivate());
				break;
			}
		}
		
		return digSign.sign (messagePath, pair.getPrivate());
		
	}

	private String encodeMessage(String messagePath, SecretKey secKey, String mode) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		//il messaggio in messagePath è un messaggio qualsiasi (testo, immagine ecc)
		//lo convertiamo in byte e poi lo cifriamo
		Path path = Paths.get(messagePath);
		byte[] data = Files.readAllBytes(path);
		return symCipher.symmetricEncoding(data, secKey, mode);	
	}

	private String encodeSymmetricPrivateKey (String destination, String cipherType, String mode, SecretKey secKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		String key="";
		
		BufferedReader in = new BufferedReader(new FileReader(pubKeyFile));
	    String read = null;
	    while ((read = in.readLine()) != null) {
	        String[] splited = read.split("\\s+");
	        if (splited[0].compareTo(destination)==0) {
	        	key = splited[1];
	        	asymCipher.setPadding(splited[2]);
	        	}
	    }
	    in.close();
	    //aggiungere eccezione per destinatario non trovato
		byte [] keyBytes = Base64.getDecoder().decode(key);
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		return asymCipher.asymmetricEncoding (secKey, pubKey);
	}
	
	public void decodeMessage (String destinationPath) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SignatureException, InvalidKeySpecException {
		
		BufferedReader in = new BufferedReader(new FileReader(fileToSend));

	    ArrayList <String> fields = new ArrayList <String> ();
	    for (int i=0; i<8 ; i++) 
	    	fields.add(in.readLine());
	    if (fields.get(5).compareTo("1")==0)
	    	fields.add(in.readLine());
	    in.close();
 	    
	    SecretKey secKey = this.decodeSymmetricKey (fields.get(6), fields.get(1), fields.get(2));
	    System.out.println("Secret key decifrata dal file: " + secKey.toString());
	    obtainMessage (fields.get(2), fields.get(3), fields.get(4), fields.get(7), secKey, destinationPath);
	    
	    if (fields.get(5).compareTo("1")==0){
	    	if (this.verify(destinationPath, fields.get(8), fields.get(0))){
	    		JOptionPane.showMessageDialog(null,"Message verified! " ,"Digital Sign",JOptionPane.INFORMATION_MESSAGE, new ImageIcon(GUI.class.getResource("/progetto2/resources/Ok-icon.png")));
	    		System.out.println("verifica corretta");
	    	}
	    }
	
	}

	private void obtainMessage(String cipherType, String mode, String padding, String message, SecretKey secKey, String destinationPath) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		// TODO Auto-generated method stub
		symCipher.setCipherType(cipherType);
		symCipher.setMode(mode);
		symCipher.setPadding(padding);
		symCipher.computeDimKey();
		byte [] decodedMessage = symCipher.symmetricDecoding (cipherType, mode, padding, secKey, message);
		Files.write(Paths.get(destinationPath), decodedMessage);

	}

	private SecretKey decodeSymmetricKey (String cipherSymmetricKey, String recipient, String cipherType) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		String padding = "";
		PrivateKey pvtKey = null;
		for (User utente : utenti) {
			if (utente.getName().compareTo(recipient)==0) {
				padding += utente.getPadding();
				pvtKey = utente.getAsymmetricKey();
				break;
			}
		}
		byte [] decodedKey = asymCipher.asymmetricDecoding(cipherSymmetricKey, padding, pvtKey);
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
	        	}
	    }
	    in.close();
	    //aggiungere eccezione per mittente non trovato
	    
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
	
	
}
