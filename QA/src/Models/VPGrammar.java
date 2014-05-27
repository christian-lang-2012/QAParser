package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/19/14
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class VPGrammar {

    private static String[] tagPatterns = {
            "(<VB.?>)+(<TO>)?"+NPGrammar.getPatterns()[0],
            "(<VB.?>)+(<TO>)?"+NPGrammar.getPatterns()[1],
            "(<VB.?>)+(<TO>)?"+NPGrammar.getPatterns()[2],
            "(<VB.?>)+(<TO>)?"+NPGrammar.getPatterns()[3],
            "(<VB.?>)+(<TO>)?"+NPGrammar.getPatterns()[4],
            "(<VB.?>)+(<TO>)?"+NPGrammar.getPatterns()[5],
            "(<VB.?>)+(<TO>)?"+NPGrammar.getPatterns()[6],
            "(<VB.?>)+"
    };

    public static String[] getPatterns(){
        return tagPatterns;
    }
}
