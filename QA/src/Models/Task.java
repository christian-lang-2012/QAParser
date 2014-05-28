package Models;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/27/14
 */
public class Task {
    String id;
    String task;

    public Task(String id, String task){
        this.id = id;
        this.task = task;
    }

    @Override
    public String toString(){
        String string;

        string = "Task: " + task + "\nID: " + id + "\n";

        return string;
    }
}
