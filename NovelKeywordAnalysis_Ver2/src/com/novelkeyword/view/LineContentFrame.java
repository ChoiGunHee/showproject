package com.novelkeyword.view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.TextArea;
import java.awt.TextField;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import com.novelkeyword.novel.Word;

public class LineContentFrame extends JFrame {
	
	private TextArea field;
	private String bookName;
	
	public LineContentFrame(String name) {
		super(name);
		
		this.setLayout(new BorderLayout());
		this.setSize(800, 1000);
		
		field = new TextArea();
		
		this.add(field, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	public LineContentFrame(String name, String bookName) {
		super(name);
		
		this.setLayout(new BorderLayout());
		this.setSize(800, 1000);
		
		field = new TextArea();
		this.bookName = bookName;
		
		this.add(field, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	public LineContentFrame(List<String> content, String name) {
		this(name);
	}
	
	public void setContent(List<String> content) {
		for(String line : content) {
			field.append(line + "\n");
		}
	}
	
	public void setContent(Map<String, Word> content) {
		for(String key : content.keySet()) {
			field.append(content.get(key) + "\n");
		}
	}
	
	public void setContent(String string) {
		field.append(string + "\n");
	}
	public static void main(String [] args) {
		LineContentFrame test = new LineContentFrame("tset");
	}


}
