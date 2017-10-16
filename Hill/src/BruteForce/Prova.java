package BruteForce;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prova {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pattern patternIta = Pattern.compile("[a-z&&[^aeiou]]{4,}");
		String plainText ="vfg";
		Matcher matcherIta = patternIta.matcher(plainText);
		if (!matcherIta.find()) {
			System.out.println(plainText);
		}
		

	}

}
