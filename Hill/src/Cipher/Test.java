package Cipher;

public class Test {

	public static void main(String[] args) {
		Hill h = new Hill();
		String ciphertext = "";
		
		try {
			h.setKey("aede");
			System.out.println(h.getKey());
			String plaintext = "demetrio giuseppe michele";
			System.out.println ("La stringa da cifrare �: " + plaintext);

			ciphertext = h.enc(plaintext);
			System.out.println ("La stringa cifrata �: " + ciphertext);
			
			String plaintextDec = h.dec(ciphertext);
			System.out.println ("La stringa decifrata �: " + plaintextDec);
		}catch(InvalidKeyException ex) {};
	}
}


