package BruteForce;
import Cipher.*;

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
		String patternIta = "";
		String patternEng = "";
		
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
						
						/*System.out.println(i);
						System.out.println(j);
						System.out.println(l);
						System.out.println(n);
						System.out.println(k);*/
						
						try {
							h.setKey(k);
						} catch(InvalidKeyException ex) {
							continue;
						};
						//System.out.println(h.getKey());
						String plainTextDec = h.dec("kgeprrm ,gilzhpn,fhcaposvv,rqrp'pwwdj ");
						System.out.println("Testo decifrato con chiave "+h.getKey());
						
						if (plainTextDec.matches(patternIta)) 
							System.out.println("Testo decifrato in lingua italiana"+plainTextDec);
						
						if (plainTextDec.matches(patternEng))
							System.out.println("Testo decifrato in lingua inglese"+plainTextDec);
						
						}
							
					}
				}
			}
			
		}
		
		

	}


