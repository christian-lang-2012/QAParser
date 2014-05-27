package gather;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/13/14
 */
public interface ScanReportable {
    public void recieveBadPhrase(String sentence);
    public void recieveGoodPhrase(String sentence);
}
