package progetto3.timestamping;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.Cipher;

public class Client {
	private String ID;
	//map con documento e marca associata --> necessario per verifica
	private HashMap <String, String> map;
	//keyring utente
	private KeyRing keyR;
	private String hashAlg = "SHA-256";
	private TSA tsa;

	
	/**
	 * @param iD
	 * @param map
	 */
	public Client(String iD) {
		ID = iD;
		this.map = new HashMap <String, String> ();
		
		//ottengo l'istanza univoca di TSA
		this.tsa = TSA.getInstance();
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
		byte[] data = Files.readAllBytes(path);
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

	public verifyTimeStamp () {
		
	}
	
	public markDocument() {
		
	}
	
	public KeyRing.addToKeyring () {
		
	}*/

}
