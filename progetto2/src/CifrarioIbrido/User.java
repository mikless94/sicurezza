package CifrarioIbrido;

import java.security.*;

public class User {
	private String name;
	private String padding;
	private byte[] salt;
	private String password;
	
	
	

	/**
	 * @param name
	 */
	public User(String name) {
		this.name = name;
	}





	/**
	 * @param name
	 * @param password
	 */
	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}





	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return (arg0 instanceof User) && (((User)arg0).getName()).equals(this.getName());
	}





	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return name.hashCode();
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

		this.padding = padding;

	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public byte[] getSalt() {
		return salt;
	}


	public void setSalt(byte[] salt) {
		this.salt = salt;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}	
	
	
}
