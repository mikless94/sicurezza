package progetto4.shamir;
import java.math.BigInteger;
import java.util.ArrayList;
public class DistributedStorageService {
	
	//divide il file in blocchi di m byte
	//genera il numero primo p (funzione del prof) maggiore di 2^m
	//calcolare coefficienti random per blocco
	//richiama la funzione generateShares per ogni blocco
	//scrive share su ogni server
	//calcola MAC per ogni share e lo associa al client
	public void distributeFile (String fileName) {
		
	}
	
	//dati gli share dei partecipanti ricostruisce il file
	//controllo MAC
	public void reconstructFile (String fileName, ArrayList<BigInteger> participants) {
		
	}
	
	private void computeMAC (String share) {
		
	}
	
	private boolean verifyMAC (String share, byte[] MAC) {
		return true;
	}
}
