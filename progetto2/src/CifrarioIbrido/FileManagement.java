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
}
