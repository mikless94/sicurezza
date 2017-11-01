package CifrarioIbrido;

import java.io.IOException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Prova {
	private static Incapsula inc = new Incapsula();

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		
		inc.addUser("Michele", 1024, "PKCS1Padding");
		inc.addUser("Giuseppe", 2048, "PKCS1Padding");
		inc.messageToSend ("Michele", "Giuseppe", "AES", "CBC", "PKCS5Padding", true , "C:\\Users\\Michele\\Desktop\\messaggiopergiuseppe.txt");
		//System.out.println("chiave simmetrica cifrata con RSA: "+inc.encodeSymmetricPrivateKey ("Giuseppe", "AES", "CBC"));
		
		//System.out.println("size chiave cifrata "+s.length());
		/*Path fileLocation = Paths.get("C:\\Users\\Michele\\git\\sicurezza\\progetto2\\publicKeyFile.pub");
		byte[] data = Files.readAllBytes(fileLocation);
		String encodedPubKey = Base64.getEncoder().encodeToString(data);
		System.out.println("Pubblica Base64 dopo aver salvato: " + encodedPubKey);*/
	

	}

}
