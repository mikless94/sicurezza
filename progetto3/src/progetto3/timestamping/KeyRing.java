package progetto3.timestamping;

public class KeyRing {
	private static KeyRing keyRingTSA;
	
	
	
	/**
	 * @return the keyRingTSA
	 */
	public static KeyRing getKeyRingTSA() {
		return keyRingTSA;
	}



	/**
	 * @param keyRingTSA the keyRingTSA to set
	 */
	public static void setKeyRingTSA(KeyRing keyRingTSA) {
		KeyRing.keyRingTSA = keyRingTSA;
	}



	static void addToKeyring (String file) {
		
	}

}
