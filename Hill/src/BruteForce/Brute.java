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
		
		//va bene sia considerare euristiche separate per italiano e inglese o euristiche congiunte
		int num_keys = 0;
		Hill h = new Hill();
		String k = "";
		int word_length = 9;
		
		//Pattern patternIta = Pattern.compile("((([b-df-hj-np-tv-z])(?!\\2)){3})");
		//Pattern patternIta = Pattern.compile("th|wh|,[a-z']|[a-z&&[^aeiou]]{3,5}""(.+)\\1+");
		//Pattern patternIta = Pattern.compile("(\\w)\\2+|([aeiou])\\1+|,[a-z',]|q[^qu]|jq|th|lk|kl|fv|fw|gz|nm|gc|yz|wv|wz|vw|vz|zw|rw|cf|sw|nr|kh|bt|rz|kx|pv|dp|"
				//+ "lb|gk|zh|vs|vt|mt|cj|jq|gw");
		Pattern patternIta = Pattern.compile("(.)\\1{2,}|,\\S|'{2,}|\\s{2,}|[a-z&&[^aeiou]]{4,}|\\w{"+word_length+",}\\b");
		
		
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
		
						String plainText = h.dec("m bqhigabqkmawahofsbhx'frc'zavfqbntgxpo'r ckudiqrqrvexj,jtesllffo'i vttytwofhjgohtbays'i");
						Matcher matcherIta = patternIta.matcher(plainText);
						if (!matcherIta.find()) {
							num_keys++;
							System.out.println(plainText);
							//System.out.println(k);
						}
						}	
					}
				}
			}
		System.out.println("numero chiavi:"+num_keys);
	
			
		}
	}


