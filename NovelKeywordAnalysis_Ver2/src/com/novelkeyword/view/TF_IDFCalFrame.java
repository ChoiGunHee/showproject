package com.novelkeyword.view;

import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;

import javax.swing.JFrame;

import com.novelkeyword.event.FileSearchEvent;
import com.novelkeyword.event.TF_IDFCalEvent;

public class TF_IDFCalFrame extends JFrame {

	public TF_IDFCalFrame() {
		
		super("TF-IDF 계산");
		this.setSize(500, 500);
		this.setLayout(new GridLayout(5, 1));
		
		//머리말 파일 검색
		Panel headFileSearchpanel = new Panel(new GridLayout(2, 2));
		TextField headWeightField = new TextField();
		TextField headFilePathField = new TextField();
		CustomButton headSearchButton = new CustomButton("Search");
			headSearchButton.addActionListener(new FileSearchEvent(headFilePathField));
		headFileSearchpanel.add(new CustomLable("Head Weight"));
		headFileSearchpanel.add(headWeightField);
		headFileSearchpanel.add(headSearchButton);
		headFileSearchpanel.add(headFilePathField);
		
		//본문 파일 검색
		Panel nonDialogueFileSearchpanel = new Panel(new GridLayout(2, 2));
		TextField nonDialogueFilePathField = new TextField();
		TextField nonDialogueWeightField = new TextField();
		CustomButton nonDialogueSearchButton = new CustomButton("Search");
			nonDialogueSearchButton.addActionListener(new FileSearchEvent(nonDialogueFilePathField));
			nonDialogueFileSearchpanel.add(new CustomLable("NonDialogue Weight"));
			nonDialogueFileSearchpanel.add(nonDialogueWeightField);
			nonDialogueFileSearchpanel.add(nonDialogueSearchButton);
			nonDialogueFileSearchpanel.add(nonDialogueFilePathField);
		
		//대화문 파일 검색
		Panel dialogueFileSearchpanel = new Panel(new GridLayout(2, 2));
		TextField dialogueFilePathField = new TextField();
		TextField dialogueWeightField = new TextField();
		CustomButton dialogueSearchButton = new CustomButton("Search");
			dialogueSearchButton.addActionListener(new FileSearchEvent(dialogueFilePathField));	
			dialogueFileSearchpanel.add(new CustomLable("Dialogue Weight"));
			dialogueFileSearchpanel.add(dialogueWeightField);
			dialogueFileSearchpanel.add(dialogueSearchButton);
			dialogueFileSearchpanel.add(dialogueFilePathField);
			
		//맺음말 파일 검색
		Panel closingFileSearchpanel = new Panel(new GridLayout(2, 2));
		TextField closingFilePathField = new TextField();
		TextField closingWeightField = new TextField();
		CustomButton closingSearchButton = new CustomButton("Search");
			closingSearchButton.addActionListener(new FileSearchEvent(closingFilePathField));	
			closingFileSearchpanel.add(new CustomLable("Closing Weight"));
			closingFileSearchpanel.add(closingWeightField);
			closingFileSearchpanel.add(closingSearchButton);
			closingFileSearchpanel.add(closingFilePathField);

		//계산 버튼
		CustomButton calButton = new CustomButton("Cal");
			calButton.addActionListener(new TF_IDFCalEvent(	headFilePathField,
															nonDialogueFilePathField,
															dialogueFilePathField,
															closingFilePathField,
															headWeightField,
															nonDialogueWeightField,
															dialogueWeightField,
															closingWeightField));
			
		this.add(headFileSearchpanel);
		this.add(nonDialogueFileSearchpanel);
		this.add(dialogueFileSearchpanel);
		this.add(closingFileSearchpanel);
		this.add(calButton);
		
		this.setVisible(true);
	}
	
	public static void main(String [] args) {
		TF_IDFCalFrame testFrame = new TF_IDFCalFrame();
	}
}
