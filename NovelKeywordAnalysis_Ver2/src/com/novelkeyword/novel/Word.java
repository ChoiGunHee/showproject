package com.novelkeyword.novel;

public class Word 
	implements Comparable<Word> {

	private String morpheme;
	private String tag;
	private int frequency;
	private double weight;
	
	public Word(String morpheme, String tag) {
		this.morpheme = morpheme;
		this.tag = tag;
		frequency = 1;
		weight = 0;
	}
	
	public Word(String morpheme, String tag, int frequency) {
		this.morpheme = morpheme;
		this.tag = tag;
		this.frequency = frequency;
	}

	public void upFrequency() {
		frequency++;
	}
	
	@Override
	public String toString() {
		return "Word [morpheme=" + morpheme + ", tag=" + tag + ", frequency="
				+ frequency + ", weight=" + weight + "]";
	}

	public String getMorpheme() {
		return morpheme;
	}

	public void setMorpheme(String morpheme) {
		this.morpheme = morpheme;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(Word word) {
		double result = word.getWeight() - this.getWeight();
		
		if(result > 0)
			return 1;
		else if(result < 0)
			return -1;
		else
			return 0;
	}

	public void addWeight(double weight) {
		this.weight += weight;
	}
	
}
