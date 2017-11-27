package progetto3.timestamping;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ProvaKeyRing {
	private static KeyRing instance = null;

    
    public static KeyRing getInstance() {
	      if(instance == null) {
	         instance = new KeyRing("prova.txt");
	      }
	      return instance;
	   }
    
    public static void main(String[] args) {
    
    	System.out.println("******Test di prova per il keyring.******");
    
    	KeyRing k = ProvaKeyRing.getInstance();
    	
    
    	byte[] b = new byte[10];
    	ArrayList<byte[]> prova = new ArrayList<byte[]>();
    	prova.add(b);
    	System.out.println(b);

    	k.addToKeyring("Giuseppe", "Key", "AES/CBC", "128", prova);
    	k.addToKeyring("Demetrio", "Pass", "lollazza", "null", prova);
    
    	k.encodeData("ehi");
	  
    	k.decodeData("ehi");
		ArrayList<byte[]> x = k.getValueFromKeyRing("Giuseppe", "Key", "AES/CBC", "128");
		System.out.println(x.get(0));      
  }
  
}