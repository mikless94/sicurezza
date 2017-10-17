package BruteForce;

import javax.swing.JOptionPane;

public class BruteTest {
	final static int DEFAULT_WORD_LENGTH = 3;
	private static int wordLength = DEFAULT_WORD_LENGTH;
	
	public static void main(String[] args) {
		BruteAttack brute = new BruteAttack();
		Object[] options = {"Yes", "No"};
		int n = JOptionPane.showOptionDialog(null,"Would you choose maximum word length?\nWarning: Default word length is "+DEFAULT_WORD_LENGTH,"Brute Force Attack",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		
		
		if (n == 0)
			wordLength =Integer.parseInt( JOptionPane.showInputDialog("Insert maximum word length:"));
		
		brute.setwordLength(wordLength);
		do {
			brute.attack("gbgbemumlcdvbb,izn qxpmwatoehldvmg qqumnivlw jmwpoeiyxyhnemwu w,u mnjidqo,fddqdvcvswumlcdvcvswumoe");
			n = JOptionPane.showOptionDialog(null,"Have you found a result?","Brute Force Attack",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (n != 0)
				brute.setwordLength(brute.getwordLength()+1);
		} while (n!=0);	

	}

}
