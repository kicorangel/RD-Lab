package com.autoritas.repr.ldr;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class Common {
    private int mMinFreq;
    private int mMinSize;
    private String mInputPath;
    private String mOutputPath;
    private ArrayList<String> mLabels;
    private iLoadDocsxClass mLoadDocsxClass;
    private boolean mVerbose;
    
    public Common(int minFreq, int minSize, ArrayList<String> labels, iLoadDocsxClass loadDocsxClass, String inputPath, String outputPath, boolean verbose) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        mLabels = labels;
        mInputPath = inputPath;
        mOutputPath = outputPath;
        mLoadDocsxClass = loadDocsxClass;
        mVerbose = verbose;
    }
    
    public Hashtable<String, Integer> LoadCorporaFromData(String sLabel, Hashtable<String, Integer> oCorpora, boolean verbose) throws FileNotFoundException, IOException, ParserConfigurationException, SAXException { 
        if (verbose) {
            System.out.println("Loading " + sLabel);
        }
        
        Hashtable<String, Hashtable<String, Integer>> oDocsxClass = mLoadDocsxClass.LoadDocsxClass(mInputPath, sLabel, mMinSize, true);
        Enumeration docs = oDocsxClass.keys();
        
        while (docs.hasMoreElements()) {
            String sAuthor = (String)docs.nextElement();
            Hashtable<String, Integer> oTerms = oDocsxClass.get(sAuthor);
            Enumeration terms = oTerms.keys();
            while (terms.hasMoreElements()) {
                String sTerm = (String)terms.nextElement();
                int iFreqCorpus = 0;
                if (oCorpora.containsKey(sTerm)) {
                    iFreqCorpus = oCorpora.get(sTerm);
                }
                int iFreqDoc = 0;
                if (oTerms.containsKey(sTerm)) {
                    iFreqDoc = oTerms.get(sTerm);
                }
                oCorpora.put(sTerm, iFreqCorpus+iFreqDoc);
            }
        }
        
        return oCorpora;
    }
    
    public ArrayList<String> SerializeTerms(Hashtable<String, Integer> oTerms, int minFreq ){
        ArrayList<String> oListOfTerms = new ArrayList<String>(oTerms.keySet());
        Collections.sort(oListOfTerms);
        
        if (minFreq>1) {
            for (int i=oListOfTerms.size()-1;i>=0;i--) {
                String sTerm = oListOfTerms.get(i);
                int iFreq = oTerms.get(sTerm);
                if (iFreq<minFreq) {
                    oListOfTerms.remove(i);
                }
            }
        }

        return oListOfTerms;
    }
    
    public Hashtable<String, String> LoadDocuments(String path) throws IOException {
        Hashtable<String, String> oDocs = new Hashtable<String, String>();
        
        FileReader fr = new FileReader(path);
        BufferedReader bf = new BufferedReader(fr);
        
        String sLine = "";
        while ((sLine = bf.readLine()) != null) {
//            String []info = sLine.split(",");
//            String sName = info[0];
            String sName = sLine.substring(0, sLine.indexOf(","));
            
            oDocs.put(sName, sLine);
        }
        
        return oDocs;
    }
    
    public static Hashtable<String, Hashtable<String, Double>>LoadProbabilities(String path, ArrayList<String> Labels) throws FileNotFoundException, IOException {
        Hashtable<String, Hashtable<String, Double>> oProbCat = new Hashtable<String, Hashtable<String, Double>>();
        for (int l=0;l<Labels.size();l++) {
            String label = Labels.get(l);
            Hashtable<String, Double> oProb = new Hashtable<String, Double>();
            String sPathToClusterInfo = path + "/prob_" + label.toLowerCase() + ".txt";
            FileReader fr = new FileReader(sPathToClusterInfo);
            BufferedReader bf = new BufferedReader(fr);
            String sLine = "";

            while ((sLine = bf.readLine())!=null){
                String []info = sLine.split(":::");
                oProb.put(info[0], Double.valueOf(info[1]));
            }
            oProbCat.put(label, oProb);
        }
        return oProbCat;
    }
    
    public void GenerateTF(ArrayList<String>oListOfTerms, ArrayList<String>Labels, String sClass, Hashtable<String, Hashtable<String, Integer>>oDocsxClass, String sOutput, boolean verbose) throws FileNotFoundException, IOException, ParserConfigurationException, SAXException {
        FileWriter oFW = new FileWriter(sOutput, true);
        
        Enumeration docs = oDocsxClass.keys();
        int iDoc = 1;
        while (docs.hasMoreElements()) {
            String sAuthor = (String)docs.nextElement();
            
            if (verbose) {
                System.out.println("--> " + sClass + ": "  + iDoc + "/" + sAuthor);
                iDoc++;
            }
            
            
            Hashtable<String, Integer> oFreq = oDocsxClass.get(sAuthor);
            Hashtable<String, Integer> oDoc = new Hashtable<String, Integer>();
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                if (oFreq.containsKey(sTerm)) {
                    int iDocFreq = oFreq.get(sTerm);
                    oDoc.put(sTerm, iDocFreq);
                }
            }

            // Write doc frequencies: list of tf
            StringBuilder Line = new StringBuilder();
            String sLine = sAuthor + "," + sClass + ",";
            int length = oListOfTerms.size();
            for (int i=0;i<length;i++) {
                String sTerm = oListOfTerms.get(i);

                if (oDoc.containsKey(sTerm)) {
                    int iFreq = oDoc.get(sTerm);
                    Line.append(iFreq);
                } else {
                    Line.append("0");
                }

                if (i<length-1) {
                    Line.append(",");
                } else {
                    Line.append("\n");
                }
            }
            sLine += Line.toString();
            oFW.write(sLine);
        }

        oFW.close();
    }
}
