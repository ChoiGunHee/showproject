package com.novelkeyword.view;

import java.awt.Button;
import java.awt.Color;

public class CustomButton extends Button {
	
	public CustomButton(String string) {
		super(string);
		init();
	}
	
	private void init() {
		this.setFont(new MainFont());
		this.setBackground(new Color(50, 100, 100));
		this.setForeground(Color.white);
	}
}
