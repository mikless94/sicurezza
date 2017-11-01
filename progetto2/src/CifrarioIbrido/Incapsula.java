package CifrarioIbrido;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Incapsula {
	
	private String pubKeyFile = "publicKeyFile.txt";
	private ArrayList<User> utenti = new ArrayList<User>();
	
	public void addUser (String name, int dimKey, String padding) throws NoSuchAlgorithmException, IOException {
		AsymmetricCipher asymCipher = new AsymmetricCipher(dimKey, padding);
		KeyPair pair = asymCipher.genKeyPair();
		User utente = new User(name, pair.getPrivate());
		utenti.add(utente);
		/*byte pubblica[] = pair.getPublic().getEncoded(); 
		String encodedPubKey = Base64.getEncoder().encodeToString(pubblica);
		System.out.println("Pubblica Base64 prima di salvare: " + encodedPubKey);*/
		FileManagement.savePublicKey(pubKeyFile, utente.getName(), pair.getPublic(), padding);
	}
	
	public byte[] encodeSymmetricPrivateKey (String destination, String cipherType, String mode) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SymmetricCipher symCipher = new SymmetricCipher(cipherType, mode);
		SecretKey secKey;
		String key="";
		String padding="";
		
		BufferedReader in = new BufferedReader(new FileReader(pubKeyFile));
	    String read = null;
	    while ((read = in.readLine()) != null) {
	        String[] splited = read.split("\\s+");
	        if (splited[0].compareTo(destination)==0) {
	        	key = splited[1];
	        	padding = splited[2];
	        	}
	    }
	    in.close();
	    
	    //aggiungere eccezione per destinatario non trovato
	        	
		byte [] keyBytes = Base64.getDecoder().decode(key);
		
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
		
		Cipher c = Cipher.getInstance("RSA/ECB/"+padding);
		c.init(Cipher.ENCRYPT_MODE, pubKey);
		
		//generiamo chiave privata del cifrario simmetrico
		secKey = symCipher.genSecretKey(cipherType, mode);
		System.out.println(Base64.getEncoder().encodeToString(secKey.getEncoded()));
		//cifro chiave privata appena generata con la chiave pubblica del cifrario RSA relativa al destinatario desiderato
		byte [] cipheredKeyBytes = c.doFinal(secKey.getEncoded());
		System.out.println(Base64.getEncoder().encodeToString(cipheredKeyBytes));
		
		/*c.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decodificato  = c.doFinal(ciphertext);
		System.out.println("Testo decifrato: " + new String(decodificato,"UTF8"));*/
		return cipheredKeyBytes;
		
	}
	
	
	

}
