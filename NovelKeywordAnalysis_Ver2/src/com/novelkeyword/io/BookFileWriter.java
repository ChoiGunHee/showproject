package com.novelkeyword.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BookFileWriter {
	
	private String path;
	
	public BookFileWriter(String path) {
		this.path = path;
	}
	
	public void write(List<String> content) {
		FileWriter writer;
		
		try {
			writer = new FileWriter(path);
			for(String line : content) {
				writer.write(line + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String [] args) {
		BookFileReader reader = new BookFileReader();
		List<String> content = reader.read("morpheme/견습환자_dialogue_morpheme.txt");
		
		BookFileWriter writer = new BookFileWriter("test/test.txt");
		writer.write(content);
	}
}
