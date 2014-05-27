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
//		"((<DT>)|(<PP\\$>))?(<JJ>)*(<NN>)", //chunk determiner/possessive, adjectives and nouns
//        "(<RB.?>)*((<DT>)|(<CD>)|(<PRP\\$>))?((<JJ.?>)|(<NN.?>))*(<NN.?><IN>(<DT>)?)?((<RB.?>)|(<JJ.?>)|(<NN.?>))*((<NN.?>)|(<PRP>))"
            "(<NN[^<]*>)+",
            "(<RB.?>)?<CD>(<NN[^<]*>)+",
            "(<RB.?>)?<DT>(<NN[^<]*>)+",
            "((<PRP\\$>)||(<DT>)||(<CD>))?(<RB.?>)*(<JJ.?>)*(<NN[^<]*>)+",
            "<JJ.?><IN>(<NN[^<])+",
            "<IN>((<PRP\\$>)||(<DT>))?(<NN[^<]*>)+",
            "<PRP>",
            "<TO><VB>"
	};

	public static String[] getPatterns(){
		return tagPatterns;
	}
}
