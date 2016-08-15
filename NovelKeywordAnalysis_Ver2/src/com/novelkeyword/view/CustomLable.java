package com.novelkeyword.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;

public class CustomLable extends Label {
	public CustomLable(String string) {
		super(string);
		init();
	}
	
	private void init() {
		this.setAlignment(Label.CENTER);
		this.setBackground(new Color(50, 50, 100));
		this.setFont(new MainFont());
		this.setForeground(Color.WHITE);
	}
}
