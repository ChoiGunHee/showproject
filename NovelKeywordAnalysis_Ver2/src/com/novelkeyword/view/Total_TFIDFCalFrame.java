package com.novelkeyword.view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;

import javax.swing.JFrame;

import com.novelkeyword.event.FolderSearchEvent;
import com.novelkeyword.event.ToTal_TFIDFCalEvent;

public class Total_TFIDFCalFrame extends JFrame {
	
	public Total_TFIDFCalFrame() {
		this.setSize(800, 200);
		this.setLayout(new BorderLayout());
		
		Panel searchPanel = new Panel(new GridLayout(1, 1));
			TextField pathField = new TextField();
			CustomButton searchButton = new CustomButton("Search");
			searchButton.addActionListener(new FolderSearchEvent(pathField));
		searchPanel.add(pathField);
		searchPanel.add(searchButton);
		
		Panel weightPanel = new Panel(new GridLayout(2, 4, 1, 1));
			weightPanel.add(new CustomLable("Head Weight"));
			weightPanel.add(new CustomLable("Closing Weight"));
			weightPanel.add(new CustomLable("NonDialogue Weight"));
			weightPanel.add(new CustomLable("Dialogue Weight"));
			TextField headWightField = new TextField();
			TextField closingWightField = new TextField();
			TextField nonDialogueWightField = new TextField();
			TextField dialogueWightField = new TextField();
			weightPanel.add(headWightField);
			weightPanel.add(closingWightField);
			weightPanel.add(nonDialogueWightField);
			weightPanel.add(dialogueWightField);
		
			CustomButton calButton = new CustomButton("Cal");
		calButton.addActionListener(new ToTal_TFIDFCalEvent(pathField,
															headWightField,
															closingWightField,
															nonDialogueWightField,
															dialogueWightField));
		
		this.add(searchPanel, BorderLayout.NORTH);
		this.add(weightPanel,BorderLayout.CENTER);
		this.add(calButton, BorderLayout.SOUTH);
		this.setVisible(true);
	}
	
	public static void main(String [] args) {
		Total_TFIDFCalFrame frame = new Total_TFIDFCalFrame();
	}
	
}
