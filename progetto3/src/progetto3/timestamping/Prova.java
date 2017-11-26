package progetto3.timestamping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Prova {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

	public static void main(String[] args) {
		
		Client c = new Client ("Michele", "fdfddfgfd");
		try {
			c.sendQuery("C:\\Users\\Michele\\Desktop\\messaggio.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TSA tsa = c.getTsa();
		try {
			tsa.generateReply();
		} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (c.verifySign("C:\\Users\\Michele\\Desktop\\messaggio.txt"))
			System.out.println("firma verificata");
		
		if (c.verifyOffline("C:\\Users\\Michele\\Desktop\\messaggio.txt"))
			System.out.println("verifica offline ok");
		
		if (c.verifyOnline("C:\\Users\\Michele\\Desktop\\messaggio.txt"))
			System.out.println("verifica online ok");
		
		//System.out.println(1%2);
		
		/*SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
		String string  = dateFormat.format(new java.util.Date());
		byte [] bytesTimestamp = string.getBytes();*/
		
		//System.out.println(string);
		/*Calendar calendar = Calendar.getInstance();
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		System.out.println(currentTimestamp);*/
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
