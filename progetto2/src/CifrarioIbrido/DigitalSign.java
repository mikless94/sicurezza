package CifrarioIbrido;

import java.security.KeyPair;

public class DigitalSign {
	
	private int dimKey;
	private String type;
	

	/**
	 * @param dimKey
	 * @param type
	 */
	public DigitalSign(int dimKey, String type) {
		this.dimKey = dimKey;
		this.type = type;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	public KeyPair genKeyPair() {
		// TODO Auto-generated method stub
		return null;
	}

}
