package KnownPlaintext;

import Cipher.Hill;

public class KnownPlaintext {
	
	public String attack(String plainText, String cipherText) throws PlainTextException {
		
		Hill cipher = new Hill();
		int[] key = new int[cipher.getM()*cipher.getM()];

		
		int[] plainMatrix = new int[cipher.getM()*cipher.getM()];
		int[] cipherMatrix = new int[cipher.getM()*cipher.getM()];
		
		/* Per ottenere la matrice K, di dimensione m x m, ho bisogno di m=2 coppie plainText-cipherText
		 * ognuna lungo m. Nel nostro caso specifico le due matrici plainMatrix e cipherMatrix hanno 
		 * dimensione 2 x 2: in ogni matrice ci stanno due digrammi.
		 */
		int comp = 1;
		for (int i = 0; (i < plainText.length() - cipher.getM()) && (comp == 1); i = i+cipher.getM()){
			plainMatrix[0] = Hill.getDict().get(Character.toString(plainText.charAt(i)));
			plainMatrix[1] = Hill.getDict().get(Character.toString(plainText.charAt(i+1)));
			cipherMatrix[0] = Hill.getDict().get(Character.toString(cipherText.charAt(i)));
			cipherMatrix[1] = Hill.getDict().get(Character.toString(cipherText.charAt(i+1)));
			for (int j = i + 2; (j < plainText.length()) && (comp == 1); j = j + cipher.getM()){
				plainMatrix[2] = Hill.getDict().get(Character.toString(plainText.charAt(j)));
				plainMatrix[3] = Hill.getDict().get(Character.toString(plainText.charAt(j+1)));
				cipherMatrix[2] = Hill.getDict().get(Character.toString(cipherText.charAt(j)));
				cipherMatrix[3] = Hill.getDict().get(Character.toString(cipherText.charAt(j+1)));
				
				if (plainMatrix[0]*plainMatrix[3] - plainMatrix[1]*plainMatrix[2] != 0)
					comp = 0;				
			}
		}
		
		if (((plainMatrix[0]*plainMatrix[3] - plainMatrix[1]*plainMatrix[2]) % 29) == 0)
			throw new PlainTextException("Non esistono due digrammi nel plainText " + plainText + " tale per cui la matrice X sia invertibile!");

		/*Per ottenere la matrice K ---> cipherMatrix = (plainMatrix)*K ---> K = cipherMatrix*(plainMatrix)^-1
		 *
		 *Calcolo inversa di plainMatrix:
		 */
		int det = plainMatrix[0]*plainMatrix[3] - plainMatrix[1]*plainMatrix[2];
		int temp = plainMatrix[0];
		plainMatrix[0] = plainMatrix[3];
		plainMatrix[3] = temp;
		plainMatrix[1] = 29 - plainMatrix[1];
		plainMatrix[2] = 29 - plainMatrix[2];
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
		plainMatrix[0] = (plainMatrix[0]*inv) % 29;
		plainMatrix[1] = (plainMatrix[1]*inv) % 29;
		plainMatrix[2] = (plainMatrix[2]*inv) % 29;
		plainMatrix[3] = (plainMatrix[3]*inv) % 29;		
		
		
		//Calcolo matrice K:
		key[0] = (plainMatrix[0]*cipherMatrix[0] + plainMatrix[1]*cipherMatrix[2]) % 29;
		key[1] = (plainMatrix[0]*cipherMatrix[1] + plainMatrix[1]*cipherMatrix[3]) % 29;
		key[2] = (plainMatrix[2]*cipherMatrix[0] + plainMatrix[3]*cipherMatrix[2]) % 29;
		key[3] = (plainMatrix[2]*cipherMatrix[1] + plainMatrix[3]*cipherMatrix[3]) % 29;
		
		String k = Hill.getReversedDict().get(key[0]) + Hill.getReversedDict().get(key[1]) 
				+ Hill.getReversedDict().get(key[2]) + Hill.getReversedDict().get(key[3]);
		
		return k;
		
	}
}
