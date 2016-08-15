/**
 * @version 1.0
 * @author 박 진수
 * @since 2014.10.23 목요일
 * @see ParseStrategy 인터페이스
 * 
 * @amender Choi GunHee
 * @since 2014. 10. 28. 화요일
 * @see 
 * 선행 작업 결과물을 이 프로젝트에 통합.
 */

package com.novelkeyword.analyzer;

public interface ParseStrategy {
	public void ResultProcess(String[] morphemes, String[] tags);
}
