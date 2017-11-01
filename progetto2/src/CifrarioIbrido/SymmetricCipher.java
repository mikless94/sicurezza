package CifrarioIbrido;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
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

	public void computeDimKey () {
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
	}

	/**
	 * @param cipherType
	 * @param mode
	 */
	public SymmetricCipher() {
	
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
	
	public String symmetricEncoding (byte[] data, SecretKey secKey ) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(cipherType+"/"+mode+"/"+padding);
		cipher.init(Cipher.ENCRYPT_MODE, secKey);
		byte [] cipherMessage = cipher.doFinal(data);
		return Base64.getEncoder().encodeToString(cipherMessage);
	}


	public byte[] symmetricDecoding(String cipherType, String mode, String padding, SecretKey secKey,
			String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		System.out.println("tipo di cifrario: "+cipherType);
		System.out.println("mode: "+mode);
		System.out.println("padding: "+padding);
		Cipher cipher = Cipher.getInstance(cipherType+"/"+mode+"/"+padding);
		cipher.init(Cipher.DECRYPT_MODE, secKey);
		System.out.println("message: "+message);
		byte [] decodedMessageBytes = cipher.doFinal(Base64.getDecoder().decode(message));
		return decodedMessageBytes;
		//return Base64.getEncoder().encodeToString(decodedMessageBytes);
	}

}
