package com.autoritas.repr.ldr;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class Tools {
    public static String GetText(String html)
    {
        try {
            Html2Text html2text = new Html2Text();
            Reader in = new StringReader(html);
            html2text.parse(in);
            return html2text.getText();
        } catch (IOException ex) {
            System.out.println("ERROR GetText: " +  ex.toString());
            return html;
        }
    }
    
    public static String Prepare(String text) {
        for (int i=0;i<text.length();i++) {
            char c = text.charAt(i);
//            if (!Character.isLetter(c) && c!=' ' && c!='#' && c!='@' && c!='?' && c!='!') {
            if (!Character.isLetter(c)) {
                text = text.replace(c, ' ');
            }
        }
        
        return text;
    }
}
