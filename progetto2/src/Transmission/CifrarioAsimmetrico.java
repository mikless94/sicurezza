package Transmission;

public class CifrarioAsimmetrico {
	
	int keyDim;
	String padding;
	/**
	 * @param keyDim
	 * @param padding
	 */
	public CifrarioAsimmetrico(int keyDim, String padding) {
		this.keyDim = keyDim;
		this.padding = padding;
	}
	/**
	 * @return the keyDim
	 */
	public int getKeyDim() {
		return keyDim;
	}
	/**
	 * @param keyDim the keyDim to set
	 */
	public void setKeyDim(int keyDim) {
		this.keyDim = keyDim;
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

	public void ciao() {System.out.println("sono ambrogio");}
	
	

}
