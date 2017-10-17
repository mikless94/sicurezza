package BruteForce;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prova {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Pattern patternIta = Pattern.compile("\\w{5,}\\s");
		String plainText ="ssdsv fdsc";
		Matcher matcherIta = patternIta.matcher(plainText);
		if (!matcherIta.find()) {
			System.out.println(plainText);
		}
		

	}

}
