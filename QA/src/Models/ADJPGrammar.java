package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/30/14
 * Time: 1:18 PM
 * ADJPGrammar is a regex grammar for finding adjective phrases
 */
public class ADJPGrammar {
    private static String[] tagPatterns = {
            "(<JJ.?>)+",
            "(<JJ.?>)+~NP~",
            "<RB.?><DT>",
            "<JJ.?>~PP~",
            "~ADVP~<JJ.?>",
            "<RB.?><JJ.?>",
            "<JJ.?><CC><JJ.?>",
            "<JJ.?>~S~",
            "~NP~<JJ.?>"
    };

    public static String[] getPatterns(){
        return tagPatterns;
    }
}
