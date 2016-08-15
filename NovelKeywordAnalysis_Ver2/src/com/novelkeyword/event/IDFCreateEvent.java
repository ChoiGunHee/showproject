package com.novelkeyword.event;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;

import com.novelkeyword.io.BookFileReader;
import com.novelkeyword.io.ContentReader;
import com.novelkeyword.io.ContentWriter;
import com.novelkeyword.novel.IDFContent;
import com.novelkeyword.novel.Word;
import com.novelkeyword.novel.WordContent;

public class IDFCreateEvent 
	implements ActionListener {
	private TextField filePathField;
	private int documentCount;
	
	public IDFCreateEvent(TextField filePathField) {
		this.filePathField = filePathField;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		File morphemesFolder = new File(filePathField.getText().toString());
		File [] files = morphemesFolder.listFiles();
		
		IDFContent idfContent = new IDFContent();
		idfContent.setContentCount(files.length);
		
		ContentReader contentReader = new ContentReader();
		for(int i=0; i<files.length; i++) {
			WordContent bookContent = contentReader.read(files[i].getPath());
			for(Word word : bookContent.getWords()) {
				idfContent.addWord(word);
			}//end of bookContent read
			System.out.println( files.length + " / " + (i+1));
		}//end of files

		ContentWriter writer = new ContentWriter("idf\\idf.txt");
		writer.write_idf(idfContent);
		
		JOptionPane.showMessageDialog(null, "결과완료");
	}//end of actionPerformed
}
