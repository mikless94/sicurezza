package CifrarioIbrido;

import java.io.IOException;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

public class Incapsula {
	
	private String pubKeyFile = "publicKeyFile";
	private ArrayList<User> utenti = new ArrayList<User>();
	
	public void addUser (String name, int dimKey, String padding) throws NoSuchAlgorithmException, IOException {
		AsymmetricCipher asymCipher = new AsymmetricCipher(dimKey, padding);
		KeyPair pair = asymCipher.genKeyPair();
		User utente = new User(name, pair.getPrivate());
		utenti.add(utente);
		/*byte pubblica[] = pair.getPublic().getEncoded(); 
		String encodedPubKey = Base64.getEncoder().encodeToString(pubblica);
		System.out.println("Pubblica Base64 prima di salvare: " + encodedPubKey);*/
		FileManagement.savePublicKey(pubKeyFile, utente.getName(), pair.getPublic(), padding);
	}
	
	
	

}
