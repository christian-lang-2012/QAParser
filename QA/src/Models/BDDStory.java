package Models;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 6/2/14
 *
 * This is a basic model for the Behavior Driven Development story.
 */
public class BDDStory {

    private String theStory;
    private ArrayList<BDDScenario> scenarios;

    public void addScenario(BDDScenario bddScenario){
        scenarios.add(bddScenario);
    }

    public String getTheStory() {
        return theStory;
    }

    public void setTheStory(String theStory) {
        this.theStory = theStory;
    }

    public ArrayList<BDDScenario> getScenarios() {
        return scenarios;
    }

    public void setScenarios(ArrayList<BDDScenario> scenarios) {
        this.scenarios = scenarios;
    }

    @Override
    public String toString() {
        String s = "";
        if(theStory != null)
            s += "Story:" + theStory + "\n";
        else
            s+= "Story: None:\n";

        for(BDDScenario scenario : scenarios){
            s += scenario.toString() + "\n";
        }

        return s;
    }
}
