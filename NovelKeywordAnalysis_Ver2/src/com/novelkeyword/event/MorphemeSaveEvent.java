package com.novelkeyword.event;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import com.novelkeyword.io.BookFileReader;
import com.novelkeyword.io.ContentWriter;
import com.novelkeyword.io.MorphemeSplitter;
import com.novelkeyword.novel.Word;
import com.novelkeyword.novel.WordContent;

public class MorphemeSaveEvent
	implements ActionListener {
	private TextField filePathField;
	private String path;
	
	public MorphemeSaveEvent(TextField filePathField) {
		this.filePathField = filePathField;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BookFileReader reader = new BookFileReader();
		List<String> content = reader.read(filePathField.getText().toString());
		
		MorphemeSplitter splitter = new MorphemeSplitter(content, reader.getFileName());
		WordContent wordContent = splitter.splitter();
		
		ContentWriter writer = new ContentWriter("morpheme/" 
												+ reader.getFileName()
												+"_morpheme.txt");
		writer.write(wordContent);
		
		JOptionPane.showMessageDialog(null, "결과완료");
	}

}
