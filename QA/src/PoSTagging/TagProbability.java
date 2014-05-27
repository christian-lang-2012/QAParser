package PoSTagging;

import Models.*;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 4/30/14
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class TagProbability {
	private PartOfSpeechTag tag;
	private float probability;
	private String word;

	public TagProbability(PartOfSpeechTag tag, float probability, String word){
		this.tag = tag;
		this.probability = probability;
		this.word = word;
	}

	public PartOfSpeechTag getTag(){
		return tag;
	}

	public float getProbability(){
		return probability;
	}

	public String getWord(){
		return word;
	}
}
