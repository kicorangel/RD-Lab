package generatebowbaseline;

import java.io.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

/**
 *
 * @author Francisco Manuel Rangel Pardo / Corex, Building Knowledge Solutions
 */
public class Html2Text extends HTMLEditorKit.ParserCallback
{
   StringBuffer s;

     public Html2Text() {}

     public void parse(Reader in) throws IOException {
       s = new StringBuffer();
       ParserDelegator delegator = new ParserDelegator();
       // the third parameter is TRUE to ignore charset directive
       delegator.parse(in, this, Boolean.TRUE);
     }

    @Override
     public void handleText(char[] text, int pos) {
       s.append(text);
     }

     public String getText() {
       return s.toString();
     }
}
