package progetto3.timestamping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;

public class Repository implements Serializable {
	
	private HashMap<String, byte[]> map ;
	private String fileName ;
	private static Repository instance = null;
	/**
	 * @param map
	 * @param fileName
	 */
	public Repository() {
		this.map = new HashMap<String, byte[]> ();
		this.fileName = new File ( "./repository.pub").getAbsolutePath() ;
		saveRepository ();
	}
	
	public static Repository getInstance() {
	      if(instance == null) {
	         instance = new Repository();
	      }
	      return instance;
	   }

	public void addToRepository (String ID, String role, String type, PublicKey pubKey) {
		restoreRepository();
		String idMap;
		idMap = ID.toUpperCase() + "/" + role.toUpperCase() + "/" + type.toUpperCase();
	    map.put(idMap, pubKey.getEncoded());
	    saveRepository();
	  }
	
	public PublicKey getFromRepository (String ID, String role, String type) {
		restoreRepository();
		String idMap;
		idMap = ID.toUpperCase() + "/" + role.toUpperCase() + "/" + type.toUpperCase();
	    byte [] pubBits = map.get(idMap);
	    PublicKey key = null;
	    try {
			key = KeyFactory.getInstance(type).generatePublic(new X509EncodedKeySpec(pubBits));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	    return key;
	  }
	
	
	private void saveRepository(){
		try {
			//Scrivo su file la struttura dati contenente i client.
			FileOutputStream f = new FileOutputStream(new File(fileName));
			ObjectOutputStream o = new ObjectOutputStream(f);

			// Write objects to file
			o.writeObject(map);
			f.close();
			o.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error initializing stream");
		}
	}
	
	private void restoreRepository(){
		map = null;
		try {
			
			//Leggo da file la struttura dati contenente i client.
			FileInputStream f = new FileInputStream((fileName));
			ObjectInputStream o = new ObjectInputStream(f);

			// Write objects to file
			try {
				map = (HashMap<String, byte[]>) o.readObject();
				
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
	}
	


}
