package progetto3.timestamping;

import java.io.Serializable;

public class Info implements Serializable{
	byte [] hash;
	//0 hash corrente, -1 moltiplica a sinistra, 1 moltiplica a destra
	int position;
	/**
	 * @param hash
	 * @param position
	 */
	public Info(byte[] hash, int position) {
		this.hash = hash;
		this.position = position;
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
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	
}
