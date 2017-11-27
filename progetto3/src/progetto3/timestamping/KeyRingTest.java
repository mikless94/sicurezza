package progetto3.timestamping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

public class KeyRingTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*HashSet<Client> user = new HashSet<Client>();
		
		Client demetrio = new Client("Demetrio", "1");
		Client michele = new Client("Michele", "2");
		Client giuseppe = new Client("Giuseppe", "3");
		user.add(demetrio);
		user.add(michele);
		user.add(giuseppe);
		
		demetrio.userValidation("1");
		
		byte[] b = new byte[10];
		ArrayList<byte[]> array = new ArrayList<byte[]>();
		array.add(b);
		System.out.println(b);
		
		demetrio.addToKeyring("Studente", "Key", "AES", "128", array);

		ArrayList<byte[]> x = demetrio.getValueFromKeyRing("Studente", "Key", "AES", "128");
		System.out.println(x.get(0));
		
		demetrio.saveKeyRing("1");
				
		KeyRingTest.saveUser(user);*/
		
		HashSet<Client> user = KeyRingTest.restoreUser();
		for(Client c : user){
			if(c.getID().equals("Demetrio")){
				c.userValidation("1");
				c.restoreKeyRing("1");
				ArrayList<byte[]> x = c.getValueFromKeyRing("Studente", "Key", "AES", "128");
				System.out.println(x.get(0));				
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
	
	

}
