package progetto4.shamir;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileToSend {
	private String filename;
	private Path path;
	private long fileDimension;
	private BigInteger prime;
	
	
	public FileToSend(String filename) {
		this.filename = filename;
		
		File f = new File(filename);
		fileDimension = f.length();
		
		path = Paths.get(filename);
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



	public Path getPath() {
		return path;
	}



	public void setPath(Path path) {
		this.path = path;
	}
	
	

}
