import com.attask.api.StreamClient;
import com.attask.api.StreamClientException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Models.*;
import PhraseChunking.*;
import PoSTagging.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jacobdaniel
 * Date: 4/28/14
 */
public class TestApp {

    public static void main(String[] args) {
        PartOfSpeechTagger tagger = new PartOfSpeechTagger();
        tagger.parseSentence("Verify that if a user has received a survey, but canceled/closed without completing, the user doesn't get another survey for at least 30 days");
        List<WordTagPair> taggedWords = tagger.getTaggedWords();

        System.out.println("----------Part of Speech Tags----------");
        for (WordTagPair pair : taggedWords)
            System.out.println(pair);

        System.out.println("----------Noun Phrases----------");
        PhraseChunker pChunker = new PhraseChunker(taggedWords);
        for (String np : pChunker.getNounPhrases())
            System.out.println(np);

        System.out.println("----------Verb Phrases----------");
        for (String vp : pChunker.getVerbPhrases())
            System.out.println(vp);


        //Checking stream client stuff
        StreamClient client = new StreamClient("https://hub.attask.com");

        try {
            // Login
            System.out.print("Logging in...");
            JSONObject session = client.login("christianlang", "inmelet13");
            System.out.println("done");

            // Get user
            System.out.print("Retrieving user...");
            JSONObject user = client.get("user", session.getString("userID"), new String[]{"ID", "homeGroupID", "emailAddr"});
            System.out.println("done");

            // Search projects
            System.out.print("Searching projects...");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("groupID", user.getString("homeGroupID"));
            JSONArray results = client.search("proj", map, new String[]{"ID", "name"});
            System.out.println("done");

            for (int i = 0; i < Math.min(10, results.length()); i++) {
                System.out.println(" - " + results.getJSONObject(i).getString("name"));
            }

            // Create project
            System.out.print("Creating project...");
            map.clear();
            map.put("name", "My Project");
            map.put("groupID", user.getString("homeGroupID"));
            JSONObject proj = client.post("proj", map);
            System.out.println("done");

            // Get project
            System.out.print("Retrieving project...");
            proj = client.get("proj", proj.getString("ID"));
            System.out.println("done");

            // Edit project
            System.out.print("Editing project...");
            map.clear();
            map.put("name", "Your Project");
            proj = client.put("proj", proj.getString("ID"), map);
            System.out.println("done");

            // Delete project
            System.out.print("Deleting project...");
            client.delete("proj", proj.getString("ID"));
            System.out.println("done");

            // Logout
            System.out.print("Logging out...");
            client.logout();
            System.out.println("done");
        } catch (StreamClientException e) {
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }


      /*  JSONObject obj = new JSONObject("{\"data\":{\"ID\":\"51def83c00048b66b5e345e4cf1cd6a5\",\"name\":\"Archived Regression - In Product NPS Surveys\",\"objCode\":\"PROJ\",\"tasks\":[{\"ID\":\"51def89a00048d92d4c7fc39488e9fc1\",\"name\":\"Survey Timing Considerations\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048dbdbd5fd9a339ecee0f\",\"name\":\"Verify that all existing users have survey date set for them as a random date starting from 2nd month for the next 6 months(see desc)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048dcb4b04e804cf979969\",\"name\":\"Verify that if a user selected to never receive a survey via the never show again link, they will never get another survey ever (see desc)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e0fe3cfac269b5e5558\",\"name\":\"Verify that after a user hovers selects a number on the 1-10 scale, the \\\"share with us\\\" textbox is in focus\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e19cbcb22aa3179a028\",\"name\":\"Recorded Answers\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e1bd77002e95b62dc97\",\"name\":\"Verify that the survey question using the likelihood scale is saved to the NPS table, NPS score field\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e3d54b52351b03ce3a6\",\"name\":\"Verify the survey can be turned off at a customer level (see desc)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048d8d4d55801ed4d00035\",\"name\":\"Verify the survey is not presented to the user who enters AtTask via PPM (The survey will only be presented to Anaconda users)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e2a23f9cc1d514c14a4\",\"name\":\"Verify that the user\\/company who sent a survey response is tied by the userGUID (Global Unique Identifier)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e090443aca92fdb1583\",\"name\":\"Verify that the survey can be submitted without filling in additional comments\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048da67d5a11bef28077cd\",\"name\":\"verify that the user has been logged in at least 5 times before a survey is received\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048de2ef606ecfe4db3a89\",\"name\":\"Verify that if the user clicks the close button (X), the survey closes\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048d886c469aea7ba960b1\",\"name\":\"Verify the survey can be accessed in a Firefox browser\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048dededc5749430ae6782\",\"name\":\"Verify the scale of 1 -10 for recommendation is gradiently color coded from red to green in the lightbox\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e43d6208d9ab99e912f\",\"name\":\"Verify the survey can be turned on at a customer level (see desc)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e253d10781637e321be\",\"name\":\"Verify that when the survey is completed, the timestamp is saved to the NPS table and date field\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048df2aa7324cef6b11406\",\"name\":\"Verify the user can make comments in the \\\"share with us\\\" textbox\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048db148ab29a13ee8793f\",\"name\":\"Verify that the survey is sent on a weekly basis every Monday - test on a Monday (see desc)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e376a1a09563cd0c1b6\",\"name\":\"Verify that NPS survey functionality can be globally turned off\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048daccae3ee981aef3276\",\"name\":\"Verify the survey is sent to all different user types\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048da0030659a8b2e8ac9f\",\"name\":\"Verify that a user has to have been a user for more than 30 days to get a survey\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048dd75f879cc9ed1713ae\",\"name\":\"Verify that the option for a user to select \\\"Never show again\\\" doesn't appear until after the user has selected \\\"Go away\\\" at least once\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048d83ca9b683168f4c32e\",\"name\":\"Verify the survey can be accessed in an Internet Explorer browser\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e311804b944408e01ad\",\"name\":\"Verify that NPS survey functionality can be globally turned on\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048df8ca890a312cfd21ee\",\"name\":\"Verify that when a user clicks the go away link, the lightbox dialog closes\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048dc33f23394a53fdcee4\",\"name\":\"Verify that the number of users receiving the survey is equally distributed for the 2 - 6 month period (see desc)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e14b4f2bb6ca38288ef\",\"name\":\"Verify that a user can turn the toggle on or off in Pitboss to receive a survey\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048d9a8b757352ebe59a58\",\"name\":\"Verify that if a user has received a survey, but canceled\\/closed without completing, the user doesn't get another survey for at least 30 days\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048dd172d4fcd93311c985\",\"name\":\"Verify that a user has to have to have logged in at least 5 times to receive a survey\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048ddd8ea2a905b6912413\",\"name\":\"Verify that survey title bar text is Help Us Make AtTask Better\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048db766e561b01476222b\",\"name\":\"Verify that the survey is sent on a weekly basis every Monday - test on a day other than Monday (see desc)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048dfe8ef8ab476523282c\",\"name\":\"Verify that when a user hovers over a number on the 1-10 scale, the number is shaded dark grey\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048de7c8f77dabc65aaa06\",\"name\":\"Verify that the done button is disabled when the survey is presented to the user\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e2020a1eec987e7f193\",\"name\":\"Verify that the survey question for additional comments is saved to the NPS table, primary_reason field\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048d943346935f14fc1635\",\"name\":\"Verify that if a user has completed a survey, they don't get another survey for a year (see desc)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048dc95c526ac44f4bd26c\",\"name\":\"Survey non-timing related\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e033450c00821dbedce\",\"name\":\"Verify that the done button is enabled when the user clicks on a number in the scale\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e2f6adf3787d34c14a7\",\"name\":\"Back Office Toggle\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048e49af5f840da5ceedbd\",\"name\":\"Setup to receive a survey (see desc)\",\"objCode\":\"TASK\"},{\"ID\":\"51def89a00048d8030af2851e7de8dd5\",\"name\":\"General Tests\",\"objCode\":\"TASK\"}]}}");
            String id = obj.getJSONObject("data").getString("ID");


            JSONArray array = obj.getJSONObject("data").getJSONArray("tasks");
            for(int i = 0; i < array.length(); i++){
                String task = array.getJSONObject(i).getString("name");
                System.out.println(task);
            }

      */
    }
}
