package com.kicorangel.repr.base;

import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
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
    
    public static String tolowerCase(String text) {
        return text.toLowerCase();
    }
    
    public static String removePunctuation(String text) {
        for (int i=0;i<text.length();i++) {
            char c = text.charAt(i);
//            if (!Character.isLetter(c) && c!=' ' && c!='#' && c!='@' && c!='?' && c!='!') {
            if (!Character.isLetter(c) && !Character.isDigit(c)) {
                text = text.replace(c, ' ');
            }
        }
        
        return text;
    }
    
    public static String removeNumbers(String text) {
        for (int i=0;i<text.length();i++) {
            char c = text.charAt(i);
            if (Character.isDigit(c)) {
                text = text.replace(c, ' ');
            }
        }
        
        return text;
    }
    
    public static String removeWhitespaces(String text) {
        return text.replaceAll(" ", "");
    }
    
    public static String removeSmallTokens(String text, int minLength) throws IOException {
        ArrayList<String> oTerms = getTokens(text, minLength);
        
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<oTerms.size();i++) {
            sb.append(oTerms.get(i)).append(" ");
        }
        
        return sb.toString().trim();
    }
    
    public static String removeStopWords(String text, String[]stopWords) throws IOException {
        ArrayList<String> oTerms = getTokens(text, 0);
        Hashtable<String, String> oStopWords = StringArray2Hashtable(stopWords);
        
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<oTerms.size();i++) {
            if (!oStopWords.containsKey(oTerms.get(i))) {
                sb.append(oTerms.get(i)).append(" ");
            }
        }
        return sb.toString().trim();
    }
    
    
    private static Hashtable<String, String> StringArray2Hashtable(String []stringArray) {
        Hashtable<String, String> oHash = new Hashtable<String, String>();
        
        for (int i=0;i<stringArray.length;i++) {
            oHash.put(stringArray[i], stringArray[i]);
        }
        
        return oHash;
    }
    public static ArrayList<String> getTokens(String text, int length) throws IOException {
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
        TokenStream stream = analyzer.tokenStream("myfield",  new StringReader(text));
        ArrayList<String> oTokens = new ArrayList<String>();
        TermAttribute term = stream.addAttribute(TermAttribute.class);
        while(stream.incrementToken()) {
            String sTerm = term.term();
            if (sTerm.length()>=length) {
                oTokens.add(term.term());
            }
        }
        return oTokens;
    }
    
    public static String Preprocess(String content, PreprocessingOptions prepOpt) throws IOException {
//        System.out.println("Preprocessing corpus...");
        if (prepOpt.tolowerCase) {
//            System.out.println("...lower case");
            content = Tools.tolowerCase(content);
        }
        if (prepOpt.removePunctuation) {
//            System.out.println("...remove punctuation");
            content = Tools.removePunctuation(content);
        }
        if (prepOpt.removeNumbers) {
//            System.out.println("...remove numbers");
            content = Tools.removeNumbers(content);
        }
        
        if (prepOpt.minTokenSize>0) {
            content = Tools.removeSmallTokens(content, prepOpt.minTokenSize);
        }
        
        if (prepOpt.removeWhitespaces) {
//            System.out.println("...remove whitespaces");
            content = Tools.removeWhitespaces(content);
        }
        
        if (prepOpt.stopwords.length>=0) {
            content = Tools.removeStopWords(content, prepOpt.stopwords);
        }
        
        return content;
    }
    
    
    
    public static Hashtable<String, Integer> LoadNGrams(String content, int n, int length, NGRAMTYPE type) throws IOException {
        if (type==NGRAMTYPE.CHAR) {
//            System.out.println("Loading char " + n + "-grams...");
            return LoadCharnGrams(content, n);
        } else {
//            System.out.println("Loading word " + n + "-grams...");
            return LoadWordnGrams(content, n, length);
        }
    }
    
    private static Hashtable<String, Integer> LoadWordnGrams(String corpus, int n, int length) throws IOException {
        Hashtable<String, Integer> oNGrams = new Hashtable<String, Integer>();
        
        ArrayList<String> sTerms = getTokens(corpus, length);
        
        for (int t=0;t<sTerms.size()-n;t++) {
            String nGram = "";
            for (int tt=0;tt<n;tt++) {
                nGram += sTerms.get(t + tt);
            }

            int iFreq = 0;
            if (oNGrams.containsKey(nGram)) {
                iFreq = oNGrams.get(nGram);
            }
            iFreq++;
            oNGrams.put(nGram, iFreq);
        }

        return oNGrams;
    }
    
    private static Hashtable<String, Integer> LoadCharnGrams(String corpus, int n) {
        Hashtable<String, Integer> oNGrams = new Hashtable<String, Integer>();
        for (int c=0;c<corpus.length()- n;c++) {
            String cNGram = "";

            for (int cc=0;cc<n;cc++) {
                char thchar = corpus.charAt(c + cc);
                if (thchar==' ') {
                    thchar = '_';
                }
                cNGram += thchar;
            }

            int iFreq = 0;
            if (oNGrams.containsKey(cNGram)) {
                iFreq = oNGrams.get(cNGram);
            }
            iFreq++;
            oNGrams.put(cNGram, iFreq);
        }
        
        return oNGrams;
    }
}
