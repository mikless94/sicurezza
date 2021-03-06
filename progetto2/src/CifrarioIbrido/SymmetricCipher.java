package CifrarioIbrido;

import java.security.InvalidAlgorithmParameterException;
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
import javax.crypto.spec.IvParameterSpec;

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



	public SecretKey genSecretKey() throws NoSuchAlgorithmException {
		
		KeyGenerator keyGenerator = null;
		keyGenerator = KeyGenerator.getInstance(cipherType);
		
		this.computeDimKey();
		
		keyGenerator.init(this.getDimKey(), new SecureRandom()); 
		return keyGenerator.generateKey();
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
	
	public String symmetricEncoding (byte[] data, SecretKey secKey, String mode, IvParameterSpec iv) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(cipherType+"/"+mode+"/"+padding);
	
		if (iv!=null) {

			
			try {
				cipher.init(Cipher.ENCRYPT_MODE, secKey, iv);
			} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			cipher.init(Cipher.ENCRYPT_MODE, secKey);
		}
		byte [] cipherMessage = cipher.doFinal(data);
		return Base64.getEncoder().encodeToString(cipherMessage);
	}


	public byte[] symmetricDecoding(SecretKey secKey,
			String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		
		Cipher cipher = Cipher.getInstance(cipherType+"/"+mode+"/"+padding);
		cipher.init(Cipher.DECRYPT_MODE, secKey);
		byte [] decodedMessageBytes = cipher.doFinal(Base64.getDecoder().decode(message));
		return decodedMessageBytes;
	}



	public IvParameterSpec genIV() {
		// TODO Auto-generated method stub
		IvParameterSpec iv;
		if (mode.compareTo("CBC")==0 || mode.compareTo("CFB")==0) {
			if (cipherType.compareTo("DES")==0 || cipherType.compareTo("DESede")==0 ) {
				SecureRandom random = new SecureRandom();
				byte IVBytes[] = new byte[8];
				random.nextBytes(IVBytes); 
				iv = new IvParameterSpec(IVBytes);
				
			}
			else {
				SecureRandom random = new SecureRandom();
				byte IVBytes[] = new byte[16];
				random.nextBytes(IVBytes); 
				iv = new IvParameterSpec(IVBytes);
				
			}
			return iv;
			}
		return null;
	}


	public byte[] symmetricDecoding(SecretKey secKey,
			String message, String ivString) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		// TODO Auto-generated method stub
		
		Cipher cipher = Cipher.getInstance(cipherType+"/"+mode+"/"+padding);
		byte [] IVBytes = Base64.getDecoder().decode(ivString);
		IvParameterSpec iv = new IvParameterSpec(IVBytes);
		cipher.init(Cipher.DECRYPT_MODE, secKey, iv);
		
		byte [] decodedMessageBytes = cipher.doFinal(Base64.getDecoder().decode(message));
		return decodedMessageBytes;
	}
}
