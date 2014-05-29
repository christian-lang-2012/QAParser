package Models;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/27/14
 */
public class Task {
    private String id;
    private String task;

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

    public String getTask(){
        return this.task;
    }

    public String getId(){
        return this.id;
    }

    public void setTask(String task){
        this.task = task;
    }

    public void setId(String id){
        this.id = id;
    }
}
