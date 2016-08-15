package com.novelkeyword.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;

import javax.swing.JFrame;

import com.novelkeyword.event.FileReadEvent;
import com.novelkeyword.event.FileSearchEvent;
import com.novelkeyword.event.FileWriteEvent;
import com.novelkeyword.event.KeywordTagCompareEvent;

public class KeywordTagCompareFrame extends JFrame {
	
	public KeywordTagCompareFrame() {
		super("Keyword Teg Compare");
		this.setLayout(new BorderLayout());
		
		//fileSearch Panel
		Panel fileSearchpanel = new Panel(new GridLayout(1, 3));
		TextField filePathField = new TextField();
		CustomButton searchButton = new CustomButton("Search");
			searchButton.addActionListener(new FileSearchEvent(filePathField));	
		fileSearchpanel.add(filePathField);
		fileSearchpanel.add(searchButton);
		
		//Button Panel
		Panel buttonPanel = new Panel(new GridLayout());
		
		//tagText
		TextField tagTextField = new TextField();
		buttonPanel.add(tagTextField);
			
		//writeButton
		CustomButton compareButton = new CustomButton("Compare");
			compareButton.addActionListener(new KeywordTagCompareEvent(tagTextField, filePathField));
			buttonPanel.add(compareButton);
			
		this.add(fileSearchpanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.CENTER);
		
		this.setSize(600, 200);
		this.setVisible(true);
	}
	
	public static void main(String [] args) {
		KeywordTagCompareFrame frame = new KeywordTagCompareFrame();
	}
}
