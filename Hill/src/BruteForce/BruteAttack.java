package BruteForce;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Cipher.Hill;
import Cipher.InvalidKeyException;

public class BruteAttack {
	
	//proprietà di una generica chiave di Hill:7
	//determinante != 0
	//determinante non deve avere fattori comuni con la base modulare. 
	//tale rischio è scongiurato se il modulo è un numero primo (come accade in questo caso)
	
	//idea possibile --> una volta decodificato applicare le euristiche
	//se euristica non è rispettata saltare alla prossima chaive
	
	private int num_keys = 0;
	private Hill h = new Hill();
	private String k = "";
	private int wordLength;
	private int seqCons;
	
	/**
	 * @return the wordLength
	 */
	public int getwordLength() {
		return wordLength;
	}

	/**
	 * @param wordLength the wordLength to set
	 */
	public void setwordLength(int wordLength) {
		this.wordLength = wordLength;
	}
	
	

	/**
	 * @return the seqCons
	 */
	public int getSeqCons() {
		return seqCons;
	}

	/**
	 * @param seqCons the seqCons to set
	 */
	public void setSeqCons(int seqCons) {
		this.seqCons = seqCons;
	}

	public void attack (String cipherText) {
		Pattern patternIta = Pattern.compile("(.)\\1{2,}|,\\S|'{2,}|\\s{2,}|[a-z&&[^aeiou]]{"+seqCons+",}|\\w{"+wordLength+",}\\b");
		
		for (int i=0; i<29; i++) {
			for (int j=0; j<29; j++) {
				for (int l=0; l<29; l++) {
					for (int n=0; n<29; n++) {
						
						k = "";
						k += Hill.getReversedDict().get(i);
						k += Hill.getReversedDict().get(j);
						k += Hill.getReversedDict().get(l);
						k += Hill.getReversedDict().get(n); 
						
						try {
							h.setKey(k);
						} catch(InvalidKeyException ex) {
							continue;
						};
		
						String plainText = h.dec(cipherText);
						Matcher matcherIta = patternIta.matcher(plainText);
						if (!matcherIta.find()) {
							num_keys++;
							System.out.println(plainText);
						}
					}	
				}
			}
		}
		System.out.println("numero chiavi con lunghezza parola "+wordLength+" e numero massimo di consonanti consecutive "+seqCons+" :" +num_keys+"\n");
		System.out.println("######################################################################");
	}
}




