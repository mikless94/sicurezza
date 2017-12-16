package progetto4.shamir;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Client implements Serializable{
	private String ID;
	private DistributedStorageService service;
	
	
	public Client(String iD) {
		ID = iD;
		//vedere se passare n e k
		service = DistributedStorageService.getInstance(5,3);
	}
	
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	
	public void shareFile(String filename){
		
		service.distributeFile(filename);
	}
	
	public void reconstructFile(String fileName, ArrayList<BigInteger> participants){
		
		service.reconstructFile(fileName, participants);
	}
	
	

}
