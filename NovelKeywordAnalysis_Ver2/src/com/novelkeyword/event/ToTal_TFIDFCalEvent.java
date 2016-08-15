package com.novelkeyword.event;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import com.novelkeyword.io.ContentReader;
import com.novelkeyword.io.KeywordWriter;
import com.novelkeyword.novel.KeywordContent;
import com.novelkeyword.novel.Word;
import com.novelkeyword.novel.WordContent;

public class ToTal_TFIDFCalEvent implements ActionListener {
	private String path;
	private double headWight;
	private double closingWight;
	private double nonDialogueWight;
	private double dialogueWight;
	
	private TextField pathField;
	private TextField headWightField;
	private TextField closingWightField;
	private TextField nonDialogueWightField;
	private TextField dialogueWightField;
	
	private KeywordContent keywordContent;
	
	public ToTal_TFIDFCalEvent(	TextField pathField,
								TextField headWightField,
								TextField closingWightField,
								TextField nonDialogueWightField,
								TextField dialogueWightField) {
		this.pathField = pathField;
		this.headWightField = headWightField;
		this.closingWightField = closingWightField;
		this.nonDialogueWightField = nonDialogueWightField;
		this.dialogueWightField = dialogueWightField;
	}
	private void setinit() {
		headWight = Double.parseDouble(headWightField.getText());
		closingWight = Double.parseDouble(closingWightField.getText());
		nonDialogueWight = Double.parseDouble(nonDialogueWightField.getText());
		dialogueWight = Double.parseDouble(dialogueWightField.getText());
		path = pathField.getText();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		setinit();
		
		File folder = new File(path);
		File [] morphemeFiles = folder.listFiles();
		
		ContentReader contentReader = new ContentReader();
		for(int i=0; i<morphemeFiles.length; i++) {
			WordContent content = contentReader.read(morphemeFiles[i].getPath().toString());
			String bookName = (contentReader.getBookName().split("_") )[0];
			String type = (contentReader.getBookName().split("_") )[1];
			
			System.out.println(morphemeFiles[i].getPath().toString());
			System.out.println(bookName);
			System.out.println(type);
			calKeywordContent(bookName, type, content);
		}
	}
	
	private void calKeywordContent(String bookName, String type, WordContent content) {
		if(keywordContent == null) {
			keywordContent = new KeywordContent();
			keywordContent.setBookName(bookName);
		}
		
		if(keywordContent.getBookName().equals(bookName)) {
			double weight = getTypeWeight(type);
			keywordContent.keywordCal(content, weight);
		} else {
			keywordContent.keywordList();
			KeywordWriter writer = new KeywordWriter("keyword/" + keywordContent.getBookName() + ".txt");
			writer.write(keywordContent);
			keywordContent = new KeywordContent();
			keywordContent.setBookName(bookName);
		}
		
		keywordContent.keywordList();
		KeywordWriter writer = new KeywordWriter("keyword/" + keywordContent.getBookName() + ".txt");
		writer.write(keywordContent);
	}
	
	private double getTypeWeight(String type) {
		double weight = 0;
		
		switch (type) {
		case "head" :
			weight = headWight;
			break;
			
		case "closing" :
			weight = closingWight;
			break;
			
		case "dialogue" :
			weight = dialogueWight;
			break;
			
		case "nonDialogue" :
			weight = nonDialogueWight;
			break;
		}
		
		return weight;
	}
}
