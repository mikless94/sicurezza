package progetto3.timestamping;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;

public class Client implements Serializable{
	private String ID;
	//map con documento e marca associata --> necessario per verifica
	private HashMap <String, String> map;
	//keyring utente
	private KeyRing keyR;
	private String filename;
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
		this.ID = iD;
		this.map = new HashMap <String, String> ();
		
		//ottengo l'istanza univoca di TSA
		this.tsa = TSA.getInstance();
		
		//creo il KeyRing per l'utente
		this.filename = "KeyRing"+ID+".txt";
		this.keyR = new KeyRing(filename);
	}
	
	
	
	/**
	 * @return the tsa
	 */
	public TSA getTsa() {
		return tsa;
	}



	/**
	 * @return the map
	 */
	public HashMap<String, String> getMap() {
		return map;
	}



	public void sendQuery (String fileName) throws IOException {
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
		Query query = new Query (encryptedHash, ID, this, fileName);
		tsa.addQuery(query);
	}
	
	private byte[] encryptHash(byte[] hash) {
		//estraggo dal keyring la chiave pubblica e le relative informazioni necessarie alla cifratura
		PublicKey pubKey = tsa.getKpRSA().getPublic();
		Cipher c = null;
		try {
			c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			c.init(Cipher.ENCRYPT_MODE, pubKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//cifro l'hash del documento  
		try {
			return c.doFinal(hash);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	public boolean verifySign (String doc) {
		//deserializziamo in modo da ottenere oggetto ReplyToSend
		ReplyToSend replyReceived = deserializeReply (map.get(doc));
		//replyReceived.getReply().setSerialNumber(898934);
		Signature dsa = null;
		try {
			dsa = Signature.getInstance(replyReceived.getSignType());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ottengo chiave pubblica di firma TSA
		PublicKey pubKey = tsa.getKpSign().getPublic();
		try {
			dsa.initVerify(pubKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(replyReceived.getReply());
            }
		dsa.update(b.toByteArray());
		return dsa.verify(replyReceived.getSign());
		} catch (IOException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	

	private ReplyToSend deserializeReply(String marca) {
		ReplyToSend replyReceived = null;
		try {
			FileInputStream fileIn = new FileInputStream(marca);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			replyReceived = (ReplyToSend) in.readObject();
			in.close();
			fileIn.close();
			} catch (IOException i) {
				i.printStackTrace();
				} 
			catch (ClassNotFoundException c) {
			c.printStackTrace();
			}
		return replyReceived;
	}

	public boolean verifyOffline(String doc) {
		//si presuppone che la firma sia verificata per verificare il root value all'interno della marca
		if (verifySign(doc)) {
			ReplyToSend replyReceived = deserializeReply (map.get(doc));
			//replyReceived.getReply().setRootHash(new byte[13]);
			Reply reply = replyReceived.getReply();
			byte [] computedRootHash = computeRootHash (reply.getLinkingInfo(), reply.getTSAHash());
			return Arrays.equals(computedRootHash, reply.getRootHash());
		}
		return false;
	}
	 
	private byte[] computeRootHash(ArrayList<Info> linkingInfo, String TSAHash) {
		byte [] result = linkingInfo.get(0).getHash();
		
		for (int i=1; i<linkingInfo.size(); i++) {
			byte[] concatenated = new byte[result.length + linkingInfo.get(i).getHash().length];
			//moltiplica a sinistra
			if (linkingInfo.get(i).getPosition() == -1) {
				System.arraycopy(linkingInfo.get(i).getHash(), 0, concatenated, 0, linkingInfo.get(i).getHash().length);
				System.arraycopy(result, 0, concatenated, linkingInfo.get(i).getHash().length, result.length );
			}
			//moltiplica a destra
			else {
				System.arraycopy(result, 0, concatenated, 0, result.length);
				System.arraycopy(linkingInfo.get(i).getHash(), 0, concatenated, result.length, linkingInfo.get(i).getHash().length);
			}
			
			MessageDigest digest = null;
			try {
				digest = MessageDigest.getInstance(TSAHash);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			result = digest.digest(concatenated);
		}
		return result;
	}

	public boolean verifyOnline(String doc) {
		//si presuppone verifica firma e verifica offline
		if (verifyOffline(doc)) {
			ReplyToSend replyReceived = deserializeReply (map.get(doc));
			Reply reply = replyReceived.getReply();
			byte [] computedRootHash = computeRootHash (reply.getLinkingInfo(), reply.getTSAHash());
			//modifico valore di super hash i-1
			//tsa.getSuperHash().set(reply.getTimeframeNumber(), new byte[3]);
			byte [] SHVprec = null;
			if (reply.getTimeframeNumber()==0)
				SHVprec = tsa.getShv0();
			else
				SHVprec = tsa.getSuperHash().get(reply.getTimeframeNumber()-1);
			byte [] SHVcurr = tsa.getSuperHash().get(reply.getTimeframeNumber());
			return Arrays.equals(SHVcurr, hashConcatenate(SHVprec, computedRootHash, reply.getTSAHash()));
			}
		return false; 
	}
	
	public boolean verifyChain (String doc, int range) {
		if (verifyOnline (doc)) {
			ReplyToSend replyReceived = deserializeReply (map.get(doc));
			Reply reply = replyReceived.getReply();
			ArrayList <byte[]> superHashValues = tsa.getSuperHash();
			ArrayList <byte[]> rootHashValues = tsa.getRootHash();
			//modifico un elemento nella catena
			//superHashValues.set(reply.getTimeframeNumber(), new byte[3]);
			byte [] SHVStart = null;
			
			//range <= 0 --> verifico intera catena
			if (range <= 0) {
				SHVStart = tsa.getShv0();
				for (int i=0; i<superHashValues.size(); i++) {
					if (!Arrays.equals(superHashValues.get(i), hashConcatenate(SHVStart, rootHashValues.get(i), reply.getTSAHash())))
						return false;
					SHVStart = superHashValues.get(i);
				}
				return true;
			}
			
			//verifico porzione della catena identificata dal parametro range
			else {
				int start = 0;
				int end = superHashValues.size()-1;
				
				if (reply.getTimeframeNumber()-range>0)
					start = reply.getTimeframeNumber()-range;
				if (reply.getTimeframeNumber()+range<superHashValues.size()-1)
					end = reply.getTimeframeNumber()+range;
				
				for (int i=start; i<end; i++) {
					if (i==0) 
						SHVStart = tsa.getShv0();
					else
						SHVStart = superHashValues.get(i-1);
					
					if (!Arrays.equals(superHashValues.get(i), hashConcatenate(SHVStart, rootHashValues.get(i), reply.getTSAHash())))
						return false;
					SHVStart = superHashValues.get(i);
				}
				return true;
			}
		} 
		return false;
	}
	
	
	private byte[] hashConcatenate(byte [] byte1 , byte [] byte2, String TSAHash) {
		byte[] concatenated = new byte[byte1.length + byte2.length];
		System.arraycopy(byte1, 0, concatenated, 0, byte1.length);
		System.arraycopy(byte2, 0, concatenated, byte1.length, byte2.length );
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(TSAHash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return digest.digest(concatenated);
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
	}
	
	public void addToKeyring (String role, String type, String param3, String param4, ArrayList<byte[]> array) {
		
		if(validated)
			keyR.addToKeyring(role, type, param3, param4, array);
		else{
			System.out.println("User not validated!");
		}
	}
	
	public ArrayList<byte[]> getValueFromKeyRing(String role, String type, String param3, String param4){
		
		ArrayList<byte[]> array= null;
		if(validated){
			array =  keyR.getValueFromKeyRing(role, type, param3, param4);
			if(array == null)
				System.out.println("The specified Key does not exist!");
		}
		else{
			System.out.println("User not validated!");
		}
		return array;
	}
	
	/*public ArrayList<String> getValueFromKeyRing(String role, String type, String param3, String param4){
		
		ArrayList<byte[]> array= null;
		ArrayList<String> value = null;
		if(validated){
			array =  keyR.getValueFromKeyRing(role, type, param3, param4);
			if(array == null)
				System.out.println("The specified Key does not exist!");
			else{
				//Si ritorna una stringa nel caso si è richiesto una password.
				if(param4.equals("null")){
					String pass = new String(array.get(0));
					value.add(pass);
				}
				else {
					if(param3.equals("AES") || param3.equals("DES") || param3.equals("DESede")){
						
					}
					
				}
			}
		}
		else{
			System.out.println("User not validated!");
		}
		return value;
	}
	
	public ArrayList<String> getValueFromKeyRing(String role, String type, String param3, String param4){
		
		ArrayList<byte[]> array= null;
		ArrayList<String> value = null;
		if(validated){
			array =  keyR.getValueFromKeyRing(role, type, param3, param4);
			if(array == null)
				System.out.println("The specified Key does not exist!");
			else{
				//Si ritorna una stringa nel caso si è richiesto una password.
				if(param4.equals("null")){
					String pass = new String(array.get(0));
					value.add(pass);
				}
				else {
					if(param3.equals("AES") || param3.equals("DES") || param3.equals("DESede")){
						
					}
					
				}
			}
		}
		else{
			System.out.println("User not validated!");
		}
		return value;
	}*/
	
	public void saveKeyRing(String password){
		
		if(validated){
			MessageDigest digest = null;
			try {
				digest = MessageDigest.getInstance(hashAlg);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if (Arrays.equals(hashPassword,digest.digest(password.getBytes())))
				keyR.encodeData(password);
			else
				System.out.println("Incorrect password!");
		}
		else{
			System.out.println("User not validated!");
		}
	}
	
	public void restoreKeyRing(String password){
		
		if(validated){
			MessageDigest digest = null;
			try {
				digest = MessageDigest.getInstance(hashAlg);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if (Arrays.equals(hashPassword,digest.digest(password.getBytes())))
				keyR.decodeData(password);
			else
				System.out.println("Incorrect password!");
		}
		else{
			System.out.println("User not validated!");
		}
	}

	public KeyRing getKeyR() {
		return keyR;
	}



	public void setKeyR(KeyRing keyR) {
		this.keyR = keyR;
	}



	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}



	public String getID() {
		return ID;
	}



	public void setID(String iD) {
		ID = iD;
	}

}
