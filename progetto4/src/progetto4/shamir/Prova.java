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

	public static void main(String[] args) {
		//test generazione shares
		/*BigInteger [] a = new BigInteger [] {BigInteger.valueOf(11),BigInteger.valueOf(2)};
		BigInteger [] shares;
		BigInteger prime = BigInteger.valueOf(19);
		SecretSharing s = new SecretSharing(5, 3);
		shares = s.generateShares(prime, new BigInteger ("12"), a);
		System.out.println("shares");
		for (BigInteger k:shares) {
			System.out.print(k+"\t");
		}*/
		
		//test ricostruzione segreto
		/*prime = BigInteger.valueOf(11);
		BigInteger [] participants1 = new BigInteger [] {BigInteger.valueOf(1),BigInteger.valueOf(4)};
		BigInteger [] participants2 = new BigInteger [] {BigInteger.valueOf(2),BigInteger.valueOf(0)};
		BigInteger [] participants3 = new BigInteger [] {BigInteger.valueOf(5),BigInteger.valueOf(4)};
		ArrayList <BigInteger []> info = new ArrayList <BigInteger []> ();
		info.add(participants1);
		info.add(participants2);
		info.add(participants3);
		BigInteger secret = s.rebuildSecret(info, prime);
		System.out.println("\nsegreto ricostruito "+secret);*/
		
		
		
		DistributedStorageService dss = DistributedStorageService.getInstance(5,3);
		dss.distributeFile("C:\\Users\\Demetrio Laveglia\\Desktop\\CIAO.docx");
		saveService(dss);

		/*DistributedStorageService dss = restoreService();
		BigInteger p1 = BigInteger.valueOf(1);
		BigInteger p2 = BigInteger.valueOf(2);
		BigInteger p4 = BigInteger.valueOf(4);
		
		ArrayList<BigInteger> partecipants = new ArrayList<BigInteger>();
		partecipants.add(p1);
		partecipants.add(p2);
		partecipants.add(p4);
		
		dss.reconstructFile("C:\\Users\\Demetrio Laveglia\\Desktop\\CIAO.docx", partecipants);*/
		
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
				for(String s : dss.getFiles().keySet()){
					System.out.println("File salvato: " + s);
					for(Server server : dss.getFiles().get(s).keySet())
						System.out.println("Server: " + server.getDirectory() + " , Filename: " + dss.getFiles().get(s).get(server));
				}

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
