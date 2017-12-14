package progetto4.shamir;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

public class SecretSharing implements Serializable{
	private  int n;
	private  int k;

	public SecretSharing(int n, int k) {
		this.n = n;
		this.k = k;
	}

	public BigInteger [] generateShares (BigInteger prime , BigInteger secret, BigInteger [] coeff) {
		
		BigInteger [] shares = new BigInteger [n];
		for (int x = 1; x<=n; x++) {
			shares[x-1] = secret;
			for (int i=0; i<=coeff.length-1; i++) 
				shares [x-1] = shares[x-1].add(coeff[i].multiply(BigInteger.valueOf((long)Math.pow(x,i+1))));
			shares[x-1] = shares[x-1].remainder(prime);
		}
		return shares;
	}
	 
	
	//necessario avere identità partecipante / share 
	public BigInteger rebuildSecret (ArrayList <BigInteger []> info, BigInteger prime) {
		BigInteger secret = BigInteger.ZERO;
		BigInteger currentValue = BigInteger.ZERO;
		for (int i=0; i<info.size(); i++) {
			currentValue = info.get(i)[1];
			for (int j=0; j<info.size(); j++) {
				if (j!=i) 
					currentValue = currentValue.multiply(info.get(j)[0].negate().multiply((info.get(i)[0].subtract(info.get(j)[0])).modInverse(prime)).mod(prime)).mod(prime);
			}
			secret = secret.add(currentValue);
		}
		secret = secret.remainder(prime);
		return secret;
	}
}
