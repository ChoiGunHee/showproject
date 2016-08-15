package com.novelkeyword.io;

import java.util.List;
import java.util.Map;

import com.novelkeyword.novel.IDFContent;
import com.novelkeyword.novel.Word;
import com.novelkeyword.novel.WordContent;
import com.novelkeyword.view.LineContentFrame;

public class ContentReader {
	private String bookName;
	
	private List<String> readContentList(String path) {
		BookFileReader bookReader = new BookFileReader();
		List<String> content = bookReader.read(path);
		bookName = bookReader.getFileName();
		return content;
	}
	
	public WordContent read(String fileName, List<String> content) {
		WordContent bookContent = new WordContent(fileName);
		for(String line : content) {
			String [] lineSplit = line.split(" ");
			
			String morpheme = lineSplit[0];
			String tag = lineSplit[1];
			int frequency = Integer.parseInt(lineSplit[2]);
			
			Word word = new Word(morpheme, tag, frequency);
			bookContent.addMorpheme(word);
		}//end of bookContent read
		
		return bookContent;
	}
	
	public WordContent read(String path) {
		List<String> content = readContentList(path);
		WordContent bookContent = new WordContent(bookName);
		for(String line : content) {
			String [] lineSplit = line.split(" ");
			
			String morpheme = lineSplit[0];
			String tag = lineSplit[1];
			int frequency = Integer.parseInt(lineSplit[2]);
			
			Word word = new Word(morpheme, tag, frequency);
			bookContent.addMorpheme(word);
		}//end of bookContent read
		
		return bookContent;
	}
	
	public IDFContent read_idf(List<String> content) {
		IDFContent idfContent = new IDFContent();
		for(String line : content) {
			try {
				String [] lineSplit = line.split(" ");
				
				String morpheme = lineSplit[0];
				String tag = lineSplit[1];
				int frequency = Integer.parseInt(lineSplit[2]);

				Word word = new Word(morpheme, tag, frequency);
				idfContent.addWord(word);
			} catch (Exception e) {
				int idfDocCount = Integer.parseInt(line);
				idfContent.setContentCount(idfDocCount);
			}
		}
		
		return idfContent;
	}
	
	public IDFContent read_idf(String path) {
		List<String> content = readContentList(path);
		IDFContent idfContent = new IDFContent();
		for(String line : content) {
			try {
				String [] lineSplit = line.split(" ");
				
				String morpheme = lineSplit[0];
				String tag = lineSplit[1];
				int frequency = Integer.parseInt(lineSplit[2]);

				Word word = new Word(morpheme, tag, frequency);
				idfContent.addWord(word);
			} catch (Exception e) {
				int idfDocCount = Integer.parseInt(line);
				idfContent.setContentCount(idfDocCount);
			}
		}
		
		return idfContent;
	}
	
	
	public String getBookName() {
		return bookName;
	}

	public static void main(String [] args) {
		ContentReader reader = new ContentReader();
		WordContent content= reader.read("morpheme/견습환자_nonDialogue_morpheme.txt");
		//content.printMorphemes();
		Map<String, Word> map = content.getMorphemes();
		LineContentFrame frame = new LineContentFrame("");
		frame.setContent(map);
		System.out.println(content.getBookName());
	}
}
