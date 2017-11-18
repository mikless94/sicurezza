package progetto3.timestamping;

import java.util.ArrayList;

public class TSA {
	
	private ArrayList <byte[]> rootHash;
	private ArrayList <byte[]> superHash;
	private String rootHashFile = "rootHashFile.pub";
	private String superHashFile = "superHashFile.pub";
	public static final int MERKLE_TREE_DIM = 15;
	private byte[][] merkelTree = new byte[MERKLE_TREE_DIM][];
	private static int serialNumber = 0; 
	
	public void generateReply (ArrayList <String> queries) {
		
	}
	
}
