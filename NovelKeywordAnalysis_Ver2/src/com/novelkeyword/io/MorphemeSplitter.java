package com.novelkeyword.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.novelkeyword.analyzer.HannanumMorphAnalyzer;
import com.novelkeyword.analyzer.NovelParseStrategy;
import com.novelkeyword.novel.Word;
import com.novelkeyword.novel.WordContent;

public class MorphemeSplitter {
	private Map<String, Word> morphemes = null;
	private WordContent morphemesContent;
	private List<String> content;
	
	public MorphemeSplitter() {
		
	}
	
	public MorphemeSplitter(List<String> content) {
		this.content = content;
		morphemesContent = new WordContent();
	}
	
	public MorphemeSplitter(List<String> content, String bookName) {
		this.content = content;
		morphemesContent = new WordContent(bookName);
	}
	
	public WordContent splitter() {
		HannanumMorphAnalyzer analyzer = new HannanumMorphAnalyzer();
		analyzer.setParseStrategy(new NovelParseStrategy(morphemesContent));
		
		for(String line : content) {
			analyzer.analyze(line);
		}
		
		analyzer.endProcess();
		return morphemesContent;
	}
	public static void main(String [] args) {
		BookFileReader reader = new BookFileReader();
		List<String> content = reader.read("book/견습환자.txt_dialogue.txt");
		
		MorphemeSplitter testSplitter = new MorphemeSplitter(content);
		WordContent testWordContent = testSplitter.splitter();
		testWordContent.printMorphemes();
	}
}
