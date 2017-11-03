package CifrarioIbrido;

import java.io.File;

import javax.swing.JFileChooser;

public class OpenFile {
	
	JFileChooser file_chooser = new JFileChooser();
	String fileName;
	
	public String pickMe() {
		File file;
		if(file_chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			file = file_chooser.getSelectedFile();
			fileName = file_chooser.getSelectedFile().getAbsolutePath();
		}
		else{
			fileName = "";
		}
		return fileName;
	}

}
