package CifrarioIbrido;

import java.io.*;
import java.security.*;
import java.util.Base64;

public class FileManagement {
	
	public static void savePublicKey(String fileName, String userName, PublicKey key, String padding) throws IOException {
		FileWriter out = new FileWriter(fileName,true);
		out.write(userName+" "+Base64.getEncoder().encodeToString(key.getEncoded())+" "+padding+System.lineSeparator());
		out.close();
	}

	public static void createFileToSend(String fileToSend, String cipheredKey, String cipheredMessage, String sender, String recipient,
			String cipherType, String mode, String padding, boolean sign) throws IOException {
		
		String f="";
		if (sign)
			f += "1";
		else
			f += "0";
		
		FileWriter out = new FileWriter(fileToSend);
		out.write(sender+System.lineSeparator()+recipient+System.lineSeparator()+cipherType+System.lineSeparator()+
				mode+System.lineSeparator()+padding+System.lineSeparator()+f+System.lineSeparator()+cipheredKey+System.lineSeparator()
				+cipheredMessage);
		out.close();
	}
	
	public static void saveDigitalKeysFile () {
		
	}
	
	
}
