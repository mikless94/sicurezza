package CifrarioIbrido;

import java.security.*;

public class User {
	private String name;
	private PrivateKey key;
	
	
	public User(String name, PrivateKey key) {
		super();
		this.name = name;
		this.key = key;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public PrivateKey getKey() {
		return key;
	}


	public void setKey(PrivateKey key) {
		this.key = key;
	}
	
	
	
}
