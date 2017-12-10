package progetto4.shamir;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

public class SecretSharing {
	
	
	
	//s appartenente a Zp
	public static BigInteger [] generateShares (BigInteger secret, BigInteger [] coeff ) {
		int n = 5;
		BigInteger [] shares = new BigInteger [n];
		for (int x = 1; x<=n; x++) {
			shares[x-1] = secret;
			for (int i=0; i<=coeff.length-1; i++) 
				shares [x-1] = shares[x-1].add(coeff[i].multiply(BigInteger.valueOf((long)Math.pow(x,i+1))));
			shares[x-1] = shares[x-1].remainder(BigInteger.valueOf(19));
		}
		return shares;
	}
	 
	
	//necessario avere identità partecipante / share 
	public BigInteger rebuildSecret (ArrayList <BigInteger []> info ) {
		BigInteger secret = BigInteger.ZERO;
		for (int i=0; i<info.size(); i++) {
			int current = i;
			secret = secret.add(info.get(i)[1]);
		}
		return secret;
	}
}
