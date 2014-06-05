package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/29/14
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class SentenceGrammar {
    public static String[] tagPatterns = {
            "~NP~~VP~",
            "~PP~~NP~~VP~",
            "~NP~~NP~~VP~",
            "~NP~~ADVP~~VP~",
            "~S~<CC>~S~",
            "~ADVP~~NP~~VP~",
            "<CC>~NP~~VP~",
            //"~S~~S~",
            "~NP~~VP~~PP~",
            "~S~<WRB>~S~",
            "~S~<IN>~S~",
            "~VP~<DT>~S~",
            "~S~<IN>~VP~",
            "~PP~~VP~"
    };

    public static String[] getPatterns(){
        return tagPatterns;
    }
}
