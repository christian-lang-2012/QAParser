import Models.*;
import PhraseChunking.*;
import PoSTagging.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 4/28/14
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestApp {

	public static void main(String[] args){
		PartOfSpeechTagger tagger = new PartOfSpeechTagger();
		tagger.parseSentence("Verify that if a user has received a survey, but canceled/closed without completing, the user doesn't get another survey for at least 30 days");
		List<WordTagPair> taggedWords = tagger.getTaggedWords();

		System.out.println("----------Part of Speech Tags----------");
		for(WordTagPair pair : taggedWords)
			System.out.println(pair);

		System.out.println("----------Noun Phrases----------");
		PhraseChunker pChunker = new PhraseChunker(taggedWords);
		for(String np : pChunker.getNounPhrases())
			System.out.println(np);

        System.out.println("----------Verb Phrases----------");
        for(String vp : pChunker.getVerbPhrases())
            System.out.println(vp);
    }
}
