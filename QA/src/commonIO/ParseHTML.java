package commonIO;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: christianlang
 * Date: 5/15/14
 */
public class ParseHTML {

    private static Map<String, Character> characterMap;

    private PeekableInputStream source;

    private HTMLTag tag = new HTMLTag();

    private String lockedEndTag;

    public ParseHTML(InputStream is){
        this.source = new PeekableInputStream(is);

        if(characterMap == null){
            characterMap = new HashMap<String, Character>();
            characterMap.put("nbsp", ' ');
            characterMap.put("lt", '<');
            characterMap.put("gt", '>');
            characterMap.put("amp", '&');
            characterMap.put("quot", '\"');
            characterMap.put("bull", (char)149);
            characterMap.put("trade", (char)129);
        }
    }

    public HTMLTag getTag(){
        return this.tag;
    }

    public int read() throws IOException {
        if(this.lockedEndTag != null){
            if(peekEndTag(this.lockedEndTag)){
                this.lockedEndTag = null;
            }
            else{
                return this.source.read();
            }
        }

        if(this.source.peek() == '<'){
            parseTag();
            if(!this.tag.isEnding() && (this.tag.getName().equalsIgnoreCase("script") || this.tag.getName().equalsIgnoreCase("style"))){
                this.lockedEndTag = this.tag.getName().toLowerCase();
            }
            return 0;
        }
        else if(this.source.peek() == '&'){
            return parseSpecialCharacter();
        }
        else{
            return this.source.read();
        }
    }

    private char parseSpecialCharacter() throws IOException{
        char result = (char) this.source.read();
        int advanceBy = 0;

        if(result == '&'){
            int ch = 0;
            StringBuilder builder = new StringBuilder();

            do{
                ch = this.source.peek(advanceBy++);
                if((ch != '&') && (ch != ';') && !Character.isWhitespace(ch)){
                    builder.append((char)ch);
                }
            }while((ch != ';') && (ch != -1) && !Character.isWhitespace(ch));

            String b = builder.toString().trim().toLowerCase();

            if(b.length() > 0){
                if(b.charAt(0) == '#'){
                    try
                    {
                        result = (char) Integer.parseInt(b.substring(1));
                    }
                    catch(NumberFormatException e){
                        advanceBy = 0;
                    }
                }
                else{
                    if(characterMap.containsKey(b)){
                        result = characterMap.get(b);
                    }
                    else{
                        advanceBy = 0;
                    }
                }
            }
            else{
                advanceBy = 0;
            }
        }

        while(advanceBy > 0){
            read();
            advanceBy--;
        }

        return result;
    }

    private boolean peekEndTag(String name) throws IOException{
        int i = 0;

        while((this.source.peek(i) != -1) && Character.isWhitespace(this.source.peek(i))){
            i++;
        }

        if(this.source.peek(i) != '<'){
            return false;
        }else{
            i++;
        }

        while ((this.source.peek(i) != -1) && Character.isWhitespace(this.source.peek(i))){
            i++;
        }

        if(this.source.peek(i) != '/'){
            return false;
        }
        else{
            i++;
        }

        for(int j = 0; j < name.length(); j++){
            if(Character.toLowerCase(this.source.peek(i)) != Character.toLowerCase(name.charAt(j))){
                return false;
            }
            i++;
        }
        return true;
    }

    protected void eatWhitespace() throws IOException{
        while(Character.isWhitespace((char) this.source.peek())) {
            this.source.read();
        }
    }

    protected String parseAttributeName() throws IOException{
        eatWhitespace();

        if("\"\"".indexOf(this.source.peek()) == -1){
            StringBuilder builder = new StringBuilder();
            while(!Character.isWhitespace(this.source.peek()) &&
                    (this.source.peek() != '=') && (this.source.peek() != '>') &&
                    (this.source.peek() != -1)){
                int ch = parseSpecialCharacter();
                builder.append((char) ch);
            }
            return builder.toString();
        }
        else{
            return parseString();
        }
    }

    protected  String parseString() throws IOException{
        StringBuilder result = new StringBuilder();
        eatWhitespace();
        if("\"\"".indexOf(this.source.peek()) != -1){
            int delimiter = this.source.read();
            while((this.source.peek() != delimiter) && (this.source.peek() != -1)){
                if(result.length() > 1000){
                    break;
                }
                int ch = parseSpecialCharacter();
                if((ch == 13) || (ch == 10)){
                    continue;
                }
                result.append((char) ch);
            }
            if("\"\"".indexOf(this.source.peek()) != -1){
                this.source.read();
            }
        }
        else{
            while(!Character.isWhitespace(this.source.peek()) && (this.source.peek() != -1) && (this.source.peek() != '>')){
                result.append(parseSpecialCharacter());
            }
        }

        return result.toString();
    }

    protected void parseTag() throws  IOException{
        this.tag.clear();
        StringBuilder tagName = new StringBuilder();

        this.source.read();

        if((this.source.peek(0) == '!') && (this.source.peek(1) == '-') && (this.source.peek(2) == '-')){
            while(this.source.peek() != -1){
                if((this.source.peek(0) == '-') && (this.source.peek(1) == '-') && (this.source.peek(2) == '>')){
                    break;
                }
                if(this.source.peek() != '\r'){
                    tagName.append((char) this.source.peek());
                }
                this.source.read();
            }
            tagName.append("--");
            this.source.read();
            this.source.read();
            this.source.read();
            return;
        }

        while(this.source.peek() != -1){
            if(Character.isWhitespace((char) this.source.peek()) || (this.source.peek() == '>')){
                break;
            }
            tagName.append((char)this.source.read());
        }

        eatWhitespace();
        this.tag.setName(tagName.toString());

        while((this.source.peek() != '>') && (this.source.peek() != -1)){
            String attributeName = parseAttributeName();
            String attributeValue = null;

            if(attributeName.equals("/")){
                eatWhitespace();
                if(this.source.peek() == '>'){
                    this.tag.setEnding(true);
                    break;
                }
            }

            eatWhitespace();
            if(this.source.peek() == '='){
                this.source.read();
                attributeValue = parseString();
            }

            this.tag.setAttribute(attributeName, attributeValue);
        }
        this.source.read();
    }
}
