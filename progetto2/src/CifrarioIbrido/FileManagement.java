package CifrarioIbrido;

import java.io.*;
import java.security.*;

public class FileManagement {
	
	public static void savePublicKey(String fileName, String userName, PublicKey key, String Padding) throws IOException {
		FileOutputStream out = new FileOutputStream(fileName+".pub");
		out.write(key.getEncoded());
		out.close();
	}
}
