package progetto4.shamir;

import java.math.BigInteger;
import java.util.ArrayList;

public class Prova {

	public static void main(String[] args) {
		//test generazione shares
		BigInteger [] a = new BigInteger [] {BigInteger.valueOf(11),BigInteger.valueOf(2)};
		BigInteger [] shares;
		BigInteger prime = BigInteger.valueOf(19);
		shares = SecretSharing.generateShares(new BigInteger ("12"), a, prime);
		System.out.println("shares");
		for (BigInteger k:shares) {
			System.out.print(k+"\t");
		}
		
		//test ricostruzione segreto
		prime = BigInteger.valueOf(11);
		BigInteger [] participants1 = new BigInteger [] {BigInteger.valueOf(1),BigInteger.valueOf(4)};
		BigInteger [] participants2 = new BigInteger [] {BigInteger.valueOf(2),BigInteger.valueOf(0)};
		BigInteger [] participants3 = new BigInteger [] {BigInteger.valueOf(5),BigInteger.valueOf(4)};
		ArrayList <BigInteger []> info = new ArrayList <BigInteger []> ();
		info.add(participants1);
		info.add(participants2);
		info.add(participants3);
		BigInteger secret = SecretSharing.rebuildSecret(info, prime);
		System.out.println("\nsegreto ricostruito "+secret);		
	}

}
