package com.novelkeyword.io;

import java.util.LinkedList;
import java.util.List;

public class ContentSplitter {
	
	private List<String> content = null;
	private List<String> dialogueContent = new LinkedList<String>();
	private List<String> nonDialogueContnet = new LinkedList<String>();
	
	public ContentSplitter(List<String> content) {
		this.content = content;
	}
	
	public void splitter() {
		for(String line : content) {
			if ( line.startsWith("“") || line.startsWith("\"") || line.startsWith("「") || line.startsWith("」") ) {
				line = line.replaceAll("“", "");
				line = line.replaceAll("”", "");
				line = line.replaceAll("\"", "");
				line = line.replaceAll("「", "");
				line = line.replaceAll("」", "");
				dialogueContent.add(line);
			}
			else {
				nonDialogueContnet.add(line);
			}
		}
	}
	
	public List<String> getDialogueContent() {
		return dialogueContent;
	}

	public void setDialogueContent(List<String> dialogueContent) {
		this.dialogueContent = dialogueContent;
	}

	public List<String> getNonDialogueContnet() {
		return nonDialogueContnet;
	}

	public void setNonDialogueContnet(List<String> nonDialogueContnet) {
		this.nonDialogueContnet = nonDialogueContnet;
	}

	public static void main(String [] args) {
		BookFileReader reader = new BookFileReader();
		List<String> content = reader.read("C:\\Users\\GunHee\\Desktop\\저널논문\\샘플자료(소설10권)\\견습환자.txt");
		ContentSplitter dialogueSplitter = new ContentSplitter(content);
		dialogueSplitter.splitter();
		List<String> dialogueContent = dialogueSplitter.getDialogueContent();
		List<String> nonDialgueContent = dialogueSplitter.getNonDialogueContnet();
		
		
		for(String line : dialogueContent) {
			System.out.println(line);
		}
		
		/*
		for(String line : nonDialgueContent) {
			System.out.println(line);
		}
		*/
	}
}
