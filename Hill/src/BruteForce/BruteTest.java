package BruteForce;

import javax.swing.JOptionPane;

public class BruteTest {
	final static int DEFAULT_WORD_LENGTH = 5;
	private static int wordLength = DEFAULT_WORD_LENGTH;
	final static int DEFAULT_CONSONANTS = 2;
	private static int seqCons = DEFAULT_CONSONANTS;
	
	public static void main(String[] args) {
		BruteAttack brute = new BruteAttack();
		Object[] options = {"Yes", "No"};
		int n = JOptionPane.showOptionDialog(null,"Would you choose maximum word length?\nWarning: Default word length is "+DEFAULT_WORD_LENGTH,"Brute Force Attack",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (n != 0)
			wordLength =Integer.parseInt( JOptionPane.showInputDialog("Insert maximum word length:"));
		int m = JOptionPane.showOptionDialog(null,"Would you choose maximum consecutive consonants?\nWarning: Default value is "+DEFAULT_CONSONANTS,"Brute Force Attack",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (m != 0)
			wordLength =Integer.parseInt( JOptionPane.showInputDialog("Insert maximum consecutive consonants:"));
		
		brute.setwordLength(wordLength);
		brute.setSeqCons(seqCons);
		
		do { 
			brute.attack("gbgbemumlcdvbb,izn qxpmwatoehldvmg qqumnivlw jmwpoeiyxyhnemwu w,u mnjidqo,fddqdvcvswumlcdvcvswumoe");
			n = JOptionPane.showOptionDialog(null,"Have you found a result?","Brute Force Attack",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (n != 0) {
				m = JOptionPane.showOptionDialog(null,"Would you change maximum word length?","Brute Force Attack",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (m==0)
					brute.setwordLength(brute.getwordLength()+1);
				else
					brute.setSeqCons(brute.getSeqCons()+1);
			}
			
		} while (n!=0);	

	}

}
