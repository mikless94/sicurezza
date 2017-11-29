package progetto3.timestamping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TimeStampTest {
	private static HashMap<String, Client> user;
	private static TSA tsa;

	public static void main(String[] args) {
		// creazione marche
		init();
		
		//verifica marche (può avvenire in un qualsiasi momento a partire dalla risposta della TSA
		verify ();	
	}
	
	private static void init () {
		
		user = new HashMap<String, Client>();
		
		Client c1 = new Client("1", "ciao");
		Client c2 = new Client("2", "hello");
		Client c3 = new Client("3", "world");
		TSA tsa = c1.getTsa();
		
		//aggiunta utenti al sistema
		user.put(c1.getID(), c1);
		user.put(c2.getID(), c2);
		user.put(c3.getID(), c3);
		
		
		System.out.println("#################INVIO RICHIESTE#################");
		//ogni utente manda le sue query
		for (String key: user.keySet()) {
			System.out.println("utente "+key);
			File directory = new File ("./doc"+key);
			File [] files = directory.listFiles();
			
			for (int i = 0; i< files.length; i++) {
				//il primo terzo dei file e inviato da demetrio
				Client c = user.get(key);
				try {
					System.out.println("invio richiesta relativa al documento "+files[i].getPath());
					c.sendQuery(files[i].getPath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println();
			
		}
		
		//TSA genera marche 
		try {
			tsa.generateReply();
		} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		saveUser (user);
		user = null;
		saveTSA (tsa);
		tsa = null;
	}
	
	private static void verify () {
		user = restoreUser ();
		tsa = restoreTSA ();
		
		System.out.println("\n#################VERIFICA MARCHE#################");
		//ogni utente manda le sue query
		for (String key: user.keySet()) {
			File directory = new File ("./doc"+key);
			File [] files = directory.listFiles();
		
			for (int i = 0; i< files.length; i++) {
				//il primo terzo dei file e inviato da demetrio
				Client c = user.get(key);
				System.out.println("\nverifica marca relativa al documento "+files[i].getPath());
				System.out.println ("verifica firma: "+c.verifySign(files[i].getPath()));
				System.out.println ("verifica offline: "+c.verifyOffline(files[i].getPath()));
				System.out.println ("verifica online: "+c.verifyOnline(files[i].getPath()));
				System.out.println ("verifica catena di lunghezza 1: "+c.verifyChain(files[i].getPath(), 1));
				System.out.println ("verifica intera catena: "+c.verifyChain(files[i].getPath(), -1));
			}
		}
	}
	
	private static void saveUser(HashMap<String, Client> user){
		
		try {
			//Resetto il bit di validità a FALSE per tutti i client.
			for(String s : user.keySet()) {
				user.get(s).setValidated(false);
			}
			
			//Scrivo su file la struttura dati contenente i client.
			FileOutputStream f = new FileOutputStream(new File("ClientsTSA.txt"));
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
	
	private static void saveTSA(TSA tsa){
		try {
			
			//Scrivo su file la struttura dati contenente i client.
			FileOutputStream f = new FileOutputStream(new File("ServerTSA.txt"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(tsa);
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
			FileInputStream f = new FileInputStream(("ClientsTSA.txt"));
			ObjectInputStream o = new ObjectInputStream(f);

			// Write objects to file
			try {
				user = (HashMap<String, Client>) o.readObject();
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
	
	private static TSA restoreTSA(){
		TSA tsa = null;
		try {
			
			//Leggo da file la struttura dati contenente i client.
			FileInputStream f = new FileInputStream(("ServerTSA.txt"));
			ObjectInputStream o = new ObjectInputStream(f);

			// Write objects to file
			try {
				tsa = (TSA) o.readObject();
				return tsa;
				
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
		return tsa;
	}

}
