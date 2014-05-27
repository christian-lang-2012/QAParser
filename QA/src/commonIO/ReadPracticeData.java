package commonIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/13/14
 */
public class ReadPracticeData {
    BufferedReader reader;
    List<String> tasks = new ArrayList<String>();

    public ReadPracticeData(String filename) throws IOException
    {
        this.reader = new BufferedReader(new FileReader(filename));

        String line;
        while((line = this.reader.readLine()) != null || !line.isEmpty())
        {
            tasks.add(line);
        }
    }

    public void close() throws IOException
    {
        this.reader.close();
    }

    public String getTask(int taskID){
        return tasks.get(taskID);
    }

    public boolean next() throws IOException
    {
        String line = this.reader.readLine();
        if(line == null){
            return false;
        }

        tasks.add(line);
        return true;
    }


}
