package CifrarioIbrido;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

public class DigitalSign {
	
	private int dimKey;
	private String type;
	

	/**
	 */
	public DigitalSign() {
		
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


	public KeyPair genKeyPair() throws NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
		keyPairGenerator.initialize(dimKey, new SecureRandom());
		return keyPairGenerator.generateKeyPair();
	
	}


	public String sign(String messagePath, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
		// TODO Auto-generated method stub
		Signature dsa = Signature.getInstance("signType");
		dsa.initSign(privateKey);
		Path path = Paths.get(messagePath);
		dsa.update(Files.readAllBytes(path));
		byte[] firma = dsa.sign();
		return Base64.getEncoder().encodeToString(firma);
	}

}
