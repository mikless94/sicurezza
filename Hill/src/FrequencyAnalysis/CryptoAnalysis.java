package FrequencyAnalysis;

import java.io.*;

public class CryptoAnalysis {
	
	private String cipherText;
	
	public CryptoAnalysis() {
		this.cipherText = "";
	}
	
	public void readCipherTextFromFile(String fileName){
		String data = "";
		String stringLine;
		
		BufferedReader buf;
		try {
			buf = new BufferedReader(new FileReader(fileName));
			stringLine = buf.readLine();
			while(stringLine != null) {
				data += stringLine + '\n';
				stringLine = buf.readLine();
			}
			buf.close();
		} catch (IOException e) {
			System.out.println("Errore durante le operazioni di I/O.\n");
			e.printStackTrace();
		}
		this.cipherText = data;
	}

	public String getCipherText() {
		return cipherText;
	}

	public void setCipherText(String cipherText) {
		this.cipherText = cipherText;
	}
	
}
