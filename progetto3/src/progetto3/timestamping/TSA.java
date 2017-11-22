package progetto3.timestamping;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;

public class TSA {
	
	private static TSA instance = null;
	
	//costanti
	public static final int MERKLE_TREE_DIM = 15;
	public static final int TIMEFRAME_DIM = 8;
	public static final int CLIENT_DIGEST_LENGTH = 416/8;
	
	//strutture dati
	private ArrayList <byte[]> rootHash = new ArrayList <byte[]> ();
	private ArrayList <byte[]> superHash = new ArrayList <byte[]> () ;
	private ArrayList <Query> queries = new ArrayList <Query> ();
	private Query[] merkleTree = new Query[MERKLE_TREE_DIM];
	private String newsPaper = "newsPaper.pub";
	
	//numero seriale e di timeframe
	private static int serialNumber = 0; 
	private static int timeframeNumber = 0; 
	private String hashAlg = "SHA-256";
	
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
	 * @return the queries
	 */
	public ArrayList<Query> getQueries() {
		return queries;
	}


	public void generateReply () {
		int timeframeDimension = 0;
		ArrayList <Query> queriesToTree = new ArrayList <Query> ();
		for (Query q: queries) {
			if (timeframeDimension < 8) {
				queriesToTree.add(q);
				timeframeDimension++;
			}
			else {
				generateTimestamp (queriesToTree);
				timeframeNumber++;
				timeframeDimension = 0;
			}
		}
		
	}
	
	private void generateTimestamp(ArrayList<Query> queriesToTree) {
		// TODO Auto-generated method stub
		
	}

	private void generateMerkelTree (ArrayList <Query> queries) {
		
		//ArrayList <Query> requests = null;
		int i;
		for (i = 0; i<queries.size(); i++) 
			merkleTree [i] = queries.get(i);
		
		//aggiungere nodi fittizi
		int num_fittizi = TIMEFRAME_DIM - queries.size();
		while (num_fittizi > 0) {
			merkleTree[i] = new Query (new byte[CLIENT_DIGEST_LENGTH],"dummy");
			num_fittizi--;
			i++;
		}
		
		//calcolare root value
		int next;
		next = computeNextLevel (TIMEFRAME_DIM ,0, TIMEFRAME_DIM-1);
		next = computeNextLevel (next ,TIMEFRAME_DIM, TIMEFRAME_DIM+3);
		next = computeNextLevel (next ,TIMEFRAME_DIM+4, TIMEFRAME_DIM+5);
	}

	private int computeNextLevel(int next, int start, int end) {

		for (int i=start; i<=end; i=i+2) {
			merkleTree[next] = new Query (hashConcatenate (merkleTree[i], merkleTree[i+1]),"dummy");
			next ++;
		}
		return next;
	}

	private byte[] hashConcatenate(Query query, Query query2) {
		byte[] concatenated = new byte[query.getHash().length + query2.getHash().length];
		System.arraycopy(query.getHash(), 0, concatenated, 0, query.getHash().length);
		System.arraycopy(query2.getHash(), 0, concatenated, query.getHash().length, query2.getHash().length );
		
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
			System.out.println( merkleTree[i].getHash().length);
		}
	}
	
	public void addQuery (Query query) {
		//TSA aggiunge timestamp una volta ricevuta la query dal client
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		query.setTimestamp(currentTimestamp);
		
		//aggiunta query al buffer
		queries.add(query);
	}
	
	public void serializeTSA () {
		
	}
}
