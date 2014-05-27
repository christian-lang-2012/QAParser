package gather;

import Config.Config;
import commonIO.ReadPracticeData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/13/14
 */
public class GatherForTraining implements ScanReportable {

    boolean log = true;
    int threadPoolSize = 20;

    public static void main(String args[])
    {
        try
        {
            GatherForTraining when = new GatherForTraining();
            when.process();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private ExecutorService pool;
    Collection<String> trainingDataGood = new Vector<String>();
    Collection<String> trainingDataBad = new Vector<String>();
    private int currentTask;

    private int totalTasks;

    public void process() throws IOException
    {
        this.pool = Executors.newFixedThreadPool(threadPoolSize);
        Collection<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        ReadPracticeData practiceData = new ReadPracticeData("goodPracticeData.txt");
        report("Building training data");
        //TODO: practice data parsing and what to do with that data

        Date started = new Date();
        int i = 0;
        while(practiceData.next()){
            String statement = practiceData.getTask(i);

            CollectionWorker worker = new CollectionWorker(this, statement);
        }

        try{
            this.totalTasks = tasks.size();
            this.currentTask = 1;
            this.pool.invokeAll(tasks);
            this.pool.shutdown();
            report("Done collecting data.");
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        long length = (new Date()).getTime() - started.getTime();
        length /= 1000L;
        length /= 60;
        report("Took " + length + " minutes to collect training data");
        System.out.println("Writing training file");
        writeTrainingFile();
    }

    @Override
    public void recieveBadPhrase(String sentence) {
        this.trainingDataBad.add(sentence);
    }

    @Override
    public void recieveGoodPhrase(String sentence) {
        this.trainingDataGood.add(sentence);
    }

    public void report(String input){
        synchronized (this){
            System.out.println(input);
        }
    }

    public void reportDone(String input){
        report(this.currentTask + "/" + this.totalTasks + ":" + input);
    }

    private void writeTrainingFile() throws IOException{
        OutputStream fs = new FileOutputStream(Config.Config.FILENAME_GOOD_TRAINING_TEXT);
        PrintStream ps = new PrintStream(fs);

        for(String str : this.trainingDataGood){
            ps.println(str.trim());
        }

        ps.close();
        fs.close();

        fs = new FileOutputStream(Config.Config.FILENAME_BAD_TRAINING_TEXT);
        ps = new PrintStream(fs);

        for(String str: this.trainingDataBad){
            ps.println(str.trim());
        }

        ps.close();
        fs.close();

    }
}
