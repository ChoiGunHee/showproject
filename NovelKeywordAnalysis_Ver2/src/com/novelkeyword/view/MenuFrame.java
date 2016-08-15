package com.novelkeyword.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class MenuFrame extends JFrame 
	implements ActionListener {
	
	private CustomButton dialogueSplitterButton;
	private CustomButton morphemeSplitterButton;
	private CustomButton idfCreateButton;
	private CustomButton tf_idfCalButton;
	private CustomButton total_CalButton;
	private CustomButton morphemeSplitterFolderButton;
	private CustomButton keywordTagCompareButton;
	
	public MenuFrame() {
		super("Menu");
		this.setLayout(new GridLayout(4,2));
		dialogueSplitterButton = new CustomButton("Dialogue Splitter");
		dialogueSplitterButton.addActionListener(this);
		
		morphemeSplitterButton = new CustomButton("morpheme Splitter (Book)");
		morphemeSplitterButton.addActionListener(this);
		
		morphemeSplitterFolderButton = new CustomButton("morpheme Splitter (Folder)");
		morphemeSplitterFolderButton.addActionListener(this);
		
		idfCreateButton = new CustomButton("IDF Create");
		idfCreateButton.addActionListener(this);
		
		tf_idfCalButton = new CustomButton("TF-IDF Cal (Book)");
		tf_idfCalButton.addActionListener(this);
		
		total_CalButton = new CustomButton("Total Cal");
		total_CalButton.addActionListener(this);
		
		keywordTagCompareButton = new CustomButton("Keyword Tag Compare");
		keywordTagCompareButton.addActionListener(this);
		
		this.add(dialogueSplitterButton);
		this.add(morphemeSplitterButton);
		this.add(morphemeSplitterFolderButton);
		this.add(idfCreateButton);
		this.add(tf_idfCalButton);
		this.add(total_CalButton);
		this.add(keywordTagCompareButton);
		
		this.setSize(500, 500);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(dialogueSplitterButton == e.getSource()) {
			ContentSplitterFrame frame = new ContentSplitterFrame();
		}
		else if(morphemeSplitterButton == e.getSource()) {
			MorphemeSplitterFrame frame = new MorphemeSplitterFrame();
		}
		else if(morphemeSplitterFolderButton == e.getSource()) {
			MorphemeSplitterFolderFrame freme = new MorphemeSplitterFolderFrame();
		}
		else if(idfCreateButton == e.getSource()) {
			IDF_CreateFrame frame = new IDF_CreateFrame();
		}
		else if(tf_idfCalButton == e.getSource()) {
			TF_IDFCalFrame frame = new TF_IDFCalFrame();
		}
		else if(total_CalButton == e.getSource()) {
			Total_TFIDFCalFrame frame = new Total_TFIDFCalFrame();
		}
		else if(keywordTagCompareButton == e.getSource()) {
			KeywordTagCompareFrame frame = new KeywordTagCompareFrame();
		}
	}
}
