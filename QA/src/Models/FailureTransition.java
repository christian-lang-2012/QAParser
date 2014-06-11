package Models;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 6/10/14
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class FailureTransition {
    private List<Tree<PhraseData>> currentTreeState;
    private List<Tree<PhraseData>> transition;

    public FailureTransition(List<Tree<PhraseData>> state, List<Tree<PhraseData>> transition){
        this.setState(state);
        this.setTransition(transition);
    }

    public void setState(List<Tree<PhraseData>> state){
        this.currentTreeState = state;
    }

    public void setTransition(List<Tree<PhraseData>> transition){
        this.transition = transition;
    }

    public List<Tree<PhraseData>> getState(){
        return currentTreeState;
    }

    public List<Tree<PhraseData>> getTransition(){
        return transition;
    }

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof  FailureTransition))
            return false;
        if(obj == this)
            return true;

        FailureTransition ft = (FailureTransition)obj;

        boolean sameState = true;
        for(Tree<PhraseData> state : ft.getState()){
            if(!currentStateContainsState(state))
                sameState = false;
        }
        if(ft.getState().size() != currentTreeState.size())
            sameState = false;

        boolean sameTransition = true;
        for(Tree<PhraseData> transition : ft.getTransition()){
            if(!this.transition.contains(transition))
                sameTransition = false;
        }
        if(ft.getTransition().size() != this.transition.size())
            sameTransition = false;

        return (sameState && sameTransition);
    }

    private boolean currentStateContainsState(Tree<PhraseData> state){
        boolean found = false;
        for(Tree<PhraseData> currentState : currentTreeState){
            if(currentState.getRoot().getData().equals(state.getRoot().getData()))
                found = true;
        }
        return found;
    }
}
