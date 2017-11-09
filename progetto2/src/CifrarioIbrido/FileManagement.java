package CifrarioIbrido;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

public class FileManagement {
	
	public static void savePublicKey(String fileName, String userName, PublicKey key, String padding) throws IOException {
		FileWriter out = new FileWriter(fileName,true);
		out.write(userName+" "+Base64.getEncoder().encodeToString(key.getEncoded())+" "+padding+System.lineSeparator());
		out.close();
	}

	public static void createFileToSend(String fileToSend, String cipheredInfo, String cipheredKey, String cipheredMessage, String sender, String recipient,
			String cipherType, String mode, String padding) throws IOException {
		
		FileWriter out = new FileWriter(fileToSend);
		out.write(sender+System.lineSeparator()+recipient+System.lineSeparator()+cipheredInfo+System.lineSeparator()+
				cipheredKey+System.lineSeparator()+cipheredMessage+System.lineSeparator());
		out.close();
	}
	
	public static void createFileToSend(String fileToSend, String cipheredInfo, String cipheredKey, String cipheredMessage, String sender,
			String recipient, String cipherType, String mode, String padding, String sign) throws IOException {
		// TODO Auto-generated method stub
		FileWriter out = new FileWriter(fileToSend);
		out.write(sender+System.lineSeparator()+recipient+System.lineSeparator()+cipheredInfo+System.lineSeparator()+
				cipheredKey+System.lineSeparator()+cipheredMessage+System.lineSeparator()+sign);
		out.close();
		
	}
	
	public static void saveDigitalKeysFile (String file, String sender, PublicKey publicKey, String signType) throws IOException {
		Path path = Paths.get(file);

		if (!Files.exists(path)) {
			FileWriter out = new FileWriter(file,true);
			out.write(sender +" "+Base64.getEncoder().encodeToString(publicKey.getEncoded())+" "+signType+System.lineSeparator());
			out.close();
		}
		
		else {
			String tempFile = "myTempFile.txt";
			BufferedReader in = new BufferedReader(new FileReader(file));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			String read = null;
			
			while ((read = in.readLine()) != null) {
				String[] splited = read.split("\\s+");
				if (splited[0].compareTo(sender)!=0) 
					writer.write(read + System.lineSeparator());
				else 
					writer.write(sender +" "+Base64.getEncoder().encodeToString(publicKey.getEncoded())+" "+signType+System.lineSeparator()); 
		    }
			writer.close();
			in.close();
			
			BufferedWriter writer2 = new BufferedWriter(new FileWriter(file));
			BufferedReader in2 = new BufferedReader(new FileReader(tempFile));
			String read2 = null;
			while ((read2 = in2.readLine()) != null) {
		    	 writer2.write(read2 + System.getProperty("line.separator"));
		    }
			writer2.close();
			in2.close();
			Files.deleteIfExists(Paths.get("./"+tempFile));
		}
		}

	public static void savePrivateKey(String pvtKeysFile, String userName, String pvtKeyString) throws IOException {
		
		FileWriter out = new FileWriter(pvtKeysFile, true);
		out.write(userName + " " + pvtKeyString + " "+ System.lineSeparator());
		out.close();
	}
	
	public static String readPrivateKey(String fileName, User user) throws IOException {
		Path path = Paths.get(fileName);
		String cipheredKey = null;
		
		if (!Files.exists(path)) {
			return null;
		}
		else {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String read = null;
			
			while ((read = in.readLine()) != null) {
				String[] splited = read.split("\\s+");
				if (splited[0].compareTo(user.getName())==0) {
					cipheredKey = splited[1];
					break;
				}
		}
			in.close();
	}
		return cipheredKey;
 } 
	
public static void saveDigitalPrivateKey(String pvtKeysFile, String userName, String pvtKeyString) throws IOException {
		
	
	Path path = Paths.get(pvtKeysFile);

	if (!Files.exists(path)) {
		FileWriter out = new FileWriter(pvtKeysFile, true);
		out.write(userName + " " + pvtKeyString + System.lineSeparator());
		out.close();
	}
	
	else {
		String tempFile = "myTempFile.txt";
		BufferedReader in = new BufferedReader(new FileReader(pvtKeysFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		String read = null;
		
		while ((read = in.readLine()) != null) {
			String[] splited = read.split("\\s+");
			if (splited[0].compareTo(userName)!=0) 
				writer.write(read + System.lineSeparator());
			else 
				writer.write(userName +" "+ pvtKeyString + System.lineSeparator()); 
	    }
		writer.close();
		in.close();
		
		BufferedWriter writer2 = new BufferedWriter(new FileWriter(pvtKeysFile));
		BufferedReader in2 = new BufferedReader(new FileReader(tempFile));
		String read2 = null;
		while ((read2 = in2.readLine()) != null) {
	    	 writer2.write(read2 + System.getProperty("line.separator"));
	    }
		writer2.close();
		in2.close();
		Files.deleteIfExists(Paths.get("./"+tempFile));
	}
		
	}

public static String readDigitalPrivateKey(String fileName, User user) throws IOException {
	Path path = Paths.get(fileName);
	String cipheredKey = null;
	
	if (!Files.exists(path)) {
		return null;
	}
	else {
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String read = null;
		
		while ((read = in.readLine()) != null) {
			String[] splited = read.split("\\s+");
			if (splited[0].compareTo(user.getName())==0) {
				cipheredKey = splited[1];
				break;
			}
	}
		in.close();
}
	return cipheredKey;
}

public static void removeUserFromFile(String name, String filePath) throws IOException {
	Path path = Paths.get(filePath);
	String tempFile = "myTempFile.txt";
	
	if (Files.exists(path)) {
	     BufferedReader in = new BufferedReader(new FileReader(filePath));
	     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
	     String read = null;
	     while ((read = in.readLine()) != null) {
	    	 String[] splited = read.split("\\s+");
	         if (splited[0].compareTo(name)==0)
	        	 continue;
	         writer.write(read + System.getProperty("line.separator"));
	        }
	     writer.close(); 
	     in.close();
	        
	     BufferedWriter writer2 = new BufferedWriter(new FileWriter(filePath));
	     BufferedReader in2 = new BufferedReader(new FileReader(tempFile));
	     String read2 = null;
	     while ((read2 = in2.readLine()) != null) {
	    	 writer2.write(read2 + System.getProperty("line.separator"));
	     }
	     writer2.close();
	     in2.close();
	}
	Files.deleteIfExists(Paths.get("./"+tempFile));
}

	
} 
	
