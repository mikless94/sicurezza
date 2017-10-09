package Cipher;

public interface ClassicCipher {
	void setKey (String key) throws InvalidKeyException;
	String getKey();
	String genKey();
	String enc (String plainText) throws InvalidPlaintext;
	String dec (String cipherText);
}

