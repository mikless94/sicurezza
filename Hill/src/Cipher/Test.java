package Cipher;

public class Test {

	public static void main(String[] args) {
		Hill h = new Hill();
		String cipherText = "";
		
		//System.out.println (h.genKey());
		try {
			h.setKey("lidh");
		}catch(InvalidKeyException ex) {};
		 
		cipherText = h.enc("july");
		System.out.println (cipherText);
	
	}
}


