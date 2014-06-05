package Models;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 6/2/14
 */
public class BDDScenario {
    private String theGiven;
    private ArrayList<String> scenario;

    public BDDScenario(){
        scenario = new ArrayList<String>();
    }

    public ArrayList<String> getScenario(){
        return this.scenario;
    }

    public void addUserStoryToScenario(String s){
        scenario.add(s);
    }

    @Override
    public String toString() {
        String s = "";

        for(int i = 1; i <= scenario.size(); i++){
            s += scenario.get(i);
        }

        return s;
    }
}
