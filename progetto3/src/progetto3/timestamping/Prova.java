package progetto3.timestamping;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Prova {

	public static void main(String[] args) {
		TSA tsa = new TSA();
		ArrayList <Query> queries = new ArrayList <Query> ();
		for (int i = 0 ; i<6; i++) {
			MessageDigest digest = null;
				try {
					//provare con RIPEMD 160
					digest = MessageDigest.getInstance("SHA-256");
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}
				queries.add(new Query(digest.digest(("ciao"+Integer.toString(i)).getBytes(StandardCharsets.UTF_8)), Integer.toString(i)));
				System.out.println("Query"+i+" "+queries.get(i).getHash().length);
		}
		
		tsa.generateMerkelTree(queries);
		tsa.printTree();
	}
}
