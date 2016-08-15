/**
 * @version 1.0
 * @author 박 진수
 * @since 2014.10.23 목요일
 * @see 한나눔 형태소 분석기 생성
 * 
 * @amender Choi GunHee
 * @since 2014. 10. 28. 화요일
 * @see 선행 작업 결과물을 이 프로젝트에 통합.
 * 
 * @ought
 * 향후 더 개선된 workflow 선별
 * 형태소 사전 단어 추가 및 삭제
 */

package com.novelkeyword.analyzer;

import java.util.LinkedList;

import kr.ac.kaist.swrc.jhannanum.comm.Eojeol;
import kr.ac.kaist.swrc.jhannanum.comm.Sentence;
import kr.ac.kaist.swrc.jhannanum.exception.ResultTypeException;
import kr.ac.kaist.swrc.jhannanum.hannanum.Workflow;
import kr.ac.kaist.swrc.jhannanum.plugin.MajorPlugin.MorphAnalyzer.ChartMorphAnalyzer.ChartMorphAnalyzer;
import kr.ac.kaist.swrc.jhannanum.plugin.MajorPlugin.PosTagger.HmmPosTagger.HMMTagger;
import kr.ac.kaist.swrc.jhannanum.plugin.SupplementPlugin.PlainTextProcessor.InformalSentenceFilter.InformalSentenceFilter;
import kr.ac.kaist.swrc.jhannanum.plugin.SupplementPlugin.PlainTextProcessor.SentenceSegmentor.SentenceSegmentor;

public class HannanumMorphAnalyzer {
	private Workflow workflow = new Workflow();
	private ParseStrategy parseStrategy;
	
	public HannanumMorphAnalyzer() {
		super();
		setPlugin();
	}
	
	public void setParseStrategy(ParseStrategy parseStrategy) {
		this.parseStrategy = parseStrategy;
	}
	
	//workflow 설정
	public void setPlugin(){
		workflow.appendPlainTextProcessor(new SentenceSegmentor(), null);
		workflow.appendPlainTextProcessor(new InformalSentenceFilter(), null);

		workflow.setMorphAnalyzer(new ChartMorphAnalyzer(),
				"conf/plugin/MajorPlugin/MorphAnalyzer/ChartMorphAnalyzer.json");

		workflow.setPosTagger(new HMMTagger(),
				"conf/plugin/MajorPlugin/PosTagger/HmmPosTagger.json");
		workflow.appendPosProcessor(new NounKeywordExtractor(), null);
		
		try {
			workflow.activateWorkflow(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//workflow 종료
	public void endProcess(){
		workflow.close();
	}
	
	//문장단위 분석
	public void analyze(String sentance) {
		Eojeol[] eojeolArray = null;
		
		try {
			workflow.analyze(sentance);
			LinkedList<Sentence> resultList = null;
			try {
				resultList = workflow.getResultOfDocument(new Sentence(0, 0,true));
				
				for (Sentence s : resultList) {
					eojeolArray = s.getEojeols();
					for (int i = 0; i < eojeolArray.length; i++) {
						if (eojeolArray[i].length > 0) {
							String[] morphemes = eojeolArray[i].getMorphemes();
							String[] tags = eojeolArray[i].getTags();
							
							parseStrategy.ResultProcess(morphemes, tags);
						}
					}
				}
			} catch (ResultTypeException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
