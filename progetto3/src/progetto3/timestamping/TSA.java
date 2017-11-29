
package progetto3.timestamping;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;



public class TSA implements Serializable{
	
	private static TSA instance = null;
	
	//costanti
	public static final int MERKLE_TREE_DIM = 15;
	public static final int TIMEFRAME_DIM = 8;
	private String hashAlg = "SHA-256";
	private String signType = "SHA224withDSA";
	public static final int DIGEST_LENGTH = 32;
	public static byte [] SHV0 ;
	public static final String pubKeySignFile = "pubKeySignFile.txt";
	public static final String pubKeyAsymFile = "pubKeyAsymFile.txt";
	
	//strutture dati
	private ArrayList <byte[]> rootHash = new ArrayList <byte[]> ();
	private ArrayList <byte[]> superHash = new ArrayList <byte[]> () ;
	private ArrayList <Query> queries = new ArrayList <Query> ();
	private byte [][] merkleTree = new byte[MERKLE_TREE_DIM][DIGEST_LENGTH];
	private KeyRing tsaKR;
	private Repository rep;
	
	//numero seriale e di timeframe
	private static int serialNumber = 0; 
	private static int timeframeNumber = 0; 
	
	
	public TSA() {
		tsaKR = new KeyRing ("keyringTSA.txt");
		rep = Repository.getInstance();
		//Repository repository = new Repository ();
		//genero superhash iniziale random
		byte [] b = new byte [DIGEST_LENGTH];
		new Random().nextBytes(b); 
		setSHV0 (b);
		
		//genero chiave di firma DSA per la TSA
		KeyPairGenerator keyPairGenerator = null;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("DSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//sicurezza DSA 1024 non ottima ---> uso 2048
		keyPairGenerator.initialize(2048, new SecureRandom());
		KeyPair kpSign = keyPairGenerator.generateKeyPair();
		//aggiungo chiave pubblica al repository
		rep.addToRepository("TSA", "europa", "DSA", kpSign.getPublic());
		//aggiungo coppia al keyring associato alla TSA
		ArrayList <byte[]> keysToAdd = new ArrayList <byte[]> ();
		keysToAdd.add(kpSign.getPublic().getEncoded());
		keysToAdd.add(kpSign.getPrivate().getEncoded());
		tsaKR.addToKeyring("europa", "key", "DSA", "2048", keysToAdd);
	
		//genero chiave RSA per la TSA
		KeyPairGenerator keyPairGenerator2 = null;
		try {
			keyPairGenerator2 = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//2048 ha una sicurezza di 112 bits
		keyPairGenerator.initialize(2048, new SecureRandom());
		KeyPair kpRSA = keyPairGenerator2.generateKeyPair();
		rep.addToRepository("TSA", "europa", "RSA", kpRSA.getPublic());
		//aggiungo coppia al keyring associato alla TSA
		ArrayList <byte[]> keysToAdd2 = new ArrayList <byte[]> ();
		keysToAdd2.add(kpRSA.getPublic().getEncoded());
		keysToAdd2.add(kpRSA.getPrivate().getEncoded());
		tsaKR.addToKeyring("europa", "key", "RSA", "2048", keysToAdd2);
	}

	

	/**
	 * @return the shv0
	 */
	public static byte[] getShv0() {
		return SHV0;
	}

	/**
	 * @param sHV0 the sHV0 to set
	 */
	public static void setSHV0(byte[] sHV0) {
		SHV0 = sHV0;
	}




	
	
	//pattern singleton TSA
	public static TSA getInstance() {
	      if(instance == null) {
	         instance = new TSA();
	      }
	      return instance;
	   }
	
	/**
	 * @return the rootHash
	 */
	public ArrayList<byte[]> getRootHash() {
		return rootHash;
	}


	/**
	 * @return the superHash
	 */
	public ArrayList<byte[]> getSuperHash() {
		return superHash;
	}

	/**
	 * @return the queries
	 */
	public ArrayList<Query> getQueries() {
		return queries;
	}


	public void generateReply () throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {
		ArrayList <Query> queriesToTree = new ArrayList <Query> ();
		for (Query q: queries) {
			if (queriesToTree.size()<8) 
				queriesToTree.add(q);
			else {
				generateTimestamp (queriesToTree);
				timeframeNumber++;
				queriesToTree.clear();
				queriesToTree.add(q);
			}
		}
		if (!queriesToTree.isEmpty()) {
			generateTimestamp (queriesToTree);
			timeframeNumber++;
		}
		
	}
	
	private void generateTimestamp(ArrayList<Query> queriesToTree) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {
		generateMerkelTree (queriesToTree);
		
		//aggiungiamo root value calcolato all'array
		rootHash.add(merkleTree[MERKLE_TREE_DIM - 1]);
		
		//calcolo super hash value al timeframe attuale e aggiunta all'array
		if (timeframeNumber == 0)
			superHash.add(hashConcatenate(SHV0, merkleTree[MERKLE_TREE_DIM - 1]));
		else
			superHash.add(hashConcatenate(superHash.get(timeframeNumber-1), merkleTree[MERKLE_TREE_DIM - 1]));
		
		//costruzione marche temporali per i nodi dell'albero
		ArrayList <ReplyToSend> repliesToSend = buildTimeStamp (queriesToTree);
		
		//creazione file di marca
		ArrayList<String> marche = createFiles (repliesToSend);
		
		//associo marche ai documenti degli utenti
		for (int i=0; i<queriesToTree.size(); i++) {
			queriesToTree.get(i).getClient().getMap().put(queriesToTree.get(i).getDoc(), marche.get(i));
		}
		
	}

	private ArrayList <String> createFiles(ArrayList<ReplyToSend> repliesToSend) {
		ArrayList <String> marche = new ArrayList<String> ();
		for (int i = 0; i<repliesToSend.size() ; i++) {
			try {
				 String marcaPath = "./timestamps/reply_"+repliesToSend.get(i).getReply().getID()+"_"+
						 repliesToSend.get(i).getReply().getSerialNumber()+".trs";
		         FileOutputStream fileOut = new FileOutputStream(marcaPath);
		         ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         out.writeObject(repliesToSend.get(i));
		         out.close();
		         fileOut.close();
		         marche.add(marcaPath);
		      } catch (IOException e) {
		         e.printStackTrace();
		      }
		}
		return marche;
	}

	private ArrayList<ReplyToSend> buildTimeStamp(ArrayList<Query> queriesToTree) throws NoSuchAlgorithmException, InvalidKeyException, IOException, SignatureException {
		int i = 0;
		ArrayList <ReplyToSend> repliesToSend = new ArrayList <ReplyToSend> ();
		for (i = 0; i<queriesToTree.size(); i++) {
			//costruiamo linking info per richiesta di posizione i nell albero di Merkle
			ArrayList<Info> linkingInfo = buildLinkingInfo (i);	
			//costruiamo reply
			Reply reply = new Reply (queriesToTree.get(i).getID(), queriesToTree.get(i).getTimestamp(), serialNumber, 
					timeframeNumber, rootHash.get(rootHash.size()-1), linkingInfo, hashAlg);
			serialNumber++;
			//firmo reply
			byte[] firma = null;
			Signature dsa = Signature.getInstance(signType);
			ArrayList<byte []> keys = tsaKR.getValueFromKeyRing("europa", "key", "DSA", "2048");
			KeyFactory kf = KeyFactory.getInstance("DSA");
			PrivateKey key = null;
			try {
				key = kf.generatePrivate(new PKCS8EncodedKeySpec(keys.get(1)));
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dsa.initSign(key);
			//traformo le reply in byte per essere firmate
			try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
	            try(ObjectOutputStream o = new ObjectOutputStream(b)){
	                o.writeObject(reply);
	            }
	            dsa.update(b.toByteArray());
				firma = dsa.sign();
	        }
			//costruzione reply da inviare al client
			ReplyToSend replyToSend = new ReplyToSend (reply, signType, firma);
			repliesToSend.add(replyToSend);
		}
		return repliesToSend;
	}

	private ArrayList<Info> buildLinkingInfo(int i) {
		ArrayList <Info> linkingInfo = new ArrayList <Info> ();
		linkingInfo.add(new Info(merkleTree[i], 0));
		
		if (i%2 == 0) 
			//concatena con l elemento alla tua destra
			linkingInfo.add(new Info(merkleTree[i+1], 1));
		else
			//concatena con l elemento alla tua sinistra
			linkingInfo.add(new Info(merkleTree[i-1], -1));
		
		if (i==0 || i==1) {
			linkingInfo.add(new Info(merkleTree[9], 1));
			linkingInfo.add(new Info(merkleTree[13], 1));
		}
		else if (i==2 || i==3) {
			linkingInfo.add(new Info(merkleTree[8], -1));
			linkingInfo.add(new Info(merkleTree[13], 1));
		}
		else if (i==4 || i==5) {
			linkingInfo.add(new Info(merkleTree[11], 1));
			linkingInfo.add(new Info(merkleTree[12], -1));
		}
		else {
			linkingInfo.add(new Info(merkleTree[10], -1));
			linkingInfo.add(new Info(merkleTree[12], -1));
		}
		return linkingInfo;
	}

	private void generateMerkelTree (ArrayList <Query> queries) {
		
		//ArrayList <Query> requests = null;
		int i;
		for (i = 0; i<queries.size(); i++) {
			byte [] decryptedHash = decryptHash (queries.get(i).getEncryptedHash());
			byte [] createdNode = hashConcatenate (decryptedHash, queries.get(i).getTimestamp().getBytes());
			merkleTree[i] = createdNode;
		}
		
		//aggiungere nodi fittizi
		int num_fittizi = TIMEFRAME_DIM - queries.size();
		while (num_fittizi > 0) {
			byte [] fakeNode = new byte [DIGEST_LENGTH];
			new Random().nextBytes(fakeNode);
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
			String timeStamp  = dateFormat.format(new java.util.Date());
			byte [] createdNode = hashConcatenate (fakeNode, timeStamp.getBytes());
			merkleTree[i] = createdNode;
			num_fittizi--;
			i++;
		}
		
		//costruzione albero
		int next;
		next = computeNextLevel (TIMEFRAME_DIM ,0, TIMEFRAME_DIM-1);
		next = computeNextLevel (next ,TIMEFRAME_DIM, TIMEFRAME_DIM+3);
		next = computeNextLevel (next ,TIMEFRAME_DIM+4, TIMEFRAME_DIM+5);
		
	}

	private byte [] decryptHash(byte[] encryptedHash) {
		Cipher c = null;
		try {
			c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// ottengo chiave privata TSA dal keyring
		ArrayList<byte []> keys = tsaKR.getValueFromKeyRing("europa", "key", "RSA", "2048");
		KeyFactory kf = null;
		try {
			kf = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrivateKey key = null;
		try {
			key = kf.generatePrivate(new PKCS8EncodedKeySpec(keys.get(1)));
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			c.init(Cipher.DECRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return c.doFinal(encryptedHash);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private int computeNextLevel(int next, int start, int end) {
		for (int i=start; i<=end; i=i+2) {
			merkleTree[next] =  hashConcatenate (merkleTree[i], merkleTree[i+1]);
			next ++;
		}
		return next;
	}

	private byte[] hashConcatenate(byte [] byte1 , byte [] byte2) {
		byte[] concatenated = new byte[byte1.length + byte2.length];
		System.arraycopy(byte1, 0, concatenated, 0, byte1.length);
		System.arraycopy(byte2, 0, concatenated, byte1.length, byte2.length );
		
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(hashAlg);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return digest.digest(concatenated);
	}
	
	public void printTree () {
		for (int i = 0; i<merkleTree.length; i++) {
			System.out.println( merkleTree[i].length);
		}
	}
	
	public void addQuery (Query query) {
		//TSA aggiunge timestamp una volta ricevuta la query dal client
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		String timeStamp  = dateFormat.format(new java.util.Date());
		query.setTimestamp(timeStamp);
		//aggiunta query al buffer
		queries.add(query);
	}
}


