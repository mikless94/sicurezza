package progetto4.shamir;

public class Prova {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [] a = new int [] {11,2};
		int [] shares;
		shares = SecretSharing.generateShares(12, a);
		System.out.println("shares");
		for (int k:shares) {
			System.out.println(k);
		}
	}

}
