import Models.*;
import PhraseChunking.*;
import PoSTagging.*;
import commonIO.HTMLTag;
import commonIO.ParseHTML;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 4/28/14
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


        try {
            URL url = new URL("https://hub.attask.com/report/view?ID=510c18670013957025d385566ba21941");
            InputStream is = url.openStream();

            ParseHTML htmlParser = new ParseHTML(is);
            int ch;

            while((ch = htmlParser.read()) != -1)
            {
                System.out.print((char)ch);

            }
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Exception");
            e.printStackTrace();
        }


    }
}
