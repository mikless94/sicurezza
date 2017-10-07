package Cipher;

import java.security.SecureRandom;
import java.util.*;

public class Hill implements ClassicCipher {
	//trasformare in 29-b
	
	private String key;
	private int m = 4;
	private static Map<String, Integer> dict = new HashMap<>();
	private static Map<Integer, String> reversedDict = new HashMap <> ();
	
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
	
	@Override
	public void setKey(String key) throws InvalidKeyException {
		boolean foo;
		foo = checkKey(key);
		if (!foo) 
			throw new InvalidKeyException("Chiave non valida");
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return this.key;
	}

	@Override
	//generates the key for the cipher
	public String genKey() {
		String k = "";
		SecureRandom rand = new SecureRandom();
		
		do {
			for (int i=0 ; i<m; i++ ) {
				int number = rand.nextInt(29);
				k += reversedDict.get(number);
				//System.out.println(("numero casuale:"+number+ "\tcarattere:"+reversedDict.get(number)));
			}
		} while (checkKey(k));
		return k;
	}
	
	
	private boolean checkKey(String key) {
		//Calcolo lunghezza della chiave:
		System.out.println(key);
		if (key.length()!=m)
			return false;
		
		//Calcolo presenza di caratteri indesiderati:
		for (int i = 0; i<key.length(); i++) {
			if (!dict.containsKey(Character.toString(key.charAt(i)))) 
				return false;
		}
		
		//Calcolo determinante:
		int k11 = dict.get(Character.toString(key.charAt(0)));
		int k12 = dict.get(Character.toString(key.charAt(1)));
		int k21 = dict.get(Character.toString(key.charAt(2)));
		int k22 = dict.get(Character.toString(key.charAt(3)));

		int det = ((k11*k22) - (k12*k21)) % 29;
		System.out.println("Determinante della matrice: " + det);
		if(det == 0)
			return false;
		
		return true;
	}

	@Override
	public String enc(String plainText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dec(String cipherText) {
		// TODO Auto-generated method stub
		return null;
	}

}
