package progetto4.shamir;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class Prova {

	public static void main(String[] args) {
		String output;
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		System.out.println(sb.toString());
	}

}
