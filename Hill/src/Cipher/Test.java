package Cipher;

public class Test {

	public static void main(String[] args) {
		Hill h = new Hill();
		//System.out.println (h.genKey());
		try {
			h.setKey("ciao");
			System.out.println("Chiave valida!\n");
		}catch(InvalidKeyException ex) {};
		
				
	}
}


