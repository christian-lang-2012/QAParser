package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/30/14
 * Time: 1:27 PM
 * ADVPGrammar is a regex grammar for finding adverb phrases
 */
public class ADVPGrammar {
    private static String[] tagPatterns = {
            "(<RB.?>)+",
            "<IN><RB.?>",
            "<RB.?><IN>"
    };

    public static String[] getPatterns(){
        return tagPatterns;
    }
}
