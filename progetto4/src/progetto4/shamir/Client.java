package progetto4.shamir;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Client {
	private String ID;
	private HashMap<String, ArrayList<String>> dict;
	private DistributedStorageService service;
	
	
	public Client(String iD) {
		ID = iD;
		dict = new HashMap<String, ArrayList<String>>();
		service = DistributedStorageService.getInstance();
	}
	
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public HashMap<String, ArrayList<String>> getDict() {
		return dict;
	}
	public void setDict(HashMap<String, ArrayList<String>> dict) {
		this.dict = dict;
	}
	
	
	public void shareFile(String filename){
		
		service.distributeFile(filename);
	}
	
	public void reconstructFile(String fileName, ArrayList<BigInteger> participants){
		
		service.reconstructFile(fileName, participants);
	}
	
	

}
