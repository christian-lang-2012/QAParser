package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/30/14
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
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
