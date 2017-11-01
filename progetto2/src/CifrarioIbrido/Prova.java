package CifrarioIbrido;

import java.io.IOException;
import java.nio.file.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Prova {
	private static Incapsula inc = new Incapsula();

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// TODO Auto-generated method stub
		
		inc.addUser("Michele", 1024, "PKCS1Padding");
		inc.addUser("Giuseppe", 2048, "PKCS1Padding");
		System.out.println(Base64.getEncoder().encodeToString(inc.encodeSymmetricPrivateKey ("Giuseppe", "AES", "CBC")));
		/*Path fileLocation = Paths.get("C:\\Users\\Michele\\git\\sicurezza\\progetto2\\publicKeyFile.pub");
		byte[] data = Files.readAllBytes(fileLocation);
		String encodedPubKey = Base64.getEncoder().encodeToString(data);
		System.out.println("Pubblica Base64 dopo aver salvato: " + encodedPubKey);*/
	

	}

}
