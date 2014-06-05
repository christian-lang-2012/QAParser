package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/30/14
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
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
