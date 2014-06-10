package translator;

import Models.BDDScenario;
import Models.BDDStory;
import Models.Task;
import Models.WordTagPair;
import PhraseChunking.PhraseChunker;
import PoSTagging.PartOfSpeechTagger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 6/2/14
 */
public class TaskToBDDStory {

    PartOfSpeechTagger tagger = new PartOfSpeechTagger();

    public BDDStory convertTaskToStory(Task task){
        BDDStory story = new BDDStory();

        tagger.parseSentence(task.getTask());
        List<WordTagPair> taggedWords = tagger.getTaggedWords();

        PhraseChunker pChunker = new PhraseChunker(taggedWords);

        BDDScenario scenario = new BDDScenario();

        for (String np : pChunker.getPhrases()){
            scenario.addUserStoryToScenario(np);
        }

        story.addScenario(scenario);

        return story;

    }
}
