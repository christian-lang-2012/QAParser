package gather;

import commonIO.HTMLTag;
import commonIO.ParseHTML;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/16/14
 */
public class HubSearch {

    private Collection<URL> doSearch(URL url) throws IOException{
        Collection<URL> results = new ArrayList<URL>();

        InputStream is = url.openStream();
        ParseHTML parser = new ParseHTML(is);
        StringBuilder builder = new StringBuilder();
        boolean  capture = false;

        int ch;
        while((ch = parser.read()) != -1){
            if(ch == 0){
                HTMLTag tag = parser.getTag();
                if(tag.getName().equalsIgnoreCase("url")){
                    builder.setLength(0);
                    capture = true;
                }
                else if(tag.getName().equalsIgnoreCase("/url")){
                    results.add(new URL(builder.toString()));
                    builder.setLength(0);
                    capture = false;
                }
            }
            else{
                if(capture){
                    builder.append((char)ch);
                }
            }
        }

        return results;
    }

    /**
     * This search is based entirely on you selecting a report, not a project.
     * A report search will be added later
     * @param searchID
     * @return
     * @throws java.io.IOException
     */
    public Collection<URL> search(String searchID) throws IOException{
        Collection<URL> result = new ArrayList<URL>();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        URL url = new URL("https://hub.attask.com/report/view?ID="+searchID);

        int tries = 0;
        boolean done = false;
        while (!done) {
            try {
                result = doSearch(url);
                done = true;
            } catch (final IOException e) {
                if (tries == 5) {
                    throw (e);
                }
                try {
                    Thread.sleep(5000);
                    //TODO: Stuff here
                } catch (final InterruptedException e1) {
                }
            }
            tries++;
        }

        return result;
    }
}
