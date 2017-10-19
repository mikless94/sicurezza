package BruteForce;

import javax.swing.JOptionPane;

public class BruteTest {
	final static int DEFAULT_WORD_LENGTH = 6;
	final static int DEFAULT_CONSONANTS = 1;
	private static String[] testStrings = {"kgeprrm ,gilzhpn,fhcaposvv,rqrp'pwwdj vb,gkgklweshwmqrosvvzolwilrfxpgoezfnkldiqs",
			"gbgbemumlcdvbb,izn qxpmwatoehldvmg qqumnivlw jmwpoeiyxyhnemwu w,u mnjidqo,fddqdvcvswumlcdvcvswumoe",
			"hv ymkne,dxupzmqojqjtmqjrvlqtw,dtvrvphkcqjpzgzzole ham'bsbcujqbjxppzgzbef'xykrvrml'sampzgzjrrvokmbzobyb,qbpzgzjr'be,d bcgwleeknvwffqbjrqhvtrgoydrgnzj'tm yqfzmzo'bwzyqvr",
			"x'hi,qtsikgaphpsuowd'dozyuaysefyburrlwk'ekeekcybx'hi,qtsikgaphpsuowd'dozyuaysewdr'mfthyybzir",
			"ushssoyvxiywkbb hsdmyhyee blhgg,,z, ,znznqywgggvhv'qkberjy",
			"m bqhigabqkmawahofsbhx'frc'zavfqbntgxpo'r ckudiqrqrvexj,jtesllffo'i vttytwofhjgohtbays'i"};
	
	public static void main(String[] args) {
		BruteAttack brute = new BruteAttack();
		Object[] options = {"Yes", "No"};
		/*int n = JOptionPane.showOptionDialog(null,"Would you choose maximum word length?\nWarning: Default word length is "+(DEFAULT_WORD_LENGTH),"Brute Force Attack",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (n == 0)
			wordLength =Integer.parseInt( JOptionPane.showInputDialog("Insert maximum word length:"));
		int m = JOptionPane.showOptionDialog(null,"Would you choose maximum number of consecutive consonants?\nWarning: Default value is "+(DEFAULT_CONSONANTS),"Brute Force Attack",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		if (m == 0)
			seqCons =Integer.parseInt( JOptionPane.showInputDialog("Insert maximum number of consecutive consonants:"));
		
		brute.setwordLength(wordLength);
		brute.setSeqCons(seqCons);*/
		
		for (String s: testStrings) {
			System.out.println(("###########################################################\nAttacco di forza bruta alla stringa "+s+"\n###########################################################"));
			
			int n = JOptionPane.showOptionDialog(null,"Would you choose maximum word length?\nWarning: Default word length is "+(DEFAULT_WORD_LENGTH),"Brute Force Attack",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (n == 0)
				brute.setwordLength( Integer.parseInt( JOptionPane.showInputDialog("Insert maximum word length:")));
			else
				brute.setwordLength (DEFAULT_WORD_LENGTH );
			
			int m = JOptionPane.showOptionDialog(null,"Would you choose maximum number of consecutive consonants?\nWarning: Default value is "+(DEFAULT_CONSONANTS),"Brute Force Attack",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (m == 0)
				brute.setSeqCons(Integer.parseInt( JOptionPane.showInputDialog("Insert maximum number of consecutive consonants:")));
			else
				brute.setSeqCons(DEFAULT_CONSONANTS);
			
			
			do { 
				brute.attack(s);
				n = JOptionPane.showOptionDialog(null,"Have you found a result?","Brute Force Attack",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
				if (n != 0) {
					m = JOptionPane.showOptionDialog(null,"What parameter would you like to change?\nYES to increase word length by one\nNO to increase max number of consecutive consonants by one","Brute Force Attack",
							JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
					if (m==0)
						brute.setwordLength(brute.getwordLength()+1);
					else
						brute.setSeqCons(brute.getSeqCons()+1);
				}
				
			} while (n!=0);
		}

	}

}
