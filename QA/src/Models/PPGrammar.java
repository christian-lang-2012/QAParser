package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/30/14
 * Time: 1:11 PM
 * PPGrammar is a regex grammar for finding prepositional phrases
 */
public class PPGrammar {
    private static String[] tagPatterns = {
            "<IN>~NP~",
            "<TO>~NP~",
            "~PP~<CC>~PP~",
            "<IN>~S~",
            "~PP~~PP~"
    };

    public static String[] getPatterns(){
        return tagPatterns;
    }
}
