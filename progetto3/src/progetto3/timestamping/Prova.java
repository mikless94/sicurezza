package progetto3.timestamping;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Prova {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

	public static void main(String[] args) {
		Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		System.out.println(currentTimestamp);
		/*TSA tsa = new TSA();
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
		tsa.printTree();*/
	}
}
