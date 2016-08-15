package com.novelkeyword.view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;

import javax.swing.JFrame;

import com.novelkeyword.event.FileSearchEvent;
import com.novelkeyword.event.FolderSearchEvent;
import com.novelkeyword.event.IDFCreateEvent;

public class IDF_CreateFrame extends JFrame {

	public IDF_CreateFrame() {
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
		CustomButton readButton = new CustomButton("IDF Crate");
			readButton.addActionListener(new IDFCreateEvent(filePathField));
			buttonPanel.add(readButton);
			
		this.add(fileSearchpanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.CENTER);
		
		this.setSize(600, 200);
		this.setVisible(true);
	}
	
	public static void main(String [] args) {
		IDF_CreateFrame testFrame = new IDF_CreateFrame();
	}
}
