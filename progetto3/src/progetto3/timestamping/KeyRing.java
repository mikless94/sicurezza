package progetto3.timestamping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class KeyRing implements Serializable{
  
  private String fileName;
  private static int IV_DIM = 16;
  private static int SALT_DIM = 8;
  private HashMap<String, ArrayList<byte []>> map;
  
  
  public KeyRing(String fileName){
    
    //associa il file all'istanza
    this.fileName = new File(fileName).getAbsolutePath();
    
    //crea una map
    this.map = new HashMap<String, ArrayList<byte []>>();
    
    //creo il salt per il keyring
    SecureRandom random_1 = new SecureRandom();
    byte[] salt = new byte[SALT_DIM];
    random_1.nextBytes(salt);
    
    //creo l'IV per il keyring
    SecureRandom random_2 = new SecureRandom();
    byte IVBytes[] = new byte[IV_DIM];
    random_2.nextBytes(IVBytes); 
    //IvParameterSpec iv = new IvParameterSpec(IVBytes);
    
    //scrivo nel file il salt e l'IV
    try {
      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
      out.write(salt);
      out.write(IVBytes);
      
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  
  public void addToKeyring (String role, String type, String algOrService, String dim, ArrayList<byte []> array) {
      
    String idMap = role.toUpperCase() + "/" + type.toUpperCase() + "/" + algOrService.toUpperCase() +
        "/" + dim.toUpperCase();
    this.map.put(idMap, array);
  }
  
  
  public ArrayList<byte []> getValueFromKeyRing(String role, String type, String algOrService, String dim){
    
    String idMap = role.toUpperCase() + "/" + type.toUpperCase() + "/" + algOrService.toUpperCase() +
        "/" + dim.toUpperCase();

      return this.map.get(idMap);
    }
  
  public void encodeData(String password){
	    byte[] salt = new byte[SALT_DIM];
	    byte IVBytes[] = new byte[IV_DIM];
	    
	    try {
	      ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.fileName));
	      in.read(salt);
	      in.read(IVBytes);
	      
	      in.close();
	      
	      char[] pass = password.toCharArray();
	      IvParameterSpec iv = new IvParameterSpec(IVBytes);
	      
	      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
	      KeySpec keySpec = new PBEKeySpec(pass, salt, 65536, 128);
	      SecretKey tmp = factory.generateSecret(keySpec);
	      SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	      
	      Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	      ciph.init(Cipher.ENCRYPT_MODE, secretKey, iv);
	      
	      //Serializzo e cifro la map
	      SealedObject data = new SealedObject(this.map, ciph);
	      
	      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.fileName));
	      
	      out.write(salt);
	      out.write(IVBytes);
	      
	      //scrivo la map su file
	      out.writeObject(data);
	      
	      this.map = null;
	      
	      out.close();
	    }catch(IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException e) {
	      e.printStackTrace();
	    }
	  }
	  
	  public HashMap<String, ArrayList<byte[]>> getMap() {
	return map;
}


public void setMap(HashMap<String, ArrayList<byte[]>> map) {
	this.map = map;
}


	public void decodeData(String password) {
	    
	    byte[] salt = new byte[SALT_DIM];
	    byte IVBytes[] = new byte[IV_DIM];
	    
	    try {
	      ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.fileName));
	      
	      in.read(salt);
	      in.read(IVBytes);
	      SealedObject sealedObject = (SealedObject) in.readObject();
	      
	      char[] pass = password.toCharArray();
	      IvParameterSpec iv = new IvParameterSpec(IVBytes);
	      
	      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
	      KeySpec keySpec = new PBEKeySpec(pass, salt, 65536, 128);
	      SecretKey tmp = factory.generateSecret(keySpec);
	      SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
	      
	      Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	      
	      ciph.init(Cipher.DECRYPT_MODE, secretKey, iv);
	      
	      this.map = (HashMap<String, ArrayList<byte []>>) sealedObject.getObject(ciph);
	      
	      in.close();
	    } catch(EOFException eof){
	      
	    }
	    catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | ClassNotFoundException | IllegalBlockSizeException | BadPaddingException e) {
	      e.printStackTrace();
	    }

	  }
	  
	  public void printMap() {
	    String s = "";
	    
	    for(String key : map.keySet()) {
	      s += key + "\n";
	    }
	    
	    System.out.println(s);
	  }
}
