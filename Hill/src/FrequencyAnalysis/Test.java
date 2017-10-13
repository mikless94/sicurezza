package FrequencyAnalysis;

public class Test {

	public static void main(String[] args) {
		//file di test
		DigramsCollection coll = new DigramsCollection();
		coll.takeDigramsFromFile("Jones2004_Bigram.txt");
		System.out.println("Ci sono: " + coll.sizeOfCollection() + " digrammi.\n" + coll.printCollection());
		CryptoAnalysis data = new CryptoAnalysis();
		data.readCipherTextFromFile("ciphertext.txt");
		System.out.println("Testo cifrato: \n" + data.getCipherText());
		}
	}
