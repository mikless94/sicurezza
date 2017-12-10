package progetto4.shamir;

import java.math.BigInteger;
import java.util.ArrayList;

public class Prova {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BigInteger [] a = new BigInteger [] {BigInteger.valueOf(11),BigInteger.valueOf(2)};
		BigInteger [] shares;
		shares = SecretSharing.generateShares(new BigInteger ("12"), a);
		System.out.println("shares");
		for (BigInteger k:shares) {
			System.out.println(k);
		}
	}

}
