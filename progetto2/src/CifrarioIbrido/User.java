package CifrarioIbrido;

import java.security.*;

public class User {
	private String name;
	private PrivateKey key;
	private String padding;
	
	
	/**
	 * @return the padding
	 */
	public String getPadding() {
		return padding;
	}


	/**
	 * @param padding the padding to set
	 */
	public void setPadding(String padding) {
		this.padding = padding;
	}


	public User(String name, PrivateKey key, String padding) {
		super();
		this.name = name;
		this.key = key;
		this.padding = padding;
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
