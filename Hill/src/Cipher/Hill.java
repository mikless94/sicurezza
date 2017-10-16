package Cipher;

import java.security.SecureRandom;
import java.util.*;

public class Hill implements ClassicCipher {
	//trasformare in 29-b
	
	private String key; 
	private int m = 2;
	private int lenKey = m*m;
	private int[] key_num = new int[lenKey];
	private static Map<String, Integer> dict = new HashMap<>();
	private static Map<Integer, String> reversedDict = new HashMap <> ();
	boolean padding = false;
	

	static {
	    dict.put(" ", 0);
	    dict.put("a", 1);
	    dict.put("b", 2);
	    dict.put("c", 3);
	    dict.put("d", 4);
	    dict.put("e", 5);
	    dict.put("f", 6);
	    dict.put("g", 7);
	    dict.put("h", 8);
	    dict.put("i", 9);
	    dict.put("j", 10);
	    dict.put("k", 11);
	    dict.put("l", 12);
	    dict.put("m", 13);
	    dict.put("n", 14);
	    dict.put("o", 15);
	    dict.put("p", 16);
	    dict.put("q", 17);
	    dict.put("r", 18);
	    dict.put("s", 19);
	    dict.put("t", 20);
	    dict.put("u", 21);
	    dict.put("v", 22);
	    dict.put("w", 23);
	    dict.put("x", 24);
	    dict.put("y", 25);
	    dict.put("z", 26);
	    dict.put(",", 27);
	    dict.put("'", 28);
	}
	
    static {
    	for (String i : dict.keySet()) {
    	    reversedDict.put(dict.get(i), i);
    	}	
    }
    
    public static Map<String, Integer> getDict() {
		return dict;
	}	
    
	public static Map<Integer, String> getReversedDict() {
		return reversedDict;
	}
    
	
	public int getM() {
		return m;
	}

	@Override
	public void setKey(String key) throws InvalidKeyException {
		boolean foo;
		foo = checkKey(key);
		if (!foo) 
			throw new InvalidKeyException("Chiave '" + key +"' inserita non valida");
		this.key = key;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	//generates the key for the cipher
	public String genKey() {
		SecureRandom rand = new SecureRandom();
		String k;
		do {
			k = "";
			for (int i=0 ; i<lenKey; i++) {
				int number = rand.nextInt(29);
				k += reversedDict.get(number);
				System.out.println(("numero casuale:"+number+ "\tcarattere:"+reversedDict.get(number)));
			}
		} while (!checkKey(k));
		this.key = k;
		return k;
	}
	

	private boolean checkKey(String key) {
		
		//controllo lunghezza chiave
		if (key.length()!=lenKey)
			return false;

		//controllo simboli chiave appartengono all'alfabeto
		for (int i = 0; i<key.length() ; i++) {
			if (!dict.containsKey(Character.toString(key.charAt(i)))) 
				return false;
		}

		//controllo determinante nullo
		/* Non c'è bisogno di controllare se il determinante ha fattori in comune con il modulo base
		 * in quanto il modulo base è un numero primo.
		 */
		for (int i=0 ; i<key.length(); i++) 
			key_num[i] = dict.get(Character.toString(key.charAt(i)));
		
		if ((key_num[0]*key_num[3] - key_num[1]*key_num[2]) == 0)
			return false;
		
		return true;
	}

	@Override
	public String enc(String plainText) throws InvalidPlaintext {
		String cipherText = "";
		int [] digram = new int [m];
		
		for (int i=0 ; i<key.length(); i++) 
			key_num[i] = dict.get(Character.toString(key.charAt(i)));
		
		if(plainText.length() % 2 != 0){
			plainText += ' ';
			padding = true;
		}
		
		for (int i=0; i<plainText.length(); i=i+m) {
			for (int j=0; j<m; j++){
				if(!dict.containsKey(Character.toString(plainText.charAt(i+j))))
					throw new InvalidPlaintext("Testo inserito non valido");
				digram[j] = dict.get(Character.toString(plainText.charAt(i+j)));
			}
			cipherText += reversedDict.get((digram[0]*key_num[0]+digram[1]*key_num[2])%29);
			cipherText += reversedDict.get((digram[0]*key_num[1]+digram[1]*key_num[3])%29);
		}
		
		return cipherText;
	}
 
	@Override
	public String dec(String cipherText) {
		
		int det = key_num[0]*key_num[3] - key_num[1]*key_num[2];

		/*Inverto valori della diagonale principale, 
	 	  mentre dell'altra diagonale li sostituisco con gli inversi additivi. 
	 	  Infine moltiplico tutti i valori della matrice per l'inverso moltiplicativo di det!
	 	  */
		int temp = key_num[0];
		key_num[0] = key_num[3];
		key_num[3] = temp;
		key_num[1] = 29 - key_num[1];
		key_num[2] = 29 - key_num[2];
		if (det < 0)
			det = 29 + (det % 29);
		int inv = 1;
		int bool = 0;
		while(bool == 0){
			if (((det*inv) % 29) == 1)
				bool = 1;
			inv++;
		}
		inv -= 1;
		key_num[0] = (key_num[0]*inv) % 29;
		key_num[1] = (key_num[1]*inv) % 29;
		key_num[2] = (key_num[2]*inv) % 29;
		key_num[3] = (key_num[3]*inv) % 29;		
		
		String plainText = "";
		int [] digram = new int [m];
		
		for (int i=0; i<cipherText.length(); i=i+m) {
			for (int j=0; j<m; j++) 
				digram[j] = dict.get(Character.toString(cipherText.charAt(i+j)));
			plainText += reversedDict.get((digram[0]*key_num[0]+digram[1]*key_num[2]) % 29);
			plainText += reversedDict.get((digram[0]*key_num[1]+digram[1]*key_num[3]) % 29);
		}
		
		if (padding)
			plainText = plainText.substring(0, plainText.length()-1);
		return plainText;
	}
}
