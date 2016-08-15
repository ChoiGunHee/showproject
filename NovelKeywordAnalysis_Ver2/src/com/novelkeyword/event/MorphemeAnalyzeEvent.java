package com.novelkeyword.event;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.lf5.viewer.configure.MRUFileManager;

import com.novelkeyword.io.BookFileReader;
import com.novelkeyword.io.MorphemeSplitter;
import com.novelkeyword.novel.WordContent;
import com.novelkeyword.view.LineContentFrame;

public class MorphemeAnalyzeEvent 
	implements ActionListener{
	private TextField filePathField;
	
	public MorphemeAnalyzeEvent(TextField filePathField) {
		this.filePathField = filePathField;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BookFileReader reader = new BookFileReader();
		List<String> content = reader.read(filePathField.getText().toString());
		
		MorphemeSplitter splitter = new MorphemeSplitter(content, reader.getFileName());
		WordContent wordContent = splitter.splitter();
		
		LineContentFrame morphemeCotent = new LineContentFrame("형태소 분석 결과");
		morphemeCotent.setContent(wordContent.getMorphemes());
		JOptionPane.showMessageDialog(null, "결과완료");
	}

}
