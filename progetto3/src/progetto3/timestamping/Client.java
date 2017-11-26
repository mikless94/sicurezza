package progetto3.timestamping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.swing.JOptionPane;

public class Client {
	private String ID;
	//map con documento e marca associata --> necessario per verifica
	private HashMap <String, String> map;
	//keyring utente
	private KeyRing keyR;
	private String filename = "KeyRing"+ID+".txt";
	private String hashAlg = "SHA-256";
	private TSA tsa;
	private byte[] hashPassword;
	boolean validated = false;

	
	/**
	 * @param iD
	 * @param map
	 */
	public Client(String iD, String password) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(hashAlg);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		this.hashPassword = digest.digest(password.getBytes());
		ID = iD;
		this.map = new HashMap <String, String> ();
		
		//ottengo l'istanza univoca di TSA
		this.tsa = TSA.getInstance();
		
		//creo il KeyRing per l'utente
		this.keyR = new KeyRing(filename);
	}

	public void sendQuery (String fileName) {
		//calcolo hash del documento
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(hashAlg);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		Path path = Paths.get(fileName);
		byte[] data = null;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte [] hash = digest.digest(data);
		//hash del documento cifrato con la chiave pubblica della TSA
		byte [] encryptedHash = encryptHash (hash);
		Query query = new Query (encryptedHash, ID);
		tsa.addQuery(query);
	}
	
	private byte[] encryptHash(byte[] hash) {
		//estraggo dal keyring la chiave pubblica e le relative informazioni necessarie alla cifratura
		PublicKey pubKey = keyR.getKeyRingTSA().getPublicKey();
		String alg = keyR.getKeyRingTSA().getAlgorithm();
		String mode = keyR.getKeyRingTSA().getMode();
		String padding = keyR.getKeyRingTSA().getPadding();
		
		Cipher c = Cipher.getInstance(alg+mode+padding);
		c.init(Cipher.ENCRYPT_MODE, pubKey);
		//cifro l'hash del documento  
		return c.doFinal(hash);
	}

	public void verifyTimeStamp () {
		
	}
	
	public void markDocument() {
		
	}
	
	public void userValidation(String password){
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(hashAlg);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (Arrays.equals(hashPassword,digest.digest(password.getBytes())))
			validated = true;
		else
			JOptionPane.showMessageDialog(null,"Incorrect Password","Warning!",JOptionPane.WARNING_MESSAGE);
		
	}
	
	public void addToKeyring (String role, String type, String param3, String param4, ArrayList<byte[]> array) {
		
		if(validated)
			keyR.addToKeyring(role, type, param3, param4, array);
	}
	
	public ArrayList<byte[]> getValueFromKeyRing(String role, String type, String param3, String param4){
		
		ArrayList<byte[]> array= null;
		if(validated){
			array =  keyR.getValueFromKeyRing(role, type, param3, param4);
		}
		return array;
	}
	
	public void saveKeyRing(String password){
		
		if(validated)
			keyR.encodeData(password);		
	}
	
	public void restoreKeyRing(String password){
		
		if(validated)
			keyR.decodeData(password);
	}
}
