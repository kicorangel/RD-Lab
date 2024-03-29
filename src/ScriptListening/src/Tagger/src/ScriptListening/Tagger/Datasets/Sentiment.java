/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package ScriptListening.Tagger.Datasets;

import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.base.Tools;
import com.kicorangel.repr.base.ValueComparator;
import com.kicorangel.repr.enumerations.Ordering;
import com.kicorangel.repr.nGrams.ArffMngr;
import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;
import org.apache.lucene.util.Version;
import org.xml.sax.SAXException;

/**
 *
 * @author @kicorangel
 */
public class Sentiment {
    private int mN;
    private int mTotal;
    private String msCorpusPath;
    private String msTmpPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    private ArrayList<String> mLabels;
    private SET mSet;
    private PreprocessingOptions mPrepOpt;
    private NGRAMTYPE mType;
       
    public Sentiment(int n, int total, NGRAMTYPE type, String corpusPath, String tmpPath, String trainingArff, String testPath, String testArff, ArrayList<String> labels, SET set, PreprocessingOptions prepOpt) {
        mN = n;
        mTotal = total;
        msCorpusPath = corpusPath;
        msTmpPath = tmpPath;
        msTrainingArff = trainingArff;
        msTestPath = testPath;
        msTestArff = testArff;
        mLabels = labels;
        mSet = set;
        mPrepOpt = prepOpt;
        mType = type;
    }
    
    public void Run() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        if (mLabels==null) {
            mLabels = GetLabels();
        }
        
        if (mSet==SET.TRAINING || mSet==SET.BOTH) {
            Prepare(mN, mTotal, mType, msCorpusPath, msTmpPath, mPrepOpt);
        }
        
        if (mSet==SET.TRAINING || mSet==SET.BOTH) {
            Generate(mN, mTotal, mType, msCorpusPath, msTmpPath, mPrepOpt, msTrainingArff);
        }
        
