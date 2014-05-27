package commonIO;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/15/14
 */
public class HTMLTag {

    private Map<String, String> attributes = new HashMap<String, String>();
    private String name = "";
    private boolean ending;

    public void clear(){
        this.attributes.clear();
        this.name = "";
        this.ending = false;
    }

    public String getAttributeValue(String name){
        return this.attributes.get(name.toLowerCase());
    }

    public String getName(){
        return this.name;
    }

    public boolean isEnding(){
        return this.ending;
    }

    public void setEnding(boolean ending){
        this.ending = ending;
    }

    public void setName(String s){
        this.name = s;
    }

    public void setAttribute(String name, String value){
        this.attributes.put(name.toLowerCase(), value);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder("<");
        builder.append(this.name);

        Set<String> set = this.attributes.keySet();
        for(String key: set){
            String value = this.attributes.get(key);
            builder.append(' ');

            if(value == null){
                builder.append("\"");
                builder.append(key);
                builder.append("\"");
            }
            else{
                builder.append(key);
                builder.append("=\"");
                builder.append(value);
                builder.append("\"");
            }
        }

        if(this.ending){
            builder.append('/');
        }

        builder.append(">");
        return builder.toString();

    }

}
