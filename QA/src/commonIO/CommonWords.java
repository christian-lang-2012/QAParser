package commonIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/14/14
 */
public class CommonWords implements  Serializable {
    private final Map<String, Integer> words;

    public CommonWords(String filename) throws IOException {
        this.words = new HashMap<String, Integer>();

        final BufferedReader reader = new BufferedReader(new FileReader(
                filename));
        String line;

        int index = 0;
        while ((line = reader.readLine()) != null) {
            this.words.put(line.trim().toLowerCase(), index++);
        }

        reader.close();
    }

    public int getWordIndex(String word) {
        return this.words.get(word.toLowerCase());
    }

    public boolean isCommonWord(String word) {
        return (this.words.containsKey(word.toLowerCase()));
    }
}
