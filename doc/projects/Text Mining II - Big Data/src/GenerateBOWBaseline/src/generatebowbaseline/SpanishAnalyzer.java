package generatebowbaseline;

import java.io.Reader;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class SpanishAnalyzer extends Analyzer
{
    private String []STOP_WORDS_SPANISH;

    public SpanishAnalyzer(String []SW)
    {
        STOP_WORDS_SPANISH = SW;
    }

    @Override
    public TokenStream tokenStream(String field, Reader reader) {

        TokenStream ts = new StopFilter(false,
                new ASCIIFoldingFilter(new LowerCaseFilter(new StandardFilter(
                    new StandardTokenizer(Version.LUCENE_30, reader)))), 
                StopFilter.makeStopSet(STOP_WORDS_SPANISH, true), 
                true);
        
        return ts;
    }

}