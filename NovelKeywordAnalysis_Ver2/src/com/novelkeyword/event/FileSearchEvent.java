package com.novelkeyword.event;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

public class FileSearchEvent 
	implements ActionListener {
	
	private String path;
	private TextField textField;
	
	public FileSearchEvent(TextField textField) {
		this.textField = textField;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
		
		int value = fileChooser.showOpenDialog(null);
		if(value == fileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			path = file.toString();
			
			textField.setText(path);
		}
	}

	public String getPath() {
		return path;
	}
	
}
