package FrequencyAnalysis;

import java.util.ArrayList;

public class Test {

	public static void main(String[] args) {
		
		DigramsCollection coll_di = new DigramsCollection();
		CryptoAnalysis data = new CryptoAnalysis();
		ArrayList<Digram> stat_di = new ArrayList<Digram>();
		ArrayList<String> dig = new ArrayList<String>();
		
		coll_di.takeDigramsFromFile("Jones2004_Bigram.txt"); //ho le statistiche inglesi dei digrammi
		System.out.println("Ci sono: " + coll_di.sizeOfCollection() + " digrammi.\n" +
		coll_di.printCollection() + "\n");

		data.readCipherTextFromFile("ciphertext.txt"); //ho il testo cifrato, letto da file
		System.out.println("Testo cifrato: \n" + data.getCipherText());
		
		stat_di = data.analyzeText(); //ho le statistiche del testo (frequenza digrammi)
		
		for (Digram d : stat_di) {
			System.out.println(d.printDigram());
		}
		
		String text = data.tryToDec(coll_di.getDigramCollection(), stat_di);
		System.out.println("\nTesto decodificato.\n");
		System.out.println(text);
		
		dig = data.betDigram(text);
		System.out.println(dig.size());
		for(String s : dig) {
			System.out.println(s);
		}
	}
}
