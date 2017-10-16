package KnownPlaintext;
import Cipher.*;


public class KnownPlaintextAttack {

	public static void main(String[] args) {
		
		KnownPlaintext cipher = new KnownPlaintext();
		
		try{
			//Primo attacco:
			
			System.out.println("--------------------CHIAVE 1-------------------");
			System.out.println("PLAINTEXT: attacco all'alba");
			System.out.println("CIPHERTEXT: axiarpjuzhvizhz'");
			String key1 = cipher.attack("attacco all'alba", "axiarpjuzhvizhz'");
			System.out.println("La chiave utilizzata per il PRIMO testo è: " + key1);
			
			Hill hill1 = new Hill();
			
			try{
				hill1.setKey(key1);
				String cipherText1 = hill1.enc("attacco all'alba");
				System.out.println(cipherText1.equals("axiarpjuzhvizhz'"));
				
			} catch (InvalidKeyException ex){} catch (InvalidPlaintext ex){}
			System.out.println("-----------------------------------------------");
			System.out.println("\n");			
			
			/*Secondo attacco:
			*/
			System.out.println("--------------------CHIAVE 2-------------------");
			System.out.println("PLAINTEXT: sicurezza informatica");
			System.out.println("CIPHERTEXT: xrjddqf,jgimyexebfyojg");
			String key2 = cipher.attack("sicurezza informatica", "xrjddqf,jgimyexebfyojg");
			System.out.println("La chiave utilizzata per il SECONDO testo è: " + key2);
			
			Hill hill2 = new Hill();
			
			try{
				hill2.setKey(key2);
				String cipherText2 = hill2.enc("sicurezza informatica");
				System.out.println(cipherText2.equals("xrjddqf,jgimyexebfyojg"));
				
			} catch (InvalidKeyException ex){} catch (InvalidPlaintext ex){}
			System.out.println("-----------------------------------------------");			
			System.out.println("\n");	

			/*Terzo attacco:
			*/
			System.out.println("--------------------CHIAVE 3-------------------");
			System.out.println("PLAINTEXT: farc, fuerzas armadas revolucionarias de colombia");
			System.out.println("CIPHERTEXT: dylqxjpwchylduv'gs'fduskfsbvjpn'v'zjdumwytgeybvuqx");
			String key3 = cipher.attack("farc, fuerzas armadas revolucionarias de colombia", "dylqxjpwchylduv'gs'fduskfsbvjpn'v'zjdumwytgeybvuqx");
			System.out.println("La chiave utilizzata per il TERZO testo è: " + key3);
			
			Hill hill3 = new Hill();
			
			try{
				hill3.setKey(key3);
				String cipherText3 = hill3.enc("farc, fuerzas armadas revolucionarias de colombia");
				System.out.println(cipherText3.equals("dylqxjpwchylduv'gs'fduskfsbvjpn'v'zjdumwytgeybvuqx"));
				
			} catch (InvalidKeyException ex){} catch (InvalidPlaintext ex){}
			System.out.println("-----------------------------------------------");
			System.out.println("\n");	
			
			/*Quarto attacco:
			*/
			System.out.println("--------------------CHIAVE 4-------------------");
			System.out.println("PLAINTEXT: postuv korespondencni problem");
			System.out.println("CIPHERTEXT: i, hcjtxxk yi,phlvj,iuywpm bmk");
			String key4 = cipher.attack("postuv korespondencni problem", "i, hcjtxxk yi,phlvj,iuywpm bmk");
			System.out.println("La chiave utilizzata per il QUARTO testo è: " + key4);
			
			Hill hill4 = new Hill();
			
			try{
				hill4.setKey(key4);
				String cipherText4 = hill4.enc("postuv korespondencni problem");
				System.out.println(cipherText4.equals("i, hcjtxxk yi,phlvj,iuywpm bmk"));
				
			} catch (InvalidKeyException ex){} catch (InvalidPlaintext ex){}
			System.out.println("-----------------------------------------------");
			System.out.println("\n");	
			
			/*Quinto attacco:
			*/
			System.out.println("--------------------CHIAVE 5-------------------");
			System.out.println("PLAINTEXT: dali' salvador, pittore");
			System.out.println("CIPHERTEXT: pmeovcezwuqje'ofdcpee'fn");
			String key5 = cipher.attack("dali' salvador, pittore", "pmeovcezwuqje'ofdcpee'fn");
			System.out.println("La chiave utilizzata per il QUINTO testo è: " + key5);

			Hill hill5 = new Hill();
			
			try{
				hill5.setKey(key5);
				String cipherText5 = hill5.enc("dali' salvador, pittore");
				System.out.println(cipherText5.equals("pmeovcezwuqje'ofdcpee'fn"));
				
			} catch (InvalidKeyException ex){} catch (InvalidPlaintext ex){}
			System.out.println("-----------------------------------------------");
			System.out.println("\n");	
			
		} catch(PlainTextException ex) {}
		
		

	}

}
