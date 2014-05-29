package taskRetriever;

import Models.Task;
import com.attask.api.StreamClient;
import com.attask.api.StreamClientException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/29/14
 */
public class TaskRetriever {
    private StreamClient client;
    private ArrayList<Task> taskList;
    private JSONObject session;

    public TaskRetriever() {
        this.client = new StreamClient("https://hub.attask.com/attask/api");
        taskList = new ArrayList<Task>();
    }

    private void login(){
        //TODO: Change these to current user and password
        String username = "christianlang";
        String password = "inmelet13";

        try {
            report("Logging in...");
            this.session = client.login(username, password);
        } catch (StreamClientException e) {
            e.printStackTrace();
            report("Please use a valid username and password");
        }
    }

    public ArrayList<Task> getTaskListByProjectID(String id){

        try {

            login();

            System.out.println("Getting project..");
            Set<String> fields = new HashSet<String>();
            fields.add("tasks");

            JSONObject proj = null;
            proj = client.get("PROJ", id, fields);

            JSONArray array = proj.getJSONArray("tasks");
            for(int i = 0; i < array.length(); i++){
                String taskName = array.getJSONObject(i).getString("name");
                String taskID = array.getJSONObject(i).getString("ID");
                Task t = new Task(taskID, taskName);
                this.taskList.add(t);
            }

            logout();

            return this.taskList;

        } catch (StreamClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void logout(){
        try {
            report("Logging out...");
            client.logout();
        } catch (StreamClientException e) {
            e.printStackTrace();
        }
    }

    public void report(String input){
        System.out.println(input);
    }
}
