package com.novelkeyword.novel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WordContent {
	private Map<String, Word> morphemes = new HashMap<String, Word>();
	private String bookName;
	
	public WordContent() {
		
	}
	
	public WordContent(String bookName) {
		this.bookName = bookName;
	}
	
	public void addMorpheme(String Morpheme, String tag) {
		if(morphemes.containsKey(Morpheme)){
			Word word = morphemes.get(Morpheme);
			word.upFrequency();
		}else{
			Word word = new Word(Morpheme, tag);
			morphemes.put(Morpheme, word);
		}
	}
	
	public void addMorpheme(Word word) {
		morphemes.put(word.getMorpheme(), word);
	}
	
	public void printMorphemes() {
		morphemes.keySet();
		for(String word : morphemes.keySet()) {
			System.out.println( morphemes.get(word) );
		}
	}
	
	public Map<String, Word> getMorphemes() {
		return morphemes;
	}

	public void setMorphemes(Map<String, Word> morphemes) {
		this.morphemes = morphemes;
	}

	public String getBookName() {
		return bookName;
	}

	public int getMorphemesCount() {
		morphemes.keySet();
		int total = 0;
		for(String word : morphemes.keySet()) {
			total += morphemes.get(word).getFrequency();
		}
		return total;
	}
	
	public List<Word> getWords() {
		List<Word> resultWords = new LinkedList<Word>();
		morphemes.keySet();
		int total = 0;
		for(String word : morphemes.keySet()) {
			resultWords.add(morphemes.get(word));
		}
		return resultWords;
	}
}
