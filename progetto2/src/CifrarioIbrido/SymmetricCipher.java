package CifrarioIbrido;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SymmetricCipher {
	private String cipherType;
	private String mode;
	private String padding = "PKCS5Padding";
	private int dimKey;
	
	
	
	
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
	 * @param cipherType
	 * @param mode
	 */
	public SymmetricCipher(String cipherType, String mode) {
		this.cipherType = cipherType;
		this.mode = mode;
	}



	/**
	 * @return the cipherType
	 */
	public String getCipherType() {
		return cipherType;
	}



	/**
	 * @param cipherType the cipherType to set
	 */
	public void setCipherType(String cipherType) {
		this.cipherType = cipherType;
	}



	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}



	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
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



	public SecretKey genSecretKey(String cipherType,String  mode) throws NoSuchAlgorithmException {
		
		KeyGenerator keyGenerator = null;
		keyGenerator = KeyGenerator.getInstance(cipherType);
		
		switch (cipherType) {
		case "AES" :
			this.setDimKey(128);
			break;
		case "DES" :
			this.setDimKey(56);
			break;
		case "DESede":
			this.setDimKey(168);
			break;
		}
		
		keyGenerator.init(this.getDimKey(), new SecureRandom()); 
		return keyGenerator.generateKey();
	}

}
