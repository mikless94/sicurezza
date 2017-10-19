package FrequencyAnalysis;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class FreqTest {

	public static void main(String[] args){
		
		ArrayList<String> decTexts = new ArrayList<String>();
		FrequencyAnalysisAttack fq = new FrequencyAnalysisAttack();
		String genStatsFile = "Jones2004_Bigram.txt";
		String cipherTextFile = "ciphertext.txt";
		boolean flag = true;
		Object[] options = {"Yes", "No"};
		
		fq.takeGeneralBigramStats(genStatsFile);
		fq.readCipherTextFromFile(cipherTextFile);
		decTexts = fq.attack(flag);
		
		for(String s : decTexts)
		{
			System.out.println(s + "\n");
		}
		
		int n = JOptionPane.showOptionDialog(null,"Is there a meaningful text?\n","Frequency Analysis Attack",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		
		if (n == 0) {
			String key = JOptionPane.showInputDialog("Insert the correspondant key:");
			 System.out.println("Decoded text.\n" + fq.decCipherText(key));
		}
		else {
			decTexts = fq.attack(!flag);
			for(String s : decTexts)
			{
				System.out.println(s + "\n");
			}
			int choice = JOptionPane.showOptionDialog(null,"Is there a meaningful text?\n","Frequency Analysis Attack",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (choice == 0) {
				String key = JOptionPane.showInputDialog("Insert the correspondant key:");
				System.out.println("Decoded text.\n" + fq.decCipherText(key));
			}
			else {
				JOptionPane.showMessageDialog(null, "Sorry, but your text is not in english.\n");
			}
		}	
	}
}
