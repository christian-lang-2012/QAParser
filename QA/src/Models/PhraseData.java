package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 6/3/14
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PhraseData {
    private String regex;
    private String word;

    public PhraseData(String regex, String word){
        this.regex = regex;
        this.word = word;
    }

    public PhraseData(String regex){
        this.regex = regex;
        this.word = null;
    }

    public String getRegex(){
        return regex;
    }

    public String getWord(){
        return word;
    }
}
