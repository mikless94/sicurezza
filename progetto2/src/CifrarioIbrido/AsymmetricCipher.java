package CifrarioIbrido;

import java.security.KeyPair;

public class AsymmetricCipher {
	
	//
	
	private int dimKey;
	private String padding;
	/**
	 * @param dimKey
	 * @param padding
	 */
	public AsymmetricCipher(int dimKey, String padding) {
		this.dimKey = dimKey;
		this.padding = padding;
	}
	/**
	 * @return the dimKey
	 */
	public int getDimKey() {
		return dimKey;
	}
	/**
	 * @param dimKey the dimKey to set
	 */
	public void setDimKey(int dimKey) {
		this.dimKey = dimKey;
	}
	/**
	 * @return the padding
	 */
	public String getPadding() {
		return padding;
	}
	/**
	 * @param padding the padding to set
	 */
	public void setPadding(String padding) {
		this.padding = padding;
	}
	
	public KeyPair genKeyPair() {
		
		return null;
	}
	

}
