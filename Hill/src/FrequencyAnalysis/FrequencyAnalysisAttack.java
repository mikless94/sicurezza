package FrequencyAnalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Cipher.*;
import FrequencyAnalysis.Bigram;
import KnownPlaintext.*;

public class FrequencyAnalysisAttack {
	
	private String cipherText;
	private BigramsCollection genBigramStats;
	private ArrayList<Bigram> cipherTextStats;
	private ArrayList<String> commonWord;
	private String subText;
	
	public FrequencyAnalysisAttack() {
		this.cipherText = "";
		this.genBigramStats = new BigramsCollection();
		this.cipherTextStats = new ArrayList<Bigram>();
		this.commonWord = new ArrayList<String>();
		this.commonWord.add("the ");
		this.commonWord.add(" the");
		this.subText = "";
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
	
	public void takeGeneralBigramStats(String fileName) {
		this.genBigramStats.takeDigramsFromFile(fileName);
	}
	
	public ArrayList<String> attack(boolean mode) {
		
		this.cipherTextStats = this.findCipherTextStats(); //acquisisco le statistiche dei digrammi del testo
		this.subText = this.cipherText.substring(0, 600);
		ArrayList<String> retList = new ArrayList<String>(); //per la serie di testi decrittati
		int sizeTextStats = this.cipherTextStats.size();
		ArrayList<String> possibleKeys = new ArrayList<String>(); //tutte le possibili chiavi
		
		if(mode)
		{
			int i;
			for(i = 0; i < 5; i++) 
			{
				Bigram digram = cipherTextStats.get(sizeTextStats - 1 - i);
				String s = "" + digram.getFirstCharacter() + digram.getSecondCharacter();
				ArrayList<String> digrams = this.getFollowingDigrams(s);
				possibleKeys = this.findPossibleKeys(s, digrams, commonWord.get(0));
				retList.addAll(this.tryToDec(possibleKeys));
			}
			return retList;
		}
		else
		{
			int i;
			for(i = 0; i < 5; i++) 
			{
				Bigram digram = cipherTextStats.get(sizeTextStats - 1 - i);
				String s = "" + digram.getFirstCharacter() + digram.getSecondCharacter();
				ArrayList<String> digrams = this.getFollowingDigrams(s);
				possibleKeys = this.findPossibleKeys(s, digrams, commonWord.get(1)); 
				retList.addAll(this.tryToDec(possibleKeys));
			}
			return retList;
		}
	}
	
	private ArrayList<Bigram> findCipherTextStats()
	{
		TreeMap<String, Integer> statistics = new TreeMap<String, Integer>();
		ArrayList<Bigram> ret_list = new ArrayList<Bigram>();
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
			ret_list.add(new Bigram(s1, s2, (int) value));
		}

		Collections.sort(ret_list);
		return ret_list;
	}

	private ArrayList<String> getFollowingDigrams(String digram)
	{
		ArrayList<String> digrams = new ArrayList<String>();
		char [] text = this.getCipherText().toCharArray();
		char [] digramChars = digram.toCharArray();
		int size = text.length;
		int i;
		
		for(i = 0; i < size - 1; i += 2)
		{
			if(text[i] == digramChars[0] && text[i+1] == digramChars[1]) 
			{
				String s = "" + text[i+2] + text[i+3];
				if(!digrams.contains(s)) {
					digrams.add(s);
				}
			}
		}
		return digrams;
	}
	
	private ArrayList<String> findPossibleKeys(String knownCodedBigram, ArrayList<String> foundBigrams, String plainBigram)
	{
		ArrayList<String> possibleKeys = new ArrayList<String>();
		KnownPlaintext knownPlain = new KnownPlaintext();
		
		for(String s : foundBigrams)
		{
			String codedDigram = knownCodedBigram + s;
			String res;
			try {
				res = knownPlain.attack(plainBigram, codedDigram);
				possibleKeys.add(res);
			} catch (PlainTextException e) {
				e.printStackTrace();
			}	
		}
		return possibleKeys;
	}
	
	private ArrayList<String> tryToDec(ArrayList<String> keys)
	{
		ArrayList<String> ret = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\s{2,}|[a-z&&[^aeiou]]{5,}");
		Hill ciph = new Hill();
		for(String s : keys)
		{
			try 
			{
				ciph.setKey(s);
				String decodedText = ciph.dec(this.subText);
				Matcher matcher = pattern.matcher(decodedText);
				if (!matcher.find())
				{
					ret.add("Key: " + s + "\n" + decodedText + "\n");
				}
			}catch (InvalidKeyException e) 
			{
				continue;
			}
		}
		return ret;
	}
	
	public String decCipherText(String key) {
		Hill plaintext = new Hill();
		try {
				plaintext.setKey(key);
			}catch(InvalidKeyException E) {
				System.out.println("Chiave inserita non valida.\n");	
		}
		return plaintext.dec(this.cipherText);
	}
	
	public String printGenStats()
	{
		return this.genBigramStats.printCollection();
	}
	
	public String printCipherTextStats()
	{
		String ret = "";
		for(Bigram b : this.cipherTextStats)
		{
			ret += b.printDigram() + "\n";
		}
		return ret;
	}
		
}

