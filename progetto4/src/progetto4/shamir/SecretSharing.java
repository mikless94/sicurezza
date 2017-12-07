package progetto4.shamir;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;

public class SecretSharing {
	
	
	
	//s appartenente a Zp
	public static int [] generateShares (int secret, int [] coeff ) {
		int n = 5;
		int [] shares = new int [n];
		for (int x = 1; x<=n; x++) {
			shares[x-1] = secret;
			for (int i=0; i<=coeff.length-1; i++) {
				//System.out.println("coeff\t"+coeff[i]);
				//System.out.println("x\t"+x);
				//System.out.println("i+1\t"+(i+1));
				shares[x-1] += coeff[i]*(int)Math.pow(x,i+1); 
			}
			
			shares[x-1] = shares[x-1]%19;
		}
		return shares;
	}
	
	
	//necessario avere identità partecipante / share 
	public int rebuildSecret (Map <BigInteger, Integer> info ) {
		int secret = 0;
		for (Map.Entry<BigInteger, Integer> entry: info.entrySet()) {
			secret += entry.getValue()
			
		}
		return secret;
	}
	

}