        if (mSet==SET.TEST || mSet==SET.BOTH) {
            Generate(mN, mTotal, mType, msTestPath, msTmpPath, mPrepOpt, msTestArff);
        }
    }
    
    private void Generate(int n, int total, NGRAMTYPE type, String corpus, String tmp, PreprocessingOptions prepOpt, String arffFile) throws IOException {
        Hashtable<String, String> oTruth = LoadTruth(corpus);
//        TreeMap<String, Integer> oNGramsCorpus = ReadNGramCorpus(tmp);
        ArrayList<String> oNGramsCorpus = ReadNGramCorpus(tmp);
        Hashtable<String, String> oCorpus = LoadCorpus(corpus);
        
        FileWriter fw = null;
        try {
            fw = new FileWriter(arffFile);
            ArffMngr.GenerateHeader(oNGramsCorpus, mLabels, type.toString(), n, total, fw);
            
            Enumeration keys = oTruth.keys();
            while (keys.hasMoreElements()) {
                String sId = (String)keys.nextElement();
                String sClass = (String)oTruth.get(sId);

                if (mLabels.contains(sClass)) {
//                    String sContent = LoadFileContents(corpus + sFile);
                    String sContent = oCorpus.get(sId);
                    sContent = Preprocess(sContent, prepOpt);
                    Hashtable<String, Integer> oMyNGrams = LoadNGrams(sContent, n, type);

                    StringBuilder sb = new StringBuilder();
                    for (int i=0;i<oNGramsCorpus.size();i++) {
                        String nGram = oNGramsCorpus.get(i);
        //            for(Map.Entry m:oNGramsCorpus.entrySet()){  
        //                String nGram = (String)m.getKey();
        //                int freq = (int)m.getValue();
                        int freq = 0;
                        if (oMyNGrams.containsKey(nGram)) {
                            freq = oMyNGrams.get(nGram);
                        }
                        double f = (double)((double)freq/(double)oMyNGrams.size());
                        sb.append(f).append(",");
                    }
                    sb.append(sClass);

                    ArffMngr.AddLine(sb.toString(), fw);
                }
            }
        } catch (Exception ex) {
            
        } finally {
            if (fw!=null) { try { fw.close(); } catch (Exception ex) {} }
        }
        
    }
    
    private ArrayList<String> ReadNGramCorpus(String path2GramsFile) {
//        System.out.println("Reading n-grams from tf file...");
        
        ArrayList<String> oNGrams = new ArrayList<String>();
        
        FileReader fr = null;
        try {
            fr = new FileReader(path2GramsFile);
            BufferedReader bf = new BufferedReader(fr);
            String sCadena = "";
            while ((sCadena = bf.readLine())!=null) {
                String []info = sCadena.split("\t");
                if (info.length==2) {
                    String nGram = info[0];
                    oNGrams.add(nGram);
                }
            }
        } catch (Exception ex) {
            
        } finally {
            if (fr!=null) { try { fr.close(); } catch (Exception ex) {} }
        }
        
        return oNGrams;
    }
    
    private void Prepare(int n, int total, NGRAMTYPE type, String corpus, String tmp, PreprocessingOptions prepOpt) throws IOException {
        String sContent = LoadCorpus(corpus, LoadTruth(corpus));
        sContent = Preprocess(sContent, prepOpt);
        Hashtable<String, Integer> oNGrams = LoadNGrams(sContent, n, type);
        TreeMap<String, Integer> oOrderedNGrams = OrderNGrams(oNGrams);
        GenerateTF(oOrderedNGrams, tmp, total);
    }
    
    private TreeMap<String, Integer> OrderNGrams(Hashtable<String, Integer> nGrams) {
//        System.out.println("Ordering n-grams...");
        ValueComparator bvc =  new ValueComparator(nGrams, Ordering.DESC);
        TreeMap<String,Integer> sortedNGrams = new TreeMap<String,Integer>(bvc);
        sortedNGrams.putAll(nGrams);
        return sortedNGrams;
    }
    
    
    private Hashtable<String, Integer> LoadNGrams(String content, int n, NGRAMTYPE type) throws IOException {
        if (type==NGRAMTYPE.CHAR) {
//            System.out.println("Loading char " + n + "-grams...");
            return LoadCharnGrams(content, n);
        } else {
//            System.out.println("Loading word " + n + "-grams...");
            return LoadWordnGrams(content, n);
        }
    }
    
    private void GenerateTF(TreeMap<String, Integer> nGrams, String path2Tmp, int total) {
//        System.out.println("Filtering " + total + " n-grams and generating tf file...");
        int i = 0;
        FileWriter fw = null;
        try {
            fw = new FileWriter(path2Tmp);
            
            for(Map.Entry m:nGrams.entrySet()){  
                String nGram = (String)m.getKey();
                int freq = (int)m.getValue();
//            }  
//
//            for( Iterator it = nGrams.keySet().iterator(); it.hasNext();) {
//                String nGram = (String)it.next();
//                int freq = (int)nGrams.get(nGram);
                
                fw.write(nGram + "\t" + freq + "\n");
                
                i++;
                
                if (i>=total) {
                    break;
                }
            }
        } catch (Exception ex) {
            
        } finally {
            if (fw!=null) { try { fw.close(); } catch (Exception ex) {} }
        }
    }
    
    private String Preprocess(String content, PreprocessingOptions prepOpt) {
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
        if (prepOpt.removeWhitespaces) {
//            System.out.println("...remove whitespaces");
            content = Tools.removeWhitespaces(content);
        }
        
        return content;
    }
    
    
   
    
    
    private Hashtable<String, Integer> LoadWordnGrams(String corpus, int n) throws IOException {
        Hashtable<String, Integer> oNGrams = new Hashtable<String, Integer>();
        
        ArrayList<String> sTerms = getTokens(corpus);
        
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
    
    private Hashtable<String, Integer> LoadCharnGrams(String corpus, int n) {
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
    
    public static ArrayList<String> getTokens(String text) throws IOException {
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
        TokenStream stream = analyzer.tokenStream("myfield",  new StringReader(text));
        ArrayList<String> oTokens = new ArrayList<String>();
        TermAttribute term = stream.addAttribute(TermAttribute.class);
        while(stream.incrementToken()) {
            oTokens.add(term.term());
        }
        return oTokens;
    }
  
    
    // Code to be customised
    
    public static ArrayList<String> GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
        oLabels.add("POSITIVE");
        oLabels.add("NEGATIVE");
        oLabels.add("NEUTRAL");
        
        return oLabels;
    }
    
    public Hashtable<String, String> LoadTruth(String sDataPath) throws FileNotFoundException, IOException {
        Hashtable<String, String> oTruth = new Hashtable<String, String>();
        
        ArrayList<String[]> oRecords = new ArrayList<String[]>();
        CSVReader csvReader = new CSVReader(
                new InputStreamReader(
                        new FileInputStream(sDataPath)
                ), '\t'
        );
          
        String []record;
        while ((record = csvReader.readNext()) != null) {
            oTruth.put(record[0], record[2]);
        }
        
        csvReader.close();
   
        return oTruth;
    }
    private Hashtable<String, String> LoadCorpus(String path2Corpus) throws FileNotFoundException, IOException {
        Hashtable<String, String> oCorpus = new Hashtable<String, String>();
        
        ArrayList<String[]> oRecords = new ArrayList<String[]>();
        CSVReader csvReader = new CSVReader(
                new InputStreamReader(
                        new FileInputStream(path2Corpus)
                ), '\t'
        );
          
        int id = 0;
        String []record;
        while ((record = csvReader.readNext()) != null) {
            oCorpus.put(record[0], record[1]);
        }
        
        csvReader.close();
        
        return oCorpus;
    }
    
    private String LoadCorpus(String path2Corpus, Hashtable<String, String>oTruth) throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();
        
        ArrayList<String[]> oRecords = new ArrayList<String[]>();
        CSVReader csvReader = new CSVReader(
                new InputStreamReader(
                        new FileInputStream(path2Corpus)
                ), '\t'
        );
          
        int id = 0;
        String []record;
        while ((record = csvReader.readNext()) != null) {
            sb.append(record[1]).append(" ");
        }
        
        csvReader.close();
        
        System.out.println(sb.length() + " characters loaded");
        return sb.toString();
    }
}
