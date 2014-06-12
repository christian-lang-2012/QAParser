package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/8/14
 * Time: 1:35 PM
 * NPGrammar is a regex grammar for finding noun phrases
 */
public class NPGrammar {
	private static String[] tagPatterns = {
            "(<RB.?>)?<CD>(<NN[^<]*>)+",
            "(<RB.?>)?<DT>(<NN[^<]*>)+",
            "((<PRP\\$>)||(<DT>)||(<CD>))?(<RB.?>)*(<JJ.?>)*(<NN[^<]*>)+",
            "<JJ.?><IN>(<NN[^<])+",
            "((<PRP\\$>)||(<DT>))?(<NN[^<]*>)+",
            "~NP~~PP~",
            "<DT><NNP><CD><NN>",
            "~NP~(<NN[^<]*>)+",
            "<NNP><POS>",
            "<VB[^<]*><NN[^<]*>",
            "<NN[^<]*><CC><NN[^<]*>",
            "~NP~~VP~",
            "<CD>",
            "~NP~<CC>~NP~",
            "~NP~~PP~",
            "~ADJP~~NP~",
           // "~NP~~NP~",
            "<NNP><CD>",
            "<DT><VB.?><NN[^<]*>",
            "(<NN[^<]*>)+",
            "<PRP>",
            "<TO><VB>~NP~",
            "<DT>~VP~",
            "<DT>~NP~"
	};

	public static String[] getPatterns(){
		return tagPatterns;
	}
}
