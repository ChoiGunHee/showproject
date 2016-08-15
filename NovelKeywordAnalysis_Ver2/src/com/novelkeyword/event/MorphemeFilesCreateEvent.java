package com.novelkeyword.event;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;

import com.novelkeyword.io.BookFileReader;
import com.novelkeyword.io.ContentWriter;
import com.novelkeyword.io.MorphemeSplitter;
import com.novelkeyword.novel.WordContent;

public class MorphemeFilesCreateEvent implements ActionListener {

	private TextField filePathField;

	public MorphemeFilesCreateEvent(TextField filePathField) {
		this.filePathField = filePathField;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String path = filePathField.getText().toString();
		File bookFolder = new File(path);
		File[] books = bookFolder.listFiles();

		MorphemeSplitter splitter;
		BookFileReader bookReader = new BookFileReader();
		ContentWriter writer;

		for (int i = 0; i < books.length; i++) {
			List<String> contentLine = bookReader.read(books[i].getPath());
			splitter = new MorphemeSplitter(contentLine, bookReader.getFileName());
			WordContent content = splitter.splitter();
			writer = new ContentWriter("morpheme/" + bookReader.getFileName()
					+ "_morpheme.txt");
			writer.write(content);
			System.out.println( books.length + " / " + (i+1));
		}
		
		JOptionPane.showMessageDialog(null, "결과완료");
	}

}
