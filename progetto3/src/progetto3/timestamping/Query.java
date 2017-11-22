package progetto3.timestamping;

import java.security.Timestamp;

public class Query {
	//hash del documento criptato con la chiave pubblica della TSA
	byte [] encryptedHash;
	String ID;
	java.sql.Timestamp timestamp;
	
	
	/**
	 * @return the timestamp
	 */
	public java.sql.Timestamp getTimestamp() {
		return timestamp;
	}
	/**
	 * @param currentTimestamp the timestamp to set
	 */
	public void setTimestamp(java.sql.Timestamp currentTimestamp) {
		this.timestamp = currentTimestamp;
	}
	/**
	 * @param hash
	 * @param iD
	 */
	public Query(byte[] hash, String iD) {
		this.encryptedHash = hash;
		ID = iD;
	}
	/**
	 * @return the hash
	 */
	public byte[] getHash() {
		return encryptedHash;
	}
	/**
	 * @param hash the hash to set
	 */
	public void setHash(byte[] hash) {
		this.encryptedHash = hash;
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
