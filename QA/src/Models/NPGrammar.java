package Models;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 5/8/14
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
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
            "<TO><VB>",
            "<DT>~VP~"
	};

	public static String[] getPatterns(){
		return tagPatterns;
	}
}
