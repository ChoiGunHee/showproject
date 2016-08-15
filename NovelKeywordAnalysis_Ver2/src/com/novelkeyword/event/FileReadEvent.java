package com.novelkeyword.event;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.novelkeyword.io.ContentSplitter;
import com.novelkeyword.io.BookFileReader;
import com.novelkeyword.view.LineContentFrame;

public class FileReadEvent 
	implements ActionListener {
	
	private List<String> content;
	private String path;
	private TextField filePathField;
	
	public FileReadEvent() {
	}
	
	public FileReadEvent(TextField filePathField) {
		this.filePathField = filePathField;
	}
	
	public FileReadEvent(String string) {
		path = string;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		path = filePathField.getText().toString();
		BookFileReader reader = new BookFileReader();
		content = reader.read(path);
		
		ContentSplitter splitter = new ContentSplitter(content);
		splitter.splitter();
		
		LineContentFrame dialogueContent = new LineContentFrame("Dialogue");
		LineContentFrame nonDialogueContent = new LineContentFrame("NonDialouge");
		
		dialogueContent.setContent(splitter.getDialogueContent());
		nonDialogueContent.setContent(splitter.getNonDialogueContnet());
		
		JOptionPane.showMessageDialog(null, "결과완료");
	}
	
}
