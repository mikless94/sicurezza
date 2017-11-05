package CifrarioIbrido;


import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class Prova {
	private static Incapsula inc = new Incapsula( );
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SignatureException, InvalidAlgorithmParameterException {
		// TODO Auto-generated method stub
		
		/*Files.deleteIfExists(Paths.get("./publicKeyFile.txt"));
		Files.deleteIfExists(Paths.get("./fileToSend.txt"));
		Files.deleteIfExists(Paths.get("./digKeysFile.txt"));
		Files.deleteIfExists(Paths.get("./myTempFile.txt"));
		
		inc.addUser("Michele", 1024, "PKCS1Padding"); 
		inc.addUser("Giuseppe", 2048, "PKCS1Padding");
		inc.addUser("Giovanni", 2048, "PKCS1Padding");*/
		//inc.deleteUser("Giovanni");
		
		//inc.messageToSend ("Michele", "Giuseppe", "AES", "CFB", "PKCS5Padding", "C:\\Users\\Michele\\Desktop\\jamesharden.jpg"/*, 1024, "SHA1withDSA"*/);
		//inc.decodeMessage("C:\\Users\\Michele\\git\\sicurezza\\progetto2\\fileToSend.txt", "C:\\Users\\Michele\\Desktop\\jameshardendec.jpg" );
		
		//inc.messageToSend ("Michele", "Giovanni", "DESede", "CBC", "PKCS5Padding", "C:\\Users\\Michele\\Desktop\\messaggio.txt", 2048, "SHA224withDSA");
		//inc.decodeMessage("C:\\Users\\Michele\\git\\sicurezza\\progetto2\\fileToSend.txt", "C:\\Users\\Michele\\Desktop\\messaggiodec.txt" );
	}

}
