package progetto4.shamir;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DistributedStorageService implements Serializable{
	
	//Numeri di server disponibili
	private static int n;
	//Numero di partecipanti
	private static int k;
	//numero di byte per blocco
	private final int BLOCK_DIMENSION = 8;
	//La dimensione è espressa in bit. In questo caso 9 byte espresso in bit.
	private final int MOD_LENGTH = (BLOCK_DIMENSION+1)*8;
	private final int CERTAINTY = 50;
	private SecretSharing shamir;
	private static DistributedStorageService instance = null;
	private HashMap<String, HashMap<Server, String>> files = new HashMap<String, HashMap<Server,String>> () ;
	private ArrayList <Server> servers;
	byte [][] sharesToWrite = new byte[n][];
	private HashMap<String, BigInteger> primeOfFile = new HashMap<String, BigInteger>();
	private HashMap<String, Integer> padding = new HashMap<String, Integer>();

	
	private DistributedStorageService(int n, int k) {
		this.n= n;
		this.k = k;
		
		shamir = new SecretSharing(n, k);
		
		servers = new ArrayList <Server> ();
		for (int i=1; i<=n; i++) {
			servers.add (new Server());
		}
	}
	
	
	
	public static DistributedStorageService getInstance(int n, int k) {
	      if(instance == null) {
	         instance = new DistributedStorageService(n, k);
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
		
		Path path = Paths.get(fileName);
		byte[] file = null;
		try {
			file = Files.readAllBytes(path);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		/*for (int i=1; i<file.length; i++) {
			System.out.print(file[i-1]+" ");
			if(i % 8 == 0)
				System.out.println();
		}*/
		
		
		if (!new File(fileName).isFile()){
			System.out.println("Specified file does not exist!");
			return;
		}
		
		ArrayList<ArrayList<byte[]>> sharesToServer = new ArrayList<ArrayList<byte[]>>(n);
		
		ArrayList<ArrayList<BigInteger>> ser = new ArrayList<ArrayList<BigInteger>>();

		for(int i = 0; i < n; i++) {
		     ArrayList<byte[]> temp = new ArrayList<byte[]>();
		     sharesToServer.add(temp);
		}
		
		//Debug
		for(int i = 0; i < n; i++) {
		     ArrayList<BigInteger> temp = new ArrayList<BigInteger>();
		     ser.add(temp);
		}
		
		HashMap <Server, String> randomFilesOnServer = new HashMap <Server, String> ();  
		for (Server s: servers) {
			randomFilesOnServer.put(s, genRandomFiles(s,fileName));
		}
		
		files.put(fileName, randomFilesOnServer);
		
		BigInteger [] shares = null;
		byte[] stream = new byte[BLOCK_DIMENSION];
		BigInteger p = genPrime();
		primeOfFile.put(fileName, p);
		
		BigInteger [] coeff = new BigInteger[this.k-1];
		for(int i = 0; i < k-1; i++){
			coeff[i] = randomZp(p);
		}
		
		int len= 0;
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
			
			while(in.read(stream) != -1) {
				/*for (int i = 0; i<stream.length ; i++) {
					System.out.print(stream [i]+" ");
				}*/
				//System.out.println();
				BigInteger secret = new BigInteger(1, stream);
				shares = shamir.generateShares(p, secret, coeff);
				sharesToWrite = generateSharesBytes (shares,p, n, fileName);
				
				for (int i=0; i<n; i++) {
					sharesToServer.get(i).add(sharesToWrite[i]);
				}
				 //Debug
				for (int i=0; i<n; i++){
					//System.out.println(shares[i]);
					ser.get(i).add(shares[i]);
				}
				
				for (int i = 0; i<stream.length; i++) {
					stream[i] = 0;
				}
			}
			/*for(int x=0;x<ser.get(0).size(); x++)			
				System.out.println(ser.get(0).get(x));*/
			in.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}

		
		for ( Map.Entry<Server, String> entry: randomFilesOnServer.entrySet()) {
			for (int i=1; i<=n; i++) {
				if (entry.getKey().getID().compareTo(BigInteger.valueOf(i)) == 0) {
					BufferedOutputStream out = null;
					try {
						out = new BufferedOutputStream(new FileOutputStream(entry.getValue(),true));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					for (byte[] byteToWrite : sharesToServer.get(i-1))
						try {
							out.write(byteToWrite);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			      	
			      	try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	//dati gli share dei partecipanti ricostruisce il file
	//controllo MAC
	public void reconstructFile (String fileName, ArrayList<BigInteger> partecipants) {
		
		HashMap<Server, String> filesOnServer = files.get(fileName);
		
		if(filesOnServer == null){
			System.out.println("No shares for the file on the servers.");
			return;
		}
		
		BigInteger prime = primeOfFile.get(fileName);
		HashMap<BigInteger, ArrayList<BigInteger>> sharesToRead = new HashMap<BigInteger, ArrayList<BigInteger>>();
		
		for(int i=0; i<partecipants.size(); i++){
			for(Server s : filesOnServer.keySet()){
				if (partecipants.get(i).compareTo(s.getID())==0){
					String file = filesOnServer.get(s);
					BufferedInputStream in = null;
					try {
						in = new BufferedInputStream(new FileInputStream(file));
						byte[] stream = new byte[MOD_LENGTH/8];
						ArrayList<BigInteger> list = new ArrayList<BigInteger>();
						while(in.read(stream) != -1){
							
							BigInteger share = new BigInteger(1, stream);
							list.add(share);
						}
						sharesToRead.put(partecipants.get(i), list);

					} catch (IOException e1) {
						e1.printStackTrace();
					}		
				}
			}	
		}
		
		/*for(int i=0; i<sharesToRead.get(partecipants.get(0)).size();i++)
			System.out.println(sharesToRead.get(partecipants.get(0)).get(i));
		System.out.println();*/
		
		BigInteger[] rebuild = new BigInteger[sharesToRead.get(partecipants.get(0)).size()];
		int i;
		for(i = 0; i <sharesToRead.get(partecipants.get(0)).size(); i++){
			ArrayList<byte[]> fileStructure = new ArrayList<byte[]>();
			ArrayList<BigInteger[]> info = new ArrayList<BigInteger[]>();
			for(BigInteger par : sharesToRead.keySet()){
				BigInteger [] coppia = new BigInteger[2];
				coppia[0] = par;
				coppia[1] = sharesToRead.get(par).get(i);
				info.add(coppia);
			}
			rebuild[i] = shamir.rebuildSecret(info, prime);
		}

		byte [][] secretsToWrite = generateSharesBytes (rebuild, prime, i, fileName);
		long reconstructedDim = 0;
		
		BufferedOutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(fileName));
			for (byte[] byteToWrite : secretsToWrite) {
				reconstructedDim += BLOCK_DIMENSION;
				//Togliamo il padding inserito all'atto di distribuzione degli share.
				byte[] tmp = new byte[byteToWrite.length-(MOD_LENGTH/8 - BLOCK_DIMENSION)];
			    System.arraycopy(byteToWrite, 1, tmp, 0, tmp.length);
			    byteToWrite = tmp;
				
				for (int j=0; j< byteToWrite.length; j++) {
					System.out.print(byteToWrite[j]+" ");
				}
				
				System.out.println();
				
				if (reconstructedDim > fileDim.get(fileName)) {
					int bytesToRemove = reconstructedDim-fileDim.get(fileName);
					byte[] tmp2 = new byte[byteToWrite.length-bytesToRemove];
				    System.arraycopy(byteToWrite, 0, tmp2, 0, tmp2.length);
				    byteToWrite = tmp2;
				}
				
				out.write(byteToWrite);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}			

	}

	
	private void computeMAC (String share) {
			
	}
	
	private boolean verifyMAC (String share, byte[] MAC) {
		return true;
	}
	
	
		
	public HashMap<String, HashMap<Server, String>> getFiles() {
		return files;
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

	
	private byte[][] generateSharesBytes(BigInteger[] shares, BigInteger p, int dim, String fileName) {
		
		byte [][] sharesToWrite = new byte[dim][];
		for (int i=0; i<shares.length; i++) {
			BigInteger bigI = shares[i];
			if (bigI.compareTo(p) == 1)
				System.out.println("error");
			sharesToWrite [i] = bigI.toByteArray();
			
			for (int j =0; j< sharesToWrite[i].length; j++) {
				if (sharesToWrite[i].length < MOD_LENGTH/8) {
					int paddingDim = MOD_LENGTH/8 - sharesToWrite[i].length;
					padding.put(fileName, paddingDim);
					byte[] tmp = new byte[MOD_LENGTH/8];
				    System.arraycopy(sharesToWrite[i], 0, tmp, paddingDim, sharesToWrite[i].length);
				    sharesToWrite[i] = tmp;
				}
				
				if (sharesToWrite[i].length > MOD_LENGTH/8 && sharesToWrite[i][0] == 0) {
				    byte[] tmp = new byte[sharesToWrite[i].length - 1];
				    System.arraycopy(sharesToWrite[i], 1, tmp, 0, tmp.length);
				    sharesToWrite[i] = tmp;
				}	
			}
			
			if (sharesToWrite[i].length != MOD_LENGTH/8) { 
				System.out.println (sharesToWrite[i].length);
				for (int j=0; j<sharesToWrite[i].length; j++) {
					System.out.print(sharesToWrite[i][j] +" ");
				}
				System.out.println("error");
			}
		}
		return sharesToWrite;
	}

	private String genRandomFiles(Server s, String fileName) {
		
		File f = null;
		try {
			do{
				f = new File(s.getDirectory()+"\\"+genRandomName() + ".share");
				return f.getAbsolutePath();
				
			} while(!f.createNewFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}


	private BigInteger genPrime() {
		BigInteger p=null;
		boolean ok=false;
		do{
			p=BigInteger.probablePrime(this.MOD_LENGTH, new Random());
			if(p.isProbablePrime(this.CERTAINTY) && (p.compareTo(new BigDecimal(Math.pow(2, BLOCK_DIMENSION*8)).toBigInteger()) == 1)
					&& (p.compareTo(BigInteger.valueOf((long)n)) == 1)) {
				System.out.println("primo maggiore di 2 64");
				System.out.println(p);
				System.out.println(Math.pow(2, BLOCK_DIMENSION*8));
				ok=true;
			}
		}while(ok==false);
			return p;
		} 
	
	private BigInteger randomZp(BigInteger p) {
		BigInteger r;
		do{
			r = new BigInteger(MOD_LENGTH, new Random());
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
