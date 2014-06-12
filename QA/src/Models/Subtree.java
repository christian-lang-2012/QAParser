package Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 6/10/14
 * Time: 12:34 PM
 * Subtree represents a phrase containing other phrases or words
 * A subtree has a type: NP, VP, ADJP, ADVP, PP, S
 */
public class Subtree<T> {
    private List<T> subtrees;
    private String type;

    public Subtree(String type){
        this.type = type;
        subtrees = new ArrayList<T>();
    }

    public void add(T tree){
        subtrees.add(tree);
    }

    public List<T> getSubtrees(){
        return subtrees;
    }

    public String getType(){
        return type;
    }

    public int getSize(){
        return subtrees.size();
    }
}
