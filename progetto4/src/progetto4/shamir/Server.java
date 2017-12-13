package progetto4.shamir;

import java.io.File;
import java.math.BigInteger;

public class Server {

	private static BigInteger IDToAssign = BigInteger.ZERO;
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
