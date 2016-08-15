package com.novelkeyword.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;

import javax.swing.JFrame;

import com.novelkeyword.event.FolderSearchEvent;
import com.novelkeyword.event.IDFCreateEvent;
import com.novelkeyword.event.MorphemeFilesCreateEvent;

public class MorphemeSplitterFolderFrame extends JFrame {
	
	public MorphemeSplitterFolderFrame() {
		super("IDF 계산");
		this.setLayout(new BorderLayout());
		
		//fileSearch Panel
		Panel fileSearchpanel = new Panel(new GridLayout(1, 3));
		TextField filePathField = new TextField();
		CustomButton searchButton = new CustomButton("Search");
			searchButton.addActionListener(new FolderSearchEvent(filePathField));	
		fileSearchpanel.add(filePathField);
		fileSearchpanel.add(searchButton);
		
		//Button Panel
		Panel buttonPanel = new Panel(new GridLayout());
		
		//readButton
		CustomButton readButton = new CustomButton("Morpheme File Create");
			readButton.addActionListener(new MorphemeFilesCreateEvent(filePathField));
			buttonPanel.add(readButton);
			
		this.add(fileSearchpanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.CENTER);
		
		this.setSize(600, 200);
		this.setVisible(true);
	}
	
	public static void main(String [] args) {
		MorphemeSplitterFolderFrame frame = new MorphemeSplitterFolderFrame(); 
	}
}
