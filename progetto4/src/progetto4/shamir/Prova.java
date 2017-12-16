package progetto4.shamir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Prova {

	/*Per distribuire tutti i file dei client decommentare la funzione init() e commentare codice successivo
	 * Per ricostruire lo specifico file del client commentare init() e decommentare codice successivo  
	 */
	public static void main(String[] args) {
		

		//usiamo per inviare le richieste shamir 
		//init (5,3);
		
		//usiamo per ricostruire file con partecipanti
		BigInteger p1 = BigInteger.valueOf(1);
		BigInteger p2 = BigInteger.valueOf(2);
		BigInteger p4 = BigInteger.valueOf(4);
		ArrayList<BigInteger> partecipants = new ArrayList<BigInteger>();
		partecipants.add(p1);
		partecipants.add(p2);
		partecipants.add(p4);
		
		String fileName = "C:\\Users\\Michele\\git\\sicurezza\\progetto4\\.\\doc2\\messaggio3.txt";
		reconstruct (fileName, partecipants);
		
	
	}
	
	private static void reconstruct(String fileName, ArrayList<BigInteger> partecipants) {
		DistributedStorageService dss = null;
		dss = restoreService();
		dss.reconstructFile(fileName, partecipants);
		System.out.println("Ricostruzione "+fileName+" avvenuta con successo");
	}

	private static void init (int n, int k) {
		System.out.println("creazione share Shamir "+"("+n+","+k+")");
		
		DistributedStorageService dss = DistributedStorageService.getInstance(n,k);
		ArrayList <Client> clients = new ArrayList <Client> ();
		
		Client c1 = new Client("1");
		Client c2 = new Client("2");
		Client c3 = new Client("3");
		
		clients.add(c1);
		clients.add(c2);
		clients.add(c3);
		
		System.out.println("#################INVIO RICHIESTE SHAMIR#################");
		//ogni utente manda le sue query
		for (Client client: clients) {
			System.out.println("utente "+client.getID());
			File directory = new File ("./doc"+client.getID());
			File [] files = directory.listFiles();
			
			for (int i = 0; i< files.length; i++) {
				System.out.println("invio richiesta Shamir relativa al documento "+files[i].getAbsolutePath());
				dss.distributeFile(files[i].getAbsolutePath());
				
			}
		}
		saveService (dss);
	}
	
	private static void saveService(DistributedStorageService dss){
		try {

			//Scrivo su file la struttura dati contenente il Servizio salvandone il suo stato.
			FileOutputStream f = new FileOutputStream(new File("Storage Service.dds"));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(dss);
			f.close();
			o.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error initializing stream");
		}
	}
	
	private static DistributedStorageService restoreService(){
		
		DistributedStorageService  dss = null;
		try {
			
			//Leggo da file la struttura dati contenente il servizio.
			FileInputStream f = new FileInputStream(("Storage Service.dds"));
			ObjectInputStream o = new ObjectInputStream(f);

			try {
				dss = (DistributedStorageService) o.readObject();
				return dss;
				
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
		return dss;
	}
}
