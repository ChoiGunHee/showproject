package com.novelkeyword.view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;

import javax.swing.JFrame;

import com.novelkeyword.event.FileReadEvent;
import com.novelkeyword.event.FileSearchEvent;
import com.novelkeyword.event.FileWriteEvent;

public class ContentSplitterFrame extends JFrame {
	
	public ContentSplitterFrame() {
		super("소설 키워드 추출");
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
		
		//readButton
		CustomButton readButton = new CustomButton("Read");
			readButton.addActionListener(new FileReadEvent(filePathField));
			buttonPanel.add(readButton);
			
		//writeButton
		CustomButton writeButton = new CustomButton("Save");
			writeButton.addActionListener(new FileWriteEvent(filePathField));
			buttonPanel.add(writeButton);
			
		this.add(fileSearchpanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.CENTER);
		
		this.setSize(600, 200);
		this.setVisible(true);
	}
	
	public static void main(String [] args) {
		ContentSplitterFrame mainFrame = new ContentSplitterFrame();
	}
}