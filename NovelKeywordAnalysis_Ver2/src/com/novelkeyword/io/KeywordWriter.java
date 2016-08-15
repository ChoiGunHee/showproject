package com.novelkeyword.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.novelkeyword.novel.KeywordContent;
import com.novelkeyword.novel.Word;

public class KeywordWriter {
	private String path;
	
	public KeywordWriter(String path) {
		this.path = path;
	}
	
	public void write(KeywordContent keywordContent) {
		FileWriter writer = null;
		
		Map<String, Word> keywords = keywordContent.getKeywords();
		List<Word> keywordList = keywordContent.getKeywordList();
		try {
		writer = new FileWriter(path);
			writer.write(keywordContent.getNounCount()
						+ " "
						+ keywordContent.getVerbCount()
						+ "\n");
			
			for(Word word : keywordList) {
				writer.write(word.getMorpheme()
								+ " "
								+ word.getTag()
								+ " "
								+ word.getFrequency()
								+ " "
								+ word.getWeight()
								+ "\n");
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
