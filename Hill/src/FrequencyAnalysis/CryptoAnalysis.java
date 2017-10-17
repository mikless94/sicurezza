package FrequencyAnalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import FrequencyAnalysis.Digram;

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
				data += stringLine;
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
	
	public ArrayList<Digram> analyzeText()
	{
		TreeMap<String, Integer> statistics = new TreeMap<String, Integer>();
		ArrayList<Digram> ret_list = new ArrayList<Digram>();
		char [] array = this.cipherText.toCharArray();
		int size = array.length;
		int i;
		
		for(i = 0; i < size - 1; i += 2)
		{
			if(array[i] != '\u0000' && array[i+1] != '\u0000') 
			{
				String s = "" + array[i] + array[i+1];
				
				if (statistics.containsKey(s))
				{
					Integer value = statistics.get(s);
					statistics.replace(s, value + 1);
				}
				else
				{
					statistics.put(s, 1);
				}
			}
		}
		
		Set<String> keys = statistics.keySet();
		for(String key : keys) 
		{
			Integer value = (Integer) statistics.get(key);
			char [] vect = key.toCharArray();
			String s1 = "" + vect[0];
			String s2 = "" + vect[1];
			ret_list.add(new Digram(s1, s2, (int) value));
		}

		Collections.sort(ret_list);
		return ret_list;
	}
	
	public String tryToDec(ArrayList<Digram> english_stats, ArrayList<Digram> cipherTextStats)
	{
		int size_en = english_stats.size();
		int size_text = cipherTextStats.size();
		String text = new String(this.cipherText);
		
		int i;
		for(i = 0; i < 2; i++) {
			Digram d1 = english_stats.get(size_en - 1 - i);
			String s1 = "" + d1.getFirstCharacter() + d1.getSecondCharacter();
			Digram toChange1 = cipherTextStats.get(size_text - 1 - i);
			String s2 = "" + toChange1.getFirstCharacter() + toChange1.getSecondCharacter();
			text = text.replace(s2, s1.toUpperCase());
		}
		return text;
	}
	
	public ArrayList<String> betDigram(String decText)
	{
		ArrayList<String> digrams = new ArrayList<String>();
		char [] text = decText.toCharArray();
		int size = text.length;
		int i;
		
		for(i = 0; i < size - 1; i += 2)
		{
			if(text[i] == 'T' && text[i+1] == 'H') 
			{
				String s = "" + text[i+2] + text[i+3];
				if(!digrams.contains(s)) {
					digrams.add(s);
				}
			}
		}
		return digrams;
	}
		
}

