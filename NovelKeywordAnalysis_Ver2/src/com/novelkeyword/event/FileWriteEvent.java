package com.novelkeyword.event;

import java.awt.Dialog;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import com.novelkeyword.io.BookFileWriter;
import com.novelkeyword.io.ContentSplitter;
import com.novelkeyword.io.BookFileReader;

public class FileWriteEvent 
	implements ActionListener {
	private String bookName;
	private String path;
	private TextField filePathField;
	
	public FileWriteEvent(String bookName, List<String> Content) {
		this.bookName = bookName;
	}
	
	public FileWriteEvent(TextField filePathField) {
		this.filePathField = filePathField;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		path = filePathField.getText().toString();
		
		BookFileReader reader = new BookFileReader();
		BookFileWriter writer = null;
		
		List<String> content = reader.read(path);
		
		ContentSplitter splitter = new ContentSplitter(content);
		splitter.splitter();
		
		writer = new BookFileWriter("book\\" + reader.getFileName() + "_dialogue.txt");
		writer.write(splitter.getDialogueContent());
		
		writer = new BookFileWriter("book\\" + reader.getFileName() + "_nonDialogue.txt");
		writer.write(splitter.getNonDialogueContnet());
		
		JOptionPane.showMessageDialog(null, "결과완료");
	}
	
}
