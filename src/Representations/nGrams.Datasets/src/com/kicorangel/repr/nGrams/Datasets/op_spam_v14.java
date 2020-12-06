/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kicorangel.repr.nGrams.Datasets;

import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.base.Tools;
import static com.kicorangel.repr.base.Tools.Preprocess;
import static com.kicorangel.repr.base.Tools.getTokens;
import com.kicorangel.repr.base.ValueComparator;
import com.kicorangel.repr.base.iLoadDocsxClass;
import com.kicorangel.repr.enumerations.Ordering;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.nGrams.ArffMngr;
import com.kicorangel.repr.enumerations.NGRAMTYPE;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
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
 * @author kicorangel
 */
public class op_spam_v14 {
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
    private int mLength;
    
    public op_spam_v14(int n, int total, int length, NGRAMTYPE type, String corpusPath, String tmpPath, String trainingArff, String testPath, String testArff, ArrayList<String> labels, SET set, PreprocessingOptions prepOpt) {
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
        mLength = length;
    }
    
    public void Run() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        if (mLabels==null) {
            mLabels = GetLabels();
        }
        
        if (mSet==SET.TRAINING || mSet==SET.BOTH) {
            Prepare(mN, mTotal, mLength, mType, msCorpusPath, msTmpPath, mPrepOpt);
        }
        
        if (mSet==SET.TRAINING || mSet==SET.BOTH) {
            Generate(mN, mTotal, mLength, mType, msCorpusPath, msTmpPath, mPrepOpt, msTrainingArff);
        }
        
        if (mSet==SET.TEST || mSet==SET.BOTH) {
            Generate(mN, mTotal, mLength, mType, msTestPath, msTmpPath, mPrepOpt, msTestArff);
        }
    }
    
    private void Generate(int n, int total, int length, NGRAMTYPE type, String corpus, String tmp, PreprocessingOptions prepOpt, String arffFile) throws IOException {
        Hashtable<String, String> oTruth = LoadTruth(corpus);
//        TreeMap<String, Integer> oNGramsCorpus = ReadNGramCorpus(tmp);
        ArrayList<String> oNGramsCorpus = ReadNGramCorpus(tmp);
        
        FileWriter fw = null;
        try {
            fw = new FileWriter(arffFile);
            ArffMngr.GenerateHeader(oNGramsCorpus, mLabels, type.toString(), n, total, fw);
            
            Enumeration keys = oTruth.keys();
            while (keys.hasMoreElements()) {
                String sFile = (String)keys.nextElement();
                String sClass = (String)oTruth.get(sFile);

                if (mLabels.contains(sClass)) {
                    String sContent = LoadFileContents(corpus + "/" + sFile);
                    sContent = Preprocess(sContent, prepOpt);
                    Hashtable<String, Integer> oMyNGrams = LoadNGrams(sContent, n, length, type);

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
    
    private void Prepare(int n, int total, int length, NGRAMTYPE type, String corpus, String tmp, PreprocessingOptions prepOpt) throws IOException {
        String sContent = LoadCorpus(corpus, LoadTruth(corpus));
        sContent = Preprocess(sContent, prepOpt);
        Hashtable<String, Integer> oNGrams = LoadNGrams(sContent, n, length, type);
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
    
    
    private Hashtable<String, Integer> LoadNGrams(String content, int n, int length, NGRAMTYPE type) throws IOException {
        if (type==NGRAMTYPE.CHAR) {
//            System.out.println("Loading char " + n + "-grams...");
            return LoadCharnGrams(content, n);
        } else {
//            System.out.println("Loading word " + n + "-grams...");
            return LoadWordnGrams(content, n, length);
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
    
    
    
    
   
    
    
    private Hashtable<String, Integer> LoadWordnGrams(String corpus, int n, int length) throws IOException {
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
    
    
  
    
    // Code to be customised
    
    public static ArrayList<String> GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
        oLabels.add("d");
        oLabels.add("t");
               
        return oLabels;
    }
    
    
    private Hashtable<String, String> LoadTruth(String sDataPath) throws FileNotFoundException, IOException {
//        System.out.println("Loading truth...");
        
        Hashtable<String, String> oTruth = new Hashtable<String, String>();
        
        File directory = new File(sDataPath);
        String[] authors = directory.list();
        for (int iAuthor = 0; iAuthor < authors.length; iAuthor++) {
            String sAuthor = authors[iAuthor];
            
            if (sAuthor.startsWith("d")) {
                oTruth.put(sAuthor, "d");
            } else if (sAuthor.startsWith("t")) {
                oTruth.put(sAuthor, "t");
            }
        }
        
        return oTruth;
    }
    
    private String LoadCorpus(String path2Corpus, Hashtable<String, String>oTruth) throws FileNotFoundException, IOException {
        String sContent = "";
        StringBuilder sb = new StringBuilder();
        
        File directory = new File(path2Corpus);
        String[] authors = directory.list();
//        System.out.println("Loading corpus (" + authors.length + " files)...");
        for (int iAuthor = 0; iAuthor < authors.length; iAuthor++) {
            String sAuthor = authors[iAuthor];
            String sClass = (String)oTruth.get(sAuthor);
            sb.append(LoadFileContents(path2Corpus + "/" + sAuthor)).append(" ");
        }
        
        return sb.toString();
    }
    
    
     private String LoadFileContents(String fileName) throws FileNotFoundException, IOException {
        StringBuilder text = new StringBuilder();
        
        boolean bText = false;
        InputStream is = new FileInputStream(fileName);
        InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
        BufferedReader bf = new BufferedReader(isr);
        String sCadena = "";
        while ((sCadena = bf.readLine())!=null) {
            text.append(sCadena).append(" ");
        }
        bf.close(); isr.close(); is.close();
        
        return text.toString().trim();
    }
     
    
    
    
    private static String GetClass(String sFile) {
        String sClass = "";
        
        if (sFile.startsWith("kor")) {
            sClass = "korean";
        } else if (sFile.startsWith("spa")) {
            sClass = "spanish";
        } else if (sFile.startsWith("tel")) {
            sClass = "telegu";
        } else if (sFile.startsWith("fre")) {
            sClass = "french";
        } else if (sFile.startsWith("ger")) {
            sClass = "german";
        } else if (sFile.startsWith("ara")) {
            sClass = "arabic";
        } else if (sFile.startsWith("hin")) {
            sClass = "hindi";
        } else if (sFile.startsWith("chi")) {
            sClass = "chinese";
        } else if (sFile.startsWith("ita")) {
            sClass = "italian";
        } else if (sFile.startsWith("jpn")) {
            sClass = "japanese";
        } else if (sFile.startsWith("tur")) {
            sClass = "turkish";
        } 
        
        return sClass;
    }
}
