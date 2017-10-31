package CifrarioIbrido;

import java.io.IOException;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Prova {
	private static Incapsula inc = new Incapsula();

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		// TODO Auto-generated method stub
		
		inc.addUser("Michele", 1024, "PKCS1Padding");
		/*Path fileLocation = Paths.get("C:\\Users\\Michele\\git\\sicurezza\\progetto2\\publicKeyFile.pub");
		byte[] data = Files.readAllBytes(fileLocation);
		String encodedPubKey = Base64.getEncoder().encodeToString(data);
		System.out.println("Pubblica Base64 dopo aver salvato: " + encodedPubKey);*/
	

	}

}
