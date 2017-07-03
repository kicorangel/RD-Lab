package com.autoritas.repr.ldr;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class GenerateArff {
    private int mMinFreq;
    private int mMinSize;
    private String mCorpusPath;
    private String mLDRPath;
    private String mTestPath;
    private String mOutputFile;
    private ArrayList<String> mLabels;
    private iLoadDocsxClass mLoadDocsxClass;
    private boolean mVerbose;
    private Common mCommon;
    
    public GenerateArff(int minFreq, int minSize, ArrayList<String> labels, iLoadDocsxClass loadDocsxClass, String corpusPath, String ldrPath, String testPath, String outputFile, boolean verbose) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        mLabels = labels;
        mCorpusPath = corpusPath;
        mLDRPath = ldrPath;
        mTestPath = testPath;
        mOutputFile = outputFile;
        mLoadDocsxClass = loadDocsxClass;
        mVerbose = verbose;
        
        mCommon = new Common(minFreq, minSize, labels, loadDocsxClass, corpusPath, ldrPath, verbose);
    }
    
    public void Process() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        ArrayList<String> oListOfTerms = new ArrayList<String>();
        int iTotalTerms = 0;
        
        if (mTestPath==null || mTestPath.isEmpty()) { // IN TRAINING TIME
            Hashtable<String, Integer> oTerms = new Hashtable<String, Integer>();
            for (int l=0;l<mLabels.size();l++) {
                String sLabel = mLabels.get(l);
                oTerms = mCommon.LoadCorporaFromData(sLabel, oTerms, true);
            }

            oListOfTerms = mCommon.SerializeTerms(oTerms, mMinFreq);
            iTotalTerms = oTerms.size();
            iTotalTerms = oListOfTerms.size();  // TODO REVISAR 
        } else { // IN TEST TIME
            FileReader fr = new FileReader(mLDRPath + "/ctf.txt");
            BufferedReader bf = new BufferedReader(fr);
            String sLine = "";

            while ((sLine = bf.readLine())!=null) {
                String sTerm = sLine.substring(1, sLine.indexOf(",") - 1);
                oListOfTerms.add(sTerm);
            }
            iTotalTerms = oListOfTerms.size();
        }
        
        Hashtable<String, String> oDocs;
        if (mTestPath==null || mTestPath.isEmpty()) {
            oDocs = mCommon.LoadDocuments(mLDRPath + "/tf-training.txt");
        } else {
            for (int l=0;l<mLabels.size();l++) {
                String sLabel = mLabels.get(l);
                Hashtable<String, Hashtable<String, Integer>> oDocsxClass = mLoadDocsxClass.LoadDocsxClass(mTestPath, sLabel, mMinSize, true);
                mCommon.GenerateTF(oListOfTerms, mLabels, sLabel, oDocsxClass, mLDRPath + "/tf-test.txt", true);
            }
            oDocs = mCommon.LoadDocuments(mLDRPath + "/tf-test.txt");
        }
        
         
        
        Hashtable<String, Hashtable<String, Double>> oProbCat = mCommon.LoadProbabilities(mLDRPath, mLabels);
        
        System.out.println("Writing header");
        FileWriter oFw = new FileWriter(mOutputFile);
        oFw.write(Weka.Header2Weka(mLabels));
        oFw.flush();
        
        System.out.println("Writing documents");
        int iDoc = 0;
        Enumeration docs = oDocs.keys();
        while (docs.hasMoreElements()) {
            String sDoc = (String)docs.nextElement();
            String sInfo = oDocs.get(sDoc);
            String []info = sInfo.split(",");
            String sAuthor = info[0];
            String sClass = info[1];
            
            System.out.println("-->" + (++iDoc) + "/" + oDocs.size());
            
            Hashtable<String, ProbStats> oDocProb = new Hashtable<String, ProbStats>();
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                
                if (info.length<i+2) {
                    continue;
                }
                            
                double iFreq = Double.valueOf(info[i+2]);
                
                if (iFreq==0) {
                    continue;
                }
                
                for (int l=0;l<mLabels.size();l++) {
                    String sLabel = mLabels.get(l).toLowerCase();
                    Hashtable<String, Double> oProb = oProbCat.get(sLabel);
                    if (oProb.containsKey(sTerm)) {
                        double iProb = oProb.get(sTerm);
                        
                        if (iProb!=0) {
                            ProbStats oProbStats = new ProbStats();
                            if (oDocProb.containsKey(sLabel)) {
                                oProbStats = oDocProb.get(sLabel);
                            }
                            
                            oProbStats.Max = Math.max(oProbStats.Max, iProb);
                            oProbStats.Min = Math.min(oProbStats.Min, iProb);
                            oProbStats.Sum += iProb;
                            oProbStats.N++;
                            oProbStats.Values.add(iProb);
                            if (oProbStats.Total==0){
                                oProbStats.Total = iTotalTerms;
                            }

                            oDocProb.put(sLabel, oProbStats);
                        } else {
//                            System.out.println("Term with frequency 0: " + sTerm);
                        }
                    }
                }
            }
            
            for (int l=0;l<mLabels.size();l++) {
                String sLabel = mLabels.get(l).toLowerCase();
                if (oDocProb.containsKey(sLabel)) {
                    ProbStats oProbStats = oDocProb.get(sLabel);
                    oProbStats.CalculateStats();
                    oDocProb.put(sLabel, oProbStats);
                }
                
            }
            oFw.write(Weka.Features2Weka(oDocProb,  mLabels, iTotalTerms, sClass.toUpperCase()));
        }
                
        oFw.close();
    }
}
