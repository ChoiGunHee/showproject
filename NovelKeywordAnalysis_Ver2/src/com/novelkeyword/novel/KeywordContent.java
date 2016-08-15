package com.novelkeyword.novel;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.novelkeyword.io.BookFileReader;
import com.novelkeyword.io.ContentReader;

public class KeywordContent {
	private Map<String, Word> keywords = new HashMap<String, Word>();
	private IDFContent idfContent;
	private int idfDocCount;
	private int nounCount;
	private int verbCount;
	private String bookName;
	private List<Word> keywordList;
	
	public KeywordContent() {
		ContentReader contentReader = new ContentReader();
		BookFileReader bookReader = new BookFileReader();
		idfContent = contentReader.read_idf(bookReader.read("idf/idf.txt"));
		idfDocCount = idfContent.getContentCount();
	}
	
	public void keywordCal(WordContent content, double weightNumber) {
		Map<String, Word> idfContentMap = idfContent.getIdfMorphemes();
		Map<String, Word> wordContentMap = content.getMorphemes();
		int totalWordContent = content.getMorphemesCount();
		for(String key : content.getMorphemes().keySet()) {
			Word word = wordContentMap.get(key);
			Word idfWord = idfContentMap.get(key);
			
			double tf = word.getFrequency() / (double) totalWordContent;
			double idf = Math.abs(( Math.log( idfDocCount / (double)idfWord.getFrequency() ) ));
			word.setWeight( tf * idf * weightNumber );
			
			addKeyword(word);
		}
	}
	
	public void addKeyword(Word word) {
		if( keywords.containsKey(word.getMorpheme())) {
			Word keyword = keywords.get(word.getMorpheme());
			keyword.addWeight(word.getWeight());
		} else {
			keywords.put(word.getMorpheme(), word);
		}
	}
	
	public List<Word> keywordList() {
		keywordList = new LinkedList<Word>();
		for(String key : keywords.keySet()) {
			Word word = keywords.get(key);
			keywordList.add(word);
		}
		
		Collections.sort(keywordList);
		
		noun_verbRate();
		return keywordList;
	}
	
	public void noun_verbRate() {
		for(String key : keywords.keySet()) {
			Word word = keywords.get(key);
			if(word.getTag().startsWith("n"))
				nounCount += word.getFrequency();
			else
				verbCount += word.getFrequency();
		}
		
		double nounRate = (double)nounCount / (nounCount + verbCount);
		double verbRate = (double)verbCount / (nounCount + verbCount);
		
		System.out.println("noun : " + nounCount + " verb : " + verbCount);
		System.out.println("nounRate : " + nounRate + " verbRate : " + verbRate);
	}

	public int getIdfDocCount() {
		return idfDocCount;
	}

	public void setIdfDocCount(int idfDocCount) {
		this.idfDocCount = idfDocCount;
	}

	public Map<String, Word> getKeywords() {
		return keywords;
	}

	public int getNounCount() {
		return nounCount;
	}

	public int getVerbCount() {
		return verbCount;
	}
	
	public void setNounCount(int nounCount) {
		this.nounCount = nounCount;
	}

	public void setVerbCount(int verbCount) {
		this.verbCount = verbCount;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public List<Word> getKeywordList() {
		return keywordList;
	}
	
}
