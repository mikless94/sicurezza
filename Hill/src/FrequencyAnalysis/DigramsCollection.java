package FrequencyAnalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import FrequencyAnalysis.Digram;

public class DigramsCollection {
	
	private ArrayList<Digram> digrams;
	
	public DigramsCollection() 
	{
		this.digrams = new ArrayList<>();
	}
	
	public void takeDigramsFromFile(String fileName)
	{
		HashSet<String> alphabet = new HashSet<>();
		String stringLine;
		
		//creazione alfabeto simboli con hashset per ricerca in expected O(1)
		alphabet.add("a");
		alphabet.add("b");
		alphabet.add("c");
		alphabet.add("d");
		alphabet.add("e");
		alphabet.add("f");
		alphabet.add("g");
		alphabet.add("h");
		alphabet.add("i");
		alphabet.add("j");
		alphabet.add("k");
		alphabet.add("l");
		alphabet.add("m");
		alphabet.add("n");
		alphabet.add("o");
		alphabet.add("p");
		alphabet.add("q");
		alphabet.add("r");
		alphabet.add("s");
		alphabet.add("t");
		alphabet.add("u");
		alphabet.add("v");
		alphabet.add("w");
		alphabet.add("x");
		alphabet.add("y");
		alphabet.add("z");
		alphabet.add(",");
		alphabet.add(" ");
		alphabet.add("'");
		
		BufferedReader buf;
		try {
			buf = new BufferedReader(new FileReader(fileName));
			stringLine = buf.readLine(); //salto la prima riga con gli identificativi
			stringLine = buf.readLine();
			
			while(stringLine != null) {
				String[] current_str = stringLine.split("\\s+");
				if(alphabet.contains(current_str[0]) && alphabet.contains(current_str[1])) {
					this.digrams.add(new Digram(current_str[0], current_str[1], Integer.parseInt(current_str[6])));
				}
				stringLine = buf.readLine();
			}
			
			buf.close();
			Collections.sort(this.digrams);
			
		} catch (IOException e) {
			System.out.println("Errore durante le operazioni di I/O.\n");
			e.printStackTrace();
		}
	}
	
	public String printCollection()
	{
		String s = "";
		
		for(Digram d : this.digrams)
		{
			s += d.printDigram() + "\n";
		}
		
		return s;
	}

	
	public String sizeOfCollection() 
	{
		return Integer.toString(this.digrams.size());
	}
	
	public ArrayList<Digram> getDigramCollection(){
		return this.digrams;
	}
	
}
	
