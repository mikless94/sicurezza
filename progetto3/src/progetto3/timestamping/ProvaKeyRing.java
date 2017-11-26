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

  public static void main(String[] args) {
    
    System.out.println("******Test di prova per il keyring.******");
    KeyRing key = new KeyRing("prova.txt");
    
    byte[] b = new byte[10];
    ArrayList<byte[]> prova = new ArrayList<byte[]>();
    prova.add(b);
    
    System.out.println("INIZIO ENCODING\n");
    
    key.addToKeyring("Giuseppe", "Key", "AES/CBC", "128", prova);
    key.addToKeyring("Demetrio", "Pass", "lollazza", "null", prova);
    
    System.out.println("FINE ENCODING\n");
    
    System.out.println("INIZIO DECODING\n");
    
    key.decodeData("ciao");
    
    key.printMap();
    
    System.out.println("FINE DECODING\n");
    
  }
  
}