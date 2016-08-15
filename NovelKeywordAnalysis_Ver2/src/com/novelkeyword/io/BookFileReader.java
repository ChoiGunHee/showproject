package com.novelkeyword.io;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class BookFileReader {
	private String path;
	private String fileName;
	private Path bookFilePath;
	
	public BookFileReader() {
	}
	
	public List<String> read(String path) {
		this.path = path;
		bookFilePath = new File(path).toPath();
		fileName = (bookFilePath.getFileName()).toString();
		
		List<String> content = null;
		List<String> resultContent = new LinkedList<String>();
		
		try {
			content = Files.readAllLines(bookFilePath, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String line : content) {
			String resultLine = line.trim();
			if(!line.equals("")) {
				resultContent.add(resultLine);	
			}
			
		}
		
		return resultContent;
	}
	
	public List<String> read() {
		List<String> content = null;
		List<String> resultContent = new LinkedList<String>();
		
		try {
			content = Files.readAllLines(bookFilePath, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(String line : content) {
			String resultLine = line.trim();
			if(!line.equals("")) {
				resultContent.add(resultLine);	
			}
			
		}
		
		return resultContent;
	}
	
	public String getFileName() {
		return fileName.replaceAll(".txt", "");
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static void main(String [] args) {
		//FileReader testReader = new FileReader("C:\\Users\\GunHee\\Desktop\\저널논문\\샘플자료(소설10권)\\견습환자.txt");
		BookFileReader testReader = new BookFileReader();
		
		List<String> content = testReader.read("C:\\Users\\GunHee\\Desktop\\Workspace\\MorphemeAnalysis\\NovelKeywordAnalysis_Ver2\\morpheme\\견습환자_nonDialogue_morpheme.txt");
		
		//System.out.println(content.get(0));
		
		for(String line : content) {
			System.out.println(line);
		}
		
		System.out.println(testReader.getFileName());
	}
}
