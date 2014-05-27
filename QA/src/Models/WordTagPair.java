package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/5/14
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class WordTagPair {
	private String word;
	private PartOfSpeechTag tag;

	public WordTagPair(String word, PartOfSpeechTag tag){
		this.word = word;
		this.tag = tag;
	}

	public String getWord(){
		return word;
	}

	public PartOfSpeechTag getTag(){
		return tag;
	}

	@Override
	public String toString(){
		String result = word + ": " + tag;
		return result;
	}
}
