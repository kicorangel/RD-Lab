/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stats;

import java.io.Reader;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

/**
 *
 * @author kico
 */
public class CustomAnalyser  extends Analyzer {
    private String []STOP_WORDS;

    public CustomAnalyser()
    {
        STOP_WORDS = new String[0];
    }
    
    public CustomAnalyser(String []SW)
    {
        if (SW==null)
            STOP_WORDS = new String[0];
        else
            STOP_WORDS = SW;
    }

    @Override
    public TokenStream tokenStream(String field, Reader reader) {

        TokenStream ts = new StopFilter(false,
                new ASCIIFoldingFilter(new LowerCaseFilter(new StandardFilter(
                    new StandardTokenizer(Version.LUCENE_30, reader)))), 
                StopFilter.makeStopSet(STOP_WORDS, true), 
                true);
        
        return ts;
    }
}
