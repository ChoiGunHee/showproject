package com.novelkeyword.io;

import java.io.FileWriter;
import java.io.IOException;

import com.novelkeyword.novel.IDFContent;
import com.novelkeyword.novel.Word;
import com.novelkeyword.novel.WordContent;

public class ContentWriter {
	private String path;
	
	public ContentWriter(String path) {
		this.path = path;
	}
	
	public void write(WordContent content) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(path);
			
			for(String key : content.getMorphemes().keySet()) {
				Word word = content.getMorphemes().get(key);
				writer.write(word.getMorpheme()
								+ " "
								+ word.getTag()
								+ " "
								+ word.getFrequency()
								+"\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void write_idf(IDFContent content) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(path);
			
			for(String key : content.getIdfMorphemes().keySet()) {
				Word word = content.getIdfMorphemes().get(key);
				writer.write(word.getMorpheme()
								+ " "
								+ word.getTag()
								+ " "
								+ word.getFrequency()
								+"\n");
			}
			writer.write(content.getContentCount()+"");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
