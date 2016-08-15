package com.novelkeyword.event;

import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.novelkeyword.io.BookFileReader;
import com.novelkeyword.io.ContentReader;
import com.novelkeyword.io.KeywordWriter;
import com.novelkeyword.novel.IDFContent;
import com.novelkeyword.novel.KeywordContent;
import com.novelkeyword.novel.Word;
import com.novelkeyword.novel.WordContent;
import com.novelkeyword.view.LineContentFrame;

public class TF_IDFCalEvent 
	implements ActionListener {
	private TextField headFilePathField;
	private TextField nonDialogueFilePathField;
	private TextField dialogueFilePathField;
	private TextField closingFilePathField;
	
	private TextField headWeightField;
	private TextField nonDialogueWeightField;
	private TextField dialogueWeightField;
	private TextField closingWeightField;
	
	private ContentReader contentReader;
	private KeywordContent keywordContent;
	
	private double headWeitght;
	private double closingWeight;
	private double dialogueWeight;
	private double nonDialogueWeight;
	
	public TF_IDFCalEvent(	TextField headFilePathField,
							TextField nonDialogueFilePathField,
							TextField dialogueFilePathField,
							TextField closingFilePathField,
							TextField headWeightField, 
							TextField nonDialogueWeightField,
							TextField dialogueWeightField,
							TextField closingWeightField) {
		contentReader = new ContentReader();
		
		this.headFilePathField = headFilePathField ;
		this.nonDialogueFilePathField = nonDialogueFilePathField;
		this.dialogueFilePathField = dialogueFilePathField;
		this.closingFilePathField = closingFilePathField;
		
		this.headWeightField = headWeightField;
		this.nonDialogueWeightField = nonDialogueWeightField;
		this.dialogueWeightField = dialogueWeightField;
		this.closingWeightField = closingWeightField;
	}
	
	private void setWeight() {
		headWeitght = Double.parseDouble(headWeightField.getText());
		closingWeight = Double.parseDouble(closingWeightField.getText());
		dialogueWeight = Double.parseDouble(dialogueWeightField.getText());
		nonDialogueWeight = Double.parseDouble(nonDialogueWeightField.getText());
	}
	
	private void calWeight(String contentPath, double weight) {
		WordContent content = contentReader.read(contentPath);
		keywordContent.keywordCal(content, weight);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		keywordContent = new KeywordContent();
		setWeight();
		
		//머리말 TF-IDF 연산
		if( !headFilePathField.getText().isEmpty() ) {
			calWeight(headFilePathField.getText(), headWeitght);
		} else {
			
		}
		
		//본문 TF-IDF 연산
		if( !nonDialogueFilePathField.getText().isEmpty() ) {
			calWeight(nonDialogueFilePathField.getText(), nonDialogueWeight);
		} else {
			
		}
		
		//대화 TF-IDF 연산
		if( !dialogueFilePathField.getText().isEmpty() ) {
			calWeight(dialogueFilePathField.getText(), dialogueWeight);
		} else {
			
		}
		
		//맺음말 TF-IDF 연산
		if( !closingFilePathField.getText().isEmpty() ) {
			calWeight(closingFilePathField.getText(), closingWeight);
		} else {
			
		}
		
		String bookName = (contentReader.getBookName().split("_") )[0];
		keywordContent.setBookName(bookName);
		
		
		List<Word> list = keywordContent.keywordList();
		LineContentFrame frame = new LineContentFrame("키워드 결과");
		for(Word word : list) {
			frame.setContent(word.toString());
		}
		
		KeywordWriter writer = new KeywordWriter("keyword/" + bookName + ".txt");
		writer.write(keywordContent);
		JOptionPane.showMessageDialog(null, "결과완료");
	}//end of actionPerformed

}
