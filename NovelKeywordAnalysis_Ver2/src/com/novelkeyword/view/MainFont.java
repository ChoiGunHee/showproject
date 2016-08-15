package com.novelkeyword.view;

import java.awt.Font;

public class MainFont extends Font {

	public MainFont(String name, int style, int size) {
		super(name, style, size);
	}
	
	public MainFont() {
		super(Font.DIALOG, Font.BOLD, 17);
	}
}
