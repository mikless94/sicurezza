package progetto4.shamir;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import progetto3.timestamping.TSA;
public class DistributedStorageService {
	
	//Numeri di server disponibili
	private int n;
	//Numero di partecipanti
	private int k;
	//La dimensione del blocco è espressa in byte.
	private final int BLOCKDIMENSION = 8;
	//La dimensione è espressa in bit. In questo caso 16 byte espresso in bit.
	private final int modLength = 16*8;
	private final int CERTAINTY = 50;
	private SecretSharing shamir;
	private ArrayList<String> server;
	private static DistributedStorageService instance = null;


	
	private DistributedStorageService(int n, int k) {
		this.n = n;
		this.k = k;
		shamir = new SecretSharing(n, k);
		
		for(int i=1; i<=n; i++){
			File f = new File(".//" + "server" + i);
			f.mkdir();
			server.add(f.getPath());
		}
	}
	
	public static DistributedStorageService getInstance(int n, int k) {
	      if(instance == null) {
	         instance = new DistributedStorageService(n,k);
	      }
	      return instance;
	   }

	//divide il file in blocchi di m byte
	//genera il numero primo p (funzione del prof) maggiore di 2^m
	//calcolare coefficienti random per blocco
	//richiama la funzione generateShares per ogni blocco
	//scrive share su ogni server
	//calcola MAC per ogni share e lo associa al client
	public void distributeFile (String fileName) {
		
		for(int i = 1; i <= n; i++){
			File f;
			do{
				f = new File(server.get(i-1) + "/" + genRandomName() + ".txt");
			} while(!f.createNewFile());
		}
		
		BigInteger p = genPrime();
		BigInteger [] coeff = new BigInteger[this.k-1];
		for(int i = 0; i < k-1; i++){
			coeff[i] = randomZp(p);
		}
		
		byte[] stream = new byte[BLOCKDIMENSION];
		try {
			ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
			
			while(in.read(stream) != -1){
				BigInteger secret = new BigInteger(1, stream);
				BigInteger [] shares = shamir.generateShares(p, secret, coeff);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	//dati gli share dei partecipanti ricostruisce il file
	//controllo MAC
	public void reconstructFile (String fileName, ArrayList<BigInteger> participants) {
		
	}
	
	private void computeMAC (String share) {
		
	}
	
	private boolean verifyMAC (String share, byte[] MAC) {
		return true;
	}
	
	
	
	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	private BigInteger genPrime() {
		BigInteger p=null;
		boolean ok=false;
		do{
			p=BigInteger.probablePrime(this.modLength, new Random());
			if(p.isProbablePrime(this.CERTAINTY) && (p.compareTo(BigInteger.valueOf((long) Math.pow(2, BLOCKDIMENSION*8))) == 1))
				ok=true;
		}while(ok==false);
			return p;
		} 
	
	private BigInteger randomZp(BigInteger p) {
		BigInteger r;
		do{
			r = new BigInteger(modLength, new Random());
		}
		while (r.compareTo(BigInteger.ZERO) < 0 || r.compareTo(p) >= 0);
		
		return r;
		}
	
	private String genRandomName(){
		String output;
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return output = sb.toString();
	}
}
