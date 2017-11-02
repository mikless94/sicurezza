package CifrarioIbrido;

import java.security.*;

public class User {
	private String name;
	private PrivateKey asymmetricKey;
	private String padding;
	private PrivateKey signKey;
	
	
	/**
	 * @return the asymmetricKey
	 */
	public PrivateKey getAsymmetricKey() {
		return asymmetricKey;
	}


	/**
	 * @param asymmetricKey the asymmetricKey to set
	 */
	public void setAsymmetricKey(PrivateKey asymmetricKey) {
		this.asymmetricKey = asymmetricKey;
	}


	/**
	 * @return the signKey
	 */
	public PrivateKey getSignKey() {
		return signKey;
	}


	/**
	 * @param signKey the signKey to set
	 */
	public void setSignKey(PrivateKey signKey) {
		this.signKey = signKey;
	}


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


	public User(String name, PrivateKey asymmetricKey, String padding) {
		super();
		this.name = name;
		this.asymmetricKey = asymmetricKey;
		this.padding = padding;

	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}	
}
