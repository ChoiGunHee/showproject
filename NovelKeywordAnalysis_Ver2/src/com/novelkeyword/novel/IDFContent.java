package com.novelkeyword.novel;

import java.util.HashMap;
import java.util.Map;

public class IDFContent {
	private int docCount;
	private Map<String, Word> idfMorphemes = new HashMap<String, Word>();
	
	public IDFContent() {
		docCount = 1;
	}
	
	public void addWord(Word key) {
		if(idfMorphemes.containsKey(key.getMorpheme())) {
			Word word = idfMorphemes.get(key.getMorpheme());
			word.upFrequency();
		}else {
			Word word = new Word(key.getMorpheme(), key.getTag(), key.getFrequency());
			idfMorphemes.put(key.getMorpheme(), word);
		}
	}
	
	public void printMorphemes() {
		idfMorphemes.keySet();
		
		for(String word : idfMorphemes.keySet()) {
			System.out.println( idfMorphemes.get(word) );
		}
		System.out.println(docCount);
	}

	public int getContentCount() {
		return docCount;
	}
	
	public void setContentCount(int count) {
		this.docCount = count;
	}
	
	public Map<String, Word> getIdfMorphemes() {
		return idfMorphemes;
	}
}
