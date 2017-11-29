package progetto3.timestamping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyRingTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HashMap<String, Client> user = new HashMap<String, Client>();
		
		Client demetrio = new Client("Demetrio", "111");
		Client michele = new Client("Michele", "222");
		Client giuseppe = new Client("Giuseppe", "333");
		user.put("Demetrio", demetrio);
		user.put("Michele", michele);
		user.put("Giuseppe", giuseppe);

		System.out.println("################# TEST KEYRING #################\n");
		
		System.out.println("-----------------Test con Utente validato-----------------\n");

		demetrio.userValidation("111");
		
		demetrio.addPasswordToKeyring("Amministratore", "Password", "Facebook", null, "Babbo");
		demetrio.addPasswordToKeyring("Amministratore", "Password", "Twitter", null, "Pippo");
		demetrio.addPasswordToKeyring("Amministratore", "Password", "Twitter", null, "Topolino");
				
		KeyPair p1 = KeyRingTest.generateKeyPair("RSA", "1024");
		demetrio.addKeyPairToKeyring("Studente", "Key", "RSA", "1024",p1);
		
		KeyPair p2 = KeyRingTest.generateKeyPair("RSA", "2048");
		demetrio.addKeyPairToKeyring("Studente", "Key", "RSA", "2048", p2);
		
		KeyPair p3 = KeyRingTest.generateKeyPair("DSA", "2048");
		demetrio.addKeyPairToKeyring("Studente", "Key", "DSA", "2048",p3);
		
		SecretKey s1 = KeyRingTest.generateSecretKey("AES", "128");
		demetrio.addSecretKeyToKeyring("Studente", "Key", "AES", "128", s1);
		
		System.out.println("La password di Facebook di Demetrio come amministratore è: " + demetrio.getPasswordFromKeyRing("Amministratore", "Password", "Facebook", null));
		System.out.println("La password di Twitter di Demetrio come amministratore è: " + demetrio.getPasswordFromKeyRing("Amministratore", "Password", "Twitter", null));
		
		SecretKey s = demetrio.getSecretKeyFromKeyRing("Studente", "Key", "AES", "128");
		System.out.println("Chiave segreta di Demetrio studente: " + Arrays.equals(s1.getEncoded(), s.getEncoded()));
		
		PublicKey pubk = demetrio.getPublicKeyFromKeyRing("Studente", "Key", "RSA", "2048");
		System.out.println("Chiave pubblica RSA 2048 di Demetrio come studente: " + Arrays.equals(pubk.getEncoded(), p2.getPublic().getEncoded()));
		
		PrivateKey privk = demetrio.getPrivateKeyFromKeyRing("Studente", "Key", "RSA", "2048");
		System.out.println("Chiave privata RSA 2048 di Demetrio come studente: " + Arrays.equals(privk.getEncoded(), p2.getPrivate().getEncoded()));
		
		System.out.println("\n-----------------Test con Utente non validato-----------------\n");
		
		System.out.println("Inserimento di una password nel Keyring di un utente non validato:");
		michele.addPasswordToKeyring("Studente", "Password ", "Whatsapp", null, "ehi");
		
		System.out.println("\nSalvataggio del KeyRing di un utente non validato:");
		michele.saveKeyRing("222");
		
		
		System.out.println("\n***************** SAVE USER *****************\n");
		
		//Serializzo il KeyRing dell'utente validato!
		System.out.println("\nSalvataggio del KeyRing di un utente validato:");

		demetrio.saveKeyRing("111");
		System.out.println("KeyRing dell'utente salvato!");
		KeyRingTest.saveUser(user);
		
		//simulazione chiusura applicazione
		user.clear();
		
		System.out.println("\n***************** RESTORE USER *****************\n");
		
		//recupero stato degli utenti
		user = KeyRingTest.restoreUser();
		
		System.out.println("\n-----------------Test con Utente validato-----------------\n");
		
		Client c1 = user.get("Demetrio");
		c1.userValidation("111");
		c1.restoreKeyRing("111");
		
		PublicKey keyPubRecovered = c1.getPublicKeyFromKeyRing("Studente", "Key", "RSA", "2048");
		System.out.println("Chiave pubblica RSA 2048 di Demetrio come studente: " + Arrays.equals(keyPubRecovered.getEncoded(), p2.getPublic().getEncoded()));
		
		PrivateKey keyPriRecovered = c1.getPrivateKeyFromKeyRing("Studente", "Key", "RSA", "2048");
		System.out.println("Chiave privata RSA 2048 di Demetrio come studente: " + Arrays.equals(keyPriRecovered.getEncoded(), p2.getPrivate().getEncoded()));
		
		PublicKey keyPubRecovered2 = c1.getPublicKeyFromKeyRing("Studente", "Key", "RSA", "1024");
		System.out.println("Chiave pubblica RSA 1024 di Demetrio come studente: " + Arrays.equals(keyPubRecovered2.getEncoded(), p1.getPublic().getEncoded()));
		
		PrivateKey keyPriRecovered2 = c1.getPrivateKeyFromKeyRing("Studente", "Key", "RSA", "1024");
		System.out.println("Chiave privata RSA 1024 di Demetrio come studente: " + Arrays.equals(keyPriRecovered2.getEncoded(), p1.getPrivate().getEncoded()));
		
		String passRecovered = c1.getPasswordFromKeyRing("Amministratore", "Password", "Twitter", null);
		System.out.println("La password di Twitter di Demetrio come amministratore: " + passRecovered);
		
		SecretKey secretRecovered = c1.getSecretKeyFromKeyRing("Studente", "Key", "AES", "128");
		System.out.println("Chiave segreta di Demetrio come studente: " + Arrays.equals(s1.getEncoded(), secretRecovered.getEncoded()));
		
		System.out.println("\n-----------------Test con chiave inesistente-----------------\n");
		
		System.out.println("Ricerca della chiave DES 128 dell'utente Demetrio come studente: ");
		SecretKey secretRecovered2 = c1.getSecretKeyFromKeyRing("Studente", "Key", "DES", "128");


		System.out.println("\n-----------------Test con Utente non validato-----------------\n");

		Client c2 = user.get("Giuseppe");
		c2.getPasswordFromKeyRing("Studente", "Password", "Whatsapp", null);
		
		}
	
	
	
	private static void saveUser(HashMap<String, Client> user){
		try {
			//Resetto il bit di validità a FALSE per tutti i client.
			for(String id: user.keySet()){
				Client client = user.get(id);
				client.setValidated(false);
			}
			//Scrivo su file la struttura dati contenente i client.
			FileOutputStream f = new FileOutputStream(new File("Clients.txt"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(user);
			f.close();
			o.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error initializing stream");
		}
	}
	
	private static HashMap<String, Client> restoreUser(){
		
		HashMap<String, Client> user = null;
		try {
			
			//Leggo da file la struttura dati contenente i client.
			FileInputStream f = new FileInputStream(("Clients.txt"));
			ObjectInputStream o = new ObjectInputStream(f);

			// Write objects to file
			try {
				user = (HashMap<String,Client>) o.readObject();
				for(String id : user.keySet())
					System.out.println(user.get(id).getID());

				return user;
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			f.close();
			o.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error initializing stream");
		}
		return user;
	}
	
	private static SecretKey generateSecretKey(String mode, String dim) {
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance(mode.toUpperCase());
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Algoritmo non supportato");

		}
		
		//Inizializziamo il KeyGenerator
		keyGenerator.init(Integer.parseInt(dim), new SecureRandom());

		//Generiamo la chiave
		SecretKey secretKey = keyGenerator.generateKey();
		
		return secretKey;
	}
	
	private static KeyPair generateKeyPair(String mode, String dim) {
		
		KeyPairGenerator keyPairGenerator = null;;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(mode.toUpperCase());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		keyPairGenerator.initialize(Integer.parseInt(dim), new SecureRandom());
		KeyPair pair = keyPairGenerator.generateKeyPair();
		return pair;
	}
}
