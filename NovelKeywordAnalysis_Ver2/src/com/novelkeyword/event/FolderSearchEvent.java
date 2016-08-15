package com.novelkeyword.event;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;

public class FolderSearchEvent
	implements ActionListener {
	private TextField filePathField;
	private String folderPath;
	
	public FolderSearchEvent(TextField filePathField) {
		this.filePathField = filePathField;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("./"));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int value = fileChooser.showOpenDialog(null);
		if(value == fileChooser.APPROVE_OPTION) {
			File folder = fileChooser.getSelectedFile();
			folderPath = folder.toString();
		}
		
		filePathField.setText(folderPath.toString());
		
	}

}
