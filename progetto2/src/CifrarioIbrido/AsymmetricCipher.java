package CifrarioIbrido;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
	
	public KeyPair genKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(dimKey, new SecureRandom());
		return keyPairGenerator.generateKeyPair();
	}
	

}
