package progetto3.timestamping;

import java.security.Timestamp;

public class Query {

	//hash del documento criptato con la chiave pubblica della TSA
	byte [] encryptedHash;
	String ID;
	String timestamp;
	//informazioni utili all'invio delle marche (client/server)
	Client client;
	String doc;
	
	/**
	 * @param encryptedHash
	 * @param iD
	 * @param client
	 * @param doc
	 */
	public Query(byte[] encryptedHash, String iD, Client client, String doc) {
		this.encryptedHash = encryptedHash;
		ID = iD;
		this.client = client;
		this.doc = doc;
	}
	
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * @param currentTimestamp the timestamp to set
	 */
	public void setTimestamp(String currentTimestamp) {
		this.timestamp = currentTimestamp;
	}
	
	
	
	
	
	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}
	/**
	 * @param client the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}
	/**
	 * @return the doc
	 */
	public String getDoc() {
		return doc;
	}
	/**
	 * @param doc the doc to set
	 */
	public void setDoc(String doc) {
		this.doc = doc;
	}
	/**
	 * @return the encryptedHash
	 */
	public byte[] getEncryptedHash() {
		return encryptedHash;
	}
	/**
	 * @param encryptedHash the encryptedHash to set
	 */
	public void setEncryptedHash(byte[] encryptedHash) {
		this.encryptedHash = encryptedHash;
	}
	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}
	
	

}
