package progetto4.shamir;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileToSend implements Serializable{
	private String filename;
	private long fileDimension;
	private BigInteger prime;
	private byte [] hash;
	
	
	public FileToSend(String filename) {
		this.filename = filename;
		
		File f = new File(filename);
		fileDimension = f.length();
		
		
	}



	public String getFilename() {
		return filename;
	}



	public void setFilename(String filename) {
		this.filename = filename;
	}



	public long getFileDimension() {
		return fileDimension;
	}



	public void setFileDimension(long fileDimension) {
		this.fileDimension = fileDimension;
	}



	public BigInteger getPrime() {
		return prime;
	}



	public void setPrime(BigInteger prime) {
		this.prime = prime;
	}






	/**
	 * @return the hash
	 */
	public byte[] getHash() {
		return hash;
	}



	/**
	 * @param hash the hash to set
	 */
	public void setHash(byte[] hash) {
		this.hash = hash;
	}
	
	
	
	

}
