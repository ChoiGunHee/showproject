package com.novelkeyword.event;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.novelkeyword.io.KeywordReader;
import com.novelkeyword.novel.KeywordContent;
import com.novelkeyword.novel.Word;
import com.novelkeyword.view.LineContentFrame;

public class KeywordTagCompareEvent 
	implements ActionListener {

	private TextField tagTextField;
	private TextField filePathField;
	
	public KeywordTagCompareEvent(TextField tagTextField,
									TextField filePathField) {
		this.tagTextField = tagTextField;
		this.filePathField = filePathField;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		KeywordReader reader = new KeywordReader();
		KeywordContent content = reader.read(filePathField.getText().toString());
		List<Word> list = content.keywordList();
		
		LineContentFrame frame = new LineContentFrame("");
		String compareTag = tagTextField.getText().toString();
		for(Word word : list) {
			if(word.getTag().startsWith(compareTag))
				frame.setContent(word.toString());
		}
	}

}
