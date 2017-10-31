package CifrarioIbrido;

import java.security.*;
import java.util.ArrayList;

public class Incapsula {
	
	private String pubKeyFile = "publicKeyFile.txt";
	private ArrayList<User> utenti = new ArrayList<User>();
	
	public void addUser (String name, int dimKey, String padding) {
		AsymmetricCipher asymCipher = new AsymmetricCipher(dimKey, padding);
		KeyPair pair = asymCipher.genKeyPair();
		User utente = new User(name, pair.getPrivate());
		utenti.add(utente);
		FileManagement.savePublicKey(pubKeyFile, pair.getPublic(), padding);
	}
	

}
