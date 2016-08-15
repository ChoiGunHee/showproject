/**
 * @version 1.0
 * @author 박 진수
 * @since 2014.10.23 목요일
 * @see 단어를 각 챕터 단어맵에 추가.
 * 
 * @amender Choi GunHee
 * @since 2014. 10. 28. 화요일
 * @see 
 * 선행 작업 결과물을 이 프로젝트에 통합.
 */
package com.novelkeyword.analyzer;

import com.novelkeyword.novel.WordContent;

public class NovelParseStrategy implements ParseStrategy{
	
	private WordContent wordContent;
	
	public NovelParseStrategy(WordContent wordContent) {
		super();
		this.wordContent = wordContent;
	}
	
	@Override
	public void ResultProcess(String[] morphemes, String[] tags) {
		for (int j = 0; j < morphemes.length; j++) {
			//System.out.println(morphemes[j]);
			wordContent.addMorpheme(morphemes[j], tags[j]);
		}
	}
}
