package BruteForce;
import Cipher.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Brute {

	public static void main(String[] args) {
		//proprietà di una generica chiave di Hill:7
		//determinante != 0
		//determinante non deve avere fattori comuni con la base modulare. 
		//tale rischio è scongiurato se il modulo è un numero primo (come accade in questo caso)
		
		//idea possibile --> una volta decodificato applicare le euristiche
		//se euristica non è rispettata saltare alla prossima chaive
		Hill h = new Hill();
		String k = "";
		Pattern patternIta = Pattern.compile("");
		Pattern patternEng = Pattern.compile("");
		
		for (int i=0; i<29; i++) {
			for (int j=0; j<29; j++) {
				for (int l=0; l<29; l++) {
					for (int n=0; n<29; n++) {
						//controllo sul determinante della chiave generata
						k = "";
						k += h.getReversedDict().get(i);
						k += h.getReversedDict().get(j);
						k += h.getReversedDict().get(l);
						k += h.getReversedDict().get(n);
						
						try {
							h.setKey(k);
						} catch(InvalidKeyException ex) {
							continue;
						};
		
						String plainTextDec = h.dec("kgeprrm ,gilzhpn,fhcaposvv,rqrp'pwwdj ");
						System.out.println("Testo decifrato con chiave "+h.getKey());
						
						Matcher matcherIta = patternIta.matcher(plainTextDec);
						Matcher matcherEng = patternEng.matcher(plainTextDec);
						
						if (matcherIta.find()) 
							System.out.println("Testo decifrato in lingua italiana"+plainTextDec);
						else if (matcherEng.find())
							System.out.println("Testo decifrato in lingua inglese"+plainTextDec);
						}
							
					}
				}
			}
			
		}
	}


