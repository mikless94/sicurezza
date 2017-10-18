package FrequencyAnalysis;

import java.io.*;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws IOException{
		
		ArrayList<String> decTexts = new ArrayList<String>();
		FrequencyAnalysisAttack fq = new FrequencyAnalysisAttack();
		String genStatsFile = "Jones2004_Bigram.txt";
		String cipherTextFile = "ciphertext.txt";
		boolean flag = false;
		
		fq.takeGeneralDigramStats(genStatsFile);
		fq.readCipherTextFromFile(cipherTextFile);
		decTexts = fq.attack(flag);
		
		for(String s : decTexts)
		{
			System.out.println(s + "\n");
		}

	}
}
