package progetto4.shamir;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;


public class Server implements Serializable{

	private static BigInteger IDToAssign = BigInteger.valueOf(1);
	private BigInteger ID;
	private String directory;
	
	/**
	 * 
	 */
	public Server() {
		ID = IDToAssign;
		IDToAssign = IDToAssign.add(BigInteger.valueOf(1));
		
		//creazione directory
		File f = new File("server" + ID);
		String [] entries = f.list();
		
		if (entries!=null) {
			for(String s: entries){
			    File currentFile = new File(f.getPath(),s);
			    currentFile.delete();
			}
		}
		f.mkdir();
		directory = f.getAbsolutePath();
	}

	/**
	 * @return the iD
	 */
	public BigInteger getID() {
		return ID;
	}

	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}

	
	
	
	
	

	
}
