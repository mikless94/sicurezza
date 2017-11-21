package progetto3.timestamping;

public class Query {
	byte [] hash;
	String ID;
	
	
	
	/**
	 * @param hash
	 * @param iD
	 */
	public Query(byte[] hash, String iD) {
		this.hash = hash;
		ID = iD;
	}
	/**
	 * @return the hash
	 */
	public byte[] getHash() {
		return hash;
	}
	/**
	 * @param hash the hash to set
	 */
	public void setHash(byte[] hash) {
		this.hash = hash;
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
