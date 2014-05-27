package PhraseChunking;

import Models.*;

import java.util.*;
import java.util.regex.*;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/8/14
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PhraseChunker {
	private List<WordTagPair> taggedWords;
	private List<String> nounPhrases;
    private List<String> verbPhrases;
    private int start = 0;

	public PhraseChunker(List<WordTagPair> taggedWords){
	  	this.taggedWords = taggedWords;
		nounPhrases = new ArrayList<String>();
        verbPhrases = new ArrayList<String>();
		findNounPhrases();
        findVerbPhrases();
	}

	public List<String> getNounPhrases(){
		return nounPhrases;
	}

    public List<String> getVerbPhrases(){
        return verbPhrases;
    }

	private void findNounPhrases(){
		String tags = "";
		for(WordTagPair pair : taggedWords)
			tags += "<"+pair.getTag().name()+">";
		for(String pattern : NPGrammar.getPatterns()){
            start = 0;
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(tags);
			while(m.find())
				parsePhrases(m.group(), "noun");
		}
	}

    private void findVerbPhrases(){
        String tags = "";
        for(WordTagPair pair : taggedWords)
            tags += "<"+pair.getTag().name()+">";
        for(String pattern : VPGrammar.getPatterns()){
            start = 0;
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(tags);
            while(m.find())
                parsePhrases(m.group(), "verb");
        }
    }

	private void parsePhrases(String match, String type){
		List<WordTagPair> wordsToAdd;

		String[] tags = match.replace("<","").replace(">"," ").split(" +");
		boolean foundMatch;

		do{
			foundMatch = true;
			wordsToAdd = new ArrayList<WordTagPair>();
			start = getIndexOfTagInPhrases(start, tags[0]);
			for(int i = start; i < (start+tags.length); i++){
				wordsToAdd.add(taggedWords.get(i));
			}

			for(int i = 0; i < tags.length; i++){
				if(!wordsToAdd.get(i).getTag().name().equals(tags[i]))
					foundMatch = false;
			}
			start+=1;
		}while(!foundMatch);

		String phrase = "";

		for(WordTagPair pair : wordsToAdd)
			phrase += pair.getWord()+" ";
        if(type.equals("noun"))
		    addNounPhrase(phrase.trim());
        else if(type.equals("verb"))
            addVerbPhrase(phrase.trim());
	}

	private int getIndexOfTagInPhrases(int startIndex, String tag){
		for(int i = startIndex; i < taggedWords.size(); i++){
			if(taggedWords.get(i).getTag().name().equals(tag)){
				return i;
			}
		}
		return -1;
	}

    private void addNounPhrase(String nounPhrase){
        List<String> toRemove = new ArrayList<String>();
        boolean add = true;
        for(int i = 0; i < nounPhrases.size(); i++){
            if(nounPhrases.get(i).contains(nounPhrase))
                add = false;
            else if(nounPhrase.contains(nounPhrases.get(i)))
                toRemove.add(nounPhrases.get(i));
        }
        if(add){
            for(String remove : toRemove)
                nounPhrases.remove(remove);
            nounPhrases.add(nounPhrase);
        }
    }

    private void addVerbPhrase(String verbPhrase){
        List<String> toRemove = new ArrayList<String>();
        boolean add = true;
        for(int i = 0; i < verbPhrases.size(); i++){
            if(verbPhrases.get(i).contains(verbPhrase))
                add = false;
            else if(verbPhrase.contains(verbPhrases.get(i)))
                toRemove.add(verbPhrases.get(i));
        }
        if(add){
            for(String remove : toRemove)
                verbPhrases.remove(remove);
            verbPhrases.add(verbPhrase);
        }
    }
}
