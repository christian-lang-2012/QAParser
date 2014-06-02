import com.attask.api.StreamClient;
import com.attask.api.StreamClientException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Models.*;
import PhraseChunking.*;
import PoSTagging.*;
import taskRetriever.TaskRetriever;

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

    public static void main(String[] args) {
        ArrayList<Task> taskList = new ArrayList<Task>();

        String practiceID = "51def83c00048b66b5e345e4cf1cd6a5";


        PartOfSpeechTagger tagger = new PartOfSpeechTagger();
/*
        tagger.parseSentence("Verify that the survey is sent on a weekly basis every Monday - test on a Monday (see desc)");
        List<WordTagPair> taggedWords = tagger.getTaggedWords();

        System.out.println("----------Part of Speech Tags----------");
        for (WordTagPair pair : taggedWords)
            System.out.println(pair);

        System.out.println("----------Noun Phrases----------");
        PhraseChunker pChunker = new PhraseChunker(taggedWords);
        for (String np : pChunker.getNounPhrases())
            System.out.println(np);

        System.out.println("----------Verb Phrases----------");
        for (String vp : pChunker.getVerbPhrases())
            System.out.println(vp);
*/

        TaskRetriever tr = new TaskRetriever();
        ArrayList<Task> tasks = tr.getTaskListByProjectID(practiceID);

        System.out.println("Starting tagging...This may take a while");

        int i = 1;
        for(Task t : tasks){
            System.out.println("----------Starting Task #: " + i + " ----------");
            System.out.println("Task: " + t.getTask());
            System.out.println();

            tagger.parseSentence(t.getTask());
            List<WordTagPair> taggedWords = tagger.getTaggedWords();

            System.out.println("----------Part of Speech Tags----------");
            for (WordTagPair pair : taggedWords)
                System.out.println(pair);

            System.out.println();
            System.out.println("----------Noun Phrases----------");
            PhraseChunker pChunker = new PhraseChunker(taggedWords);
            for (String np : pChunker.getNounPhrases())
                System.out.println(np);

            System.out.println();
            System.out.println("----------Verb Phrases----------");
            for (String vp : pChunker.getVerbPhrases())
                System.out.println(vp);


            System.out.println();
            System.out.println("----------End of Task #: " + i + "----------");
            System.out.println();
            i++;
        }
    }
}
