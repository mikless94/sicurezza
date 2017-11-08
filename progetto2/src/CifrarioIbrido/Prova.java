package CifrarioIbrido;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.io.FileUtils;


public class Prova {
	private static Incapsula inc = new Incapsula( );
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SignatureException, InvalidAlgorithmParameterException {
		// TODO Auto-generated method stub
		
		Files.deleteIfExists(Paths.get("./publicKeyFile.txt"));
		Files.deleteIfExists(Paths.get("./fileToSend.txt"));
		Files.deleteIfExists(Paths.get("./digKeysFile.txt"));
		Files.deleteIfExists(Paths.get("./myTempFile.txt"));
		Files.deleteIfExists(Paths.get("./pvtKeysFile.txt"));
		Files.deleteIfExists(Paths.get("./pvtDigitalKeysFile.txt")) ;
		
		ArrayList <String> asymPaddings = new ArrayList <String> (); 
		asymPaddings.add("PKCS1Padding"); 
		asymPaddings.add("OAEPPadding"); 
		
		ArrayList <Integer> asymKeyDims = new ArrayList <Integer> ();
		asymKeyDims.add(1024);
		asymKeyDims.add(2048);
		
		ArrayList <String> types = new ArrayList <String> (); 
		types.add("AES"); 
		types.add("DES"); 
		types.add("DESede");
		
		ArrayList <String> modes = new ArrayList <String> ();
		modes.add("ECB");
		modes.add("CBC");
		modes.add("CFB");
		
		System.out.println ("############PROVA TRASMISSIONE#################\n");
		
		for (String asymPadding: asymPaddings) {
			for(Integer asymKeyDim : asymKeyDims ) {
				inc.addUser("Michele", asymKeyDim, asymPadding, "ciao"); 
				inc.addUser("Giuseppe",asymKeyDim , asymPadding, "hello");
				System.out.println ("###################################################################");
				System.out.println ("Trasmissione verso utente con cifrario RSA "+asymKeyDim+" e padding " +asymPadding);
				System.out.println ("###################################################################");
				for (String type: types) {
					for (String mode: modes) {
						inc.messageToSend ("Michele", "Giuseppe", type, mode, "PKCS5Padding", "./message.txt");
						inc.decodeMessage("./messageDec.txt" );
						File file1 = new File("./message.txt");
						File file2 = new File("./messageDec.txt");
						if ( FileUtils.contentEquals(file1, file2) ) {
							System.out.println ("-----------------------------------------------------------------");
							System.out.println ("Trasmissione con tipo "+type+" e modo operativo " +mode+" avvenuta con successo\n");
							System.out.println ("-----------------------------------------------------------------");
						}
						Files.deleteIfExists(Paths.get("./messageDec.txt"));
					}
				}
				
			}
			
		}
		
		
		ArrayList <Integer> dims = new ArrayList <Integer> (); 
		dims.add(1024); 
		dims.add(2048); 
		
		ArrayList <String> signTypes = new ArrayList <String> ();
		//signTypes.add("SHA1withDSA");
		signTypes.add("SHA224withDSA");
		signTypes.add("SHA256withDSA");
		
		System.out.println ("\n############PROVA FIRMA#################\n");
		
		//prova combinazioni firma
		
		for (String type: signTypes) {
			for (Integer dim: dims) {
				inc.messageToSend ("Michele", "Giuseppe", "AES", "CBC", "PKCS5Padding", "./message.txt", dim, type);
				inc.decodeMessage("./messageDec.txt");
				File file1 = new File("./message.txt");
				File file2 = new File("./messageDec.txt");
				if ( FileUtils.contentEquals(file1, file2) ) {
					System.out.println ("Trasmissione con tipo di firma "+type+" e dimensione chiave " +dim+" avvenuta con successo\n");
					System.out.println ("---------------------------------------------------------");
				}
				Files.deleteIfExists(Paths.get("./messageDec.txt"));
			}
		}
	}

}
