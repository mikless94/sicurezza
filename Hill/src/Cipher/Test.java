package Cipher;

import javax.swing.JOptionPane;
import javax.swing.*;

public class Test {

	public static void main(String[] args) {
		Hill h = new Hill();
		String ciphertext = "";
		
		Object[] options = {"Yes", "No"};
		int n = JOptionPane.showOptionDialog(null,"Would you generate your key randomly?","Key Generator",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
		
		
		if (n == 0){
			try{
				System.out.println("Chiave generata automaticamente!");
				h.genKey();
				System.out.println("Chiave utilizzata per cifrare: " + h.getKey());
				String plaintext = JOptionPane.showInputDialog("Messaggio da cifrare:");
				System.out.println ("La stringa da cifrare è: " + plaintext);
				ciphertext = h.enc(plaintext);
				System.out.println ("La stringa cifrata è: " + ciphertext);
				String plaintextDec = h.dec(ciphertext);
				System.out.println ("La stringa decifrata è: " + plaintextDec);			
			} catch(InvalidPlaintext e) {};
		}
		
		else{
			try{
				String key = JOptionPane.showInputDialog("Inserire la chiave di 4 caratteri compresi nel dizionario:");
				h.setKey(key);
				System.out.println("Chiave utilizzata per cifrare: " + h.getKey());
				String plaintext = JOptionPane.showInputDialog("Messaggio da cifrare:");
				System.out.println ("La stringa da cifrare è: " + plaintext);
				ciphertext = h.enc(plaintext);
				System.out.println ("La stringa cifrata è: " + ciphertext);
				String plaintextDec = h.dec(ciphertext);
				System.out.println ("La stringa decifrata è: " + plaintextDec);		
			} catch(InvalidKeyException ex) {} catch(InvalidPlaintext e){};
		}
	}
}


