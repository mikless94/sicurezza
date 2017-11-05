package CifrarioIbrido;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class AsymmetricCipher {
	
	//
	
	private int dimKey;
	private String padding;
	/**
	 * @param dimKey
	 * @param padding
	 */
	public AsymmetricCipher() {
	
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
	public String asymmetricEncoding(SecretKey secKey, PublicKey pubKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("RSA/ECB/"+padding);
		c.init(Cipher.ENCRYPT_MODE, pubKey);
		
		//generiamo chiave privata del cifrario simmetrico
		//secKey = symCipher.genSecretKey(cipherType, mode);
		//cifro chiave privata appena generata con la chiave pubblica del cifrario RSA relativa al destinatario desiderato
		byte [] cipheredKeyBytes = c.doFinal(secKey.getEncoded());
		
		//System.out.println(Base64.getEncoder().encodeToString(cipheredKeyBytes));
		
		/*c.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decodificato  = c.doFinal(ciphertext);
		System.out.println("Testo decifrato: " + new String(decodificato,"UTF8"));*/
		return Base64.getEncoder().encodeToString(cipheredKeyBytes);
	}
	public byte[] asymmetricDecoding(String cipherSymmetricKey, PrivateKey pvtKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		
		Cipher c = Cipher.getInstance("RSA/ECB/"+padding);
		c.init(Cipher.DECRYPT_MODE, pvtKey);
		return c.doFinal(Base64.getDecoder().decode(cipherSymmetricKey));
		
	}
	public String asymmetricEncoding(String symmetricCipherInfo, PublicKey pubKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		Cipher c = Cipher.getInstance("RSA/ECB/"+padding);
		c.init(Cipher.ENCRYPT_MODE, pubKey);
		
		//generiamo chiave privata del cifrario simmetrico
		//secKey = symCipher.genSecretKey(cipherType, mode);
		//cifro chiave privata appena generata con la chiave pubblica del cifrario RSA relativa al destinatario desiderato
		byte [] cipheredKeyBytes = c.doFinal(symmetricCipherInfo.getBytes());
		
		//System.out.println(Base64.getEncoder().encodeToString(cipheredKeyBytes));
		
		/*c.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decodificato  = c.doFinal(ciphertext);
		System.out.println("Testo decifrato: " + new String(decodificato,"UTF8"));*/
		return Base64.getEncoder().encodeToString(cipheredKeyBytes);
	}
	
	
	

}
