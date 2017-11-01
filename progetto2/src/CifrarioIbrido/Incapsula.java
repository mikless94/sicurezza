package CifrarioIbrido;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.BadPaddingException;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Incapsula {
	
	private String pubKeyFile = "publicKeyFile.txt";
	private String fileToSend = "fileToSend.txt";
	
	private ArrayList<User> utenti = new ArrayList<User>();
	private SymmetricCipher symCipher = new SymmetricCipher();
	private AsymmetricCipher asymCipher = new AsymmetricCipher();
	
	public void addUser (String name, int dimKey, String padding) throws NoSuchAlgorithmException, IOException {
		asymCipher.setDimKey(dimKey);
		asymCipher.setPadding(padding);
		
		KeyPair pair = asymCipher.genKeyPair();
		User utente = new User(name, pair.getPrivate());
		utenti.add(utente);
		/*byte pubblica[] = pair.getPublic().getEncoded(); 
		String encodedPubKey = Base64.getEncoder().encodeToString(pubblica);
		System.out.println("Pubblica Base64 prima di salvare: " + encodedPubKey);*/
		FileManagement.savePublicKey(pubKeyFile, utente.getName(), pair.getPublic(), padding);
	}
	
	public void messageToSend (String sender, String recipient, String cipherType, String mode, String padding, boolean sign, String messagePath) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		symCipher.setCipherType(cipherType);
		symCipher.setMode(mode);
		
		SecretKey secKey = symCipher.genSecretKey(cipherType, mode);
		//y
		String cipheredKey = this.encodeSymmetricPrivateKey(recipient, cipherType, mode, secKey);
		//z
		String cipheredMessage = this.encodeMessage (messagePath, secKey);
		FileManagement.createFileToSend (fileToSend, cipheredKey, cipheredMessage, sender, recipient, cipherType, mode, padding, sign);
		
	}
	
	private String encodeMessage(String messagePath, SecretKey secKey) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		//il messaggio in messagePath è un messaggio qualsiasi (testo, imnagine ecc)
		//lo convertiamo in byte e poi lo cifriamo
		Path path = Paths.get(messagePath);
		byte[] data = Files.readAllBytes(path);
		return symCipher.symmetricEncoding(data, secKey);	
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
	
	
}
