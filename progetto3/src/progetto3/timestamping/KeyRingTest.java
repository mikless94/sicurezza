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
import java.util.HashSet;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyRingTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HashSet<Client> user = new HashSet<Client>();
		
		Client demetrio = new Client("Demetrio", "111");
		Client michele = new Client("Michele", "222");
		Client giuseppe = new Client("Giuseppe", "333");
		user.add(demetrio);
		user.add(michele);
		user.add(giuseppe);
		
		System.out.println("################# TEST KEYRING #################\n");
		
		System.out.println("-----------------Test con Utente validato-----------------\n");

		demetrio.userValidation("111");
		
		demetrio.addPasswordToKeyring("Amministratore", "Password", "Facebook", null, "Babbo");
		demetrio.addPasswordToKeyring("Amministratore", "Password", "Twitter", null, "Pippo");
		demetrio.addPasswordToKeyring("Amministratore", "Password", "Twitter", null, "Scemo");
				
		KeyPair p1 =  KeyRingTest.generateKeyPair("RSA", "1024");
		demetrio.addKeyPairToKeyring("Studente", "Key", "RSA", "1024",p1);
		KeyPair p2 =KeyRingTest.generateKeyPair("RSA", "2048");
		demetrio.addKeyPairToKeyring("Studente", "Key", "RSA", "2048", p2);
		KeyPair p3 =  KeyRingTest.generateKeyPair("DSA", "2048");
		demetrio.addKeyPairToKeyring("Studente", "Key", "DSA", "2048",p3);
		SecretKey s1 = KeyRingTest.generateSecretKey("AES", "128");
		demetrio.addSecretKeyToKeyring("Studente", "Key", "AES", "128", s1);
		
		System.out.println(demetrio.getPasswordFromKeyRing("Amministratore", "Password", "Facebook", null));
		System.out.println(demetrio.getPasswordFromKeyRing("Amministratore", "Password", "Twitter", null));
		
		SecretKey s = demetrio.getSecretKeyFromKeyRing("Studente", "Key", "AES", "128");
		System.out.println("Chiave segreta di demetrio: " + Arrays.equals(s1.getEncoded(), s.getEncoded()));
		
		PublicKey pubk = demetrio.getPublicKeyFromKeyRing("Studente", "Key", "RSA", "2048");
		System.out.println("Chiave pubblica RSA 2048 di demetrio: " + Arrays.equals(pubk.getEncoded(), p2.getPublic().getEncoded()));
		
		PrivateKey privk = demetrio.getPrivateKeyFromKeyRing("Studente", "Key", "RSA", "2048");
		System.out.println("Chiave privata RSA 2048 di demetrio: " + Arrays.equals(privk.getEncoded(), p2.getPrivate().getEncoded()));
		
		System.out.println("-----------------Test con Utente non validato-----------------\n");
		
		System.out.println("Inserimento di una chiave nel Keyring di un utente non validato!");
		michele.addPasswordToKeyring("Studente", "Password", "Whatsapp", null, "ehi");
		
		
		System.out.println("----------------- SAVE USER -----------------\n");
		
		//Serializzo il KeyRing dell'utente validato!
		demetrio.saveKeyRing("111");
		
		KeyRingTest.saveUser(user);
		user.clear();
		
		System.out.println("----------------- RESTORE USER -----------------\n");
				
		
		user = KeyRingTest.restoreUser();
		for(Client c : user){
			if(c.getID().equals("Demetrio")){
				c.userValidation("111");
				c.restoreKeyRing("111");
				}
				
								
			}
		}
	
	
	private static void saveUser(HashSet<Client> user){
		try {
			//Resetto il bit di validità a FALSE per tutti i client.
			for(Client c : user)
				c.setValidated(false);
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
	
	private static HashSet<Client> restoreUser(){
		
		HashSet<Client> user = null;
		try {
			
			//Leggo da file la struttura dati contenente i client.
			FileInputStream f = new FileInputStream(("Clients.txt"));
			ObjectInputStream o = new ObjectInputStream(f);

			// Write objects to file
			try {
				user = (HashSet<Client>) o.readObject();
				for(Client c : user)
					System.out.println(c.getID());

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
