package CifrarioIbrido;

public class Incapsula {
	//parametri provenienti dall'interfaccia grafica
	private int dimKey;
	private String padding;
	private String pubKeyFile;
	private AsymmetricCipher asymCipher = new AsymmetricCipher (dimKey,padding);
	
	
	

}
