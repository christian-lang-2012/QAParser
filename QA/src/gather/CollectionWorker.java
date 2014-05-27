package gather;

import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/13/14
 */
public class CollectionWorker implements Callable<Integer>
{
     //TODO: supposed to use the search and training bot to find the correct data.
    private GatherForTraining bot;

    HubSearch search;
    String id;

    public CollectionWorker(GatherForTraining bot, String id){
        this.bot = bot;
        this.id = id;
        this.search = new HubSearch();
    }


    @Override
    public Integer call() throws Exception {
        try {
            //TODO: Fix what needs to happen when this gets invoked
            this.bot.reportDone(", done scanning.");
        } catch (final Exception e) {
            this.bot.reportDone(", error encountered.");
            e.printStackTrace();
            throw e;
        }
        return null;
    }


}
