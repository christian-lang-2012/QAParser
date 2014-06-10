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
//        ArrayList<Task> taskList = new ArrayList<Task>();
//
//        String practiceID = "51def83c00048b66b5e345e4cf1cd6a5";
//
//
//        PartOfSpeechTagger tagger = new PartOfSpeechTagger();
//
//
//        TaskRetriever tr = new TaskRetriever();
//        ArrayList<Task> tasks = tr.getTaskListByProjectID(practiceID);
//
//        System.out.println("Starting tagging...This may take a while");
//
//        int i = 1;
//        for(Task t : tasks){
//            System.out.println("----------Starting Task #: " + i + " ----------");
//            System.out.println("Task: " + t.getTask());
//            System.out.println();
//
//            tagger.parseSentence(t.getTask());
//            List<WordTagPair> taggedWords = tagger.getTaggedWords();
//
//            System.out.println("----------Part of Speech Tags----------");
//            for (WordTagPair pair : taggedWords)
//                System.out.println(pair);
//
//            System.out.println();
//            System.out.println("----------Phrases----------");
//            PhraseChunker pChunker = new PhraseChunker(taggedWords);
//            for (String p : pChunker.getPhrases())
//                System.out.println(p);
//
//
//
//
//            System.out.println();
//            System.out.println("----------End of Task #: " + i + "----------");
//            System.out.println();
//            i++;
//        }

        List<WordTagPair> taggedWords = new ArrayList<WordTagPair>();
        String sentence = "Verify/VB the/DT survey/NN can/MD be/VB accessed/VBN in/IN an/DT Internet/NN Explorer/NNP browser/NN";
        String[] tags = sentence.split(" ");
        for(String tag: tags)
            taggedWords.add(new WordTagPair(tag.split("/")[0],PartOfSpeechTag.valueOf(tag.split("/")[1])));

        System.out.println("----------Phrases----------");
        PhraseChunker pChunker = new PhraseChunker(taggedWords);
        for(String phrase : pChunker.getPhrases())
            System.out.println(phrase);


    }
}
