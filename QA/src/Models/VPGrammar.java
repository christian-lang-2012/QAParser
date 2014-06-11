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
            "(<MD>)?(<VB.?>)+((<IN>)||(<TO>))?~NP~",
            "(<MD>)?(<VB.?>)+",
            "~ADVP~<VB.?>~NP~~PP~",
            "<VB.?>~S~",
            "<TO>~VP~",
            "<VB.?>~ADJP~~PP~",
            "<VB.?>~NP~~PP~",
            "<VB.?><RB>~VP~",
            "<VB.?><CC><VB.?>~NP~",
            "<VB.?>~PP~~S~",
            "<VB.?>~NP~~ADVP~",
            "~ADVP~<VB.?>~PP~",
            "~ADVP~<VB.?>~NP~",
            "~VP~<CC>~VP~",
            "<VB.?>~ADJP~",
            "~VP~~PP~",
            "<VB.?>~VP~",
            "~NP~~VP~"
    };

    public static String[] getPatterns(){
        return tagPatterns;
    }
}
