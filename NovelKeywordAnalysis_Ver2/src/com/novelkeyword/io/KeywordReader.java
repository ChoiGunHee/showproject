package com.novelkeyword.io;

import java.io.FileReader;
import java.util.Collections;
import java.util.List;

import com.novelkeyword.novel.KeywordContent;
import com.novelkeyword.novel.Word;
import com.novelkeyword.view.LineContentFrame;

public class KeywordReader {
	public KeywordReader() {
	}
	
	public KeywordContent read(String path) {
		KeywordContent content = new KeywordContent();
		BookFileReader bookreader = new BookFileReader();
		List<String> contentList = bookreader.read(path);
		
		String bookCountLine = contentList.get(0);
		String [] counts = bookCountLine.split(" ");
		content.setNounCount(Integer.parseInt(counts[0]));
		content.setVerbCount(Integer.parseInt(counts[1]));
		
		for(int i = 1; i<contentList.size(); i++) {
			String contentLine = contentList.get(i);
			String [] wordLine = contentLine.split(" ");
			
			String morpheme = wordLine[0];
			String tag = wordLine[1];
			int frequency = Integer.parseInt(wordLine[2]);
			double weight = Double.parseDouble(wordLine[3]);
			
			Word word = new Word(morpheme, tag, frequency);
			word.setWeight(weight);
			
			content.addKeyword(word);
		}
		return content;
	}
	
	public static void main(String [] args) {
		KeywordReader reader = new KeywordReader();
		BookFileReader bookreader = new BookFileReader();
		KeywordContent content = reader.read("keyword/견습환자.txt");
		List<Word> list = content.keywordList();
		Collections.sort(list);
		LineContentFrame frame = new LineContentFrame("");
		for(Word word : list) {
			frame.setContent(word.toString());
		}
	}
}
