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
public class Prepare {
    private int mMinFreq;
    private int mMinSize;
    private String mInputPath;
    private String mOutputPath;
    private ArrayList<String> mLabels;
    private iLoadDocsxClass mLoadDocsxClass;
    private boolean mVerbose;
    private Common mCommon;
    
    public Prepare(int minFreq, int minSize, ArrayList<String> labels, iLoadDocsxClass loadDocsxClass, String inputPath, String outputPath, boolean verbose) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        mLabels = labels;
        mInputPath = inputPath;
        mOutputPath = outputPath;
        mLoadDocsxClass = loadDocsxClass;
        mVerbose = verbose;
        mCommon = new Common(minFreq, minSize, labels, loadDocsxClass, inputPath, outputPath, verbose);
    }
    
    public void Process() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        Hashtable<String, Integer> oTerms = new Hashtable<String, Integer>();
        for (int l=0;l<mLabels.size();l++) {
            String sLabel = mLabels.get(l);
            oTerms = mCommon.LoadCorporaFromData(sLabel, oTerms, true);
        }
        
        ArrayList<String> oListOfTerms = mCommon.SerializeTerms(oTerms, mMinFreq);
        SaveCTF(oListOfTerms, oTerms, mOutputPath + "/ctf.txt");
        for (int l=0;l<mLabels.size();l++) {
            String sLabel = mLabels.get(l);
            Hashtable<String, Hashtable<String, Integer>> oDocsxClass = mLoadDocsxClass.LoadDocsxClass(mInputPath, sLabel, mMinSize, true);
            mCommon.GenerateTF(oListOfTerms, mLabels, sLabel, oDocsxClass, mOutputPath + "/tf-training.txt", true);
        }
        Hashtable<String, String> oDocs = mCommon.LoadDocuments(mOutputPath + "/tf-training.txt");
        GenerateWeights(oDocs, oTerms, oListOfTerms, mOutputPath + "/weights.txt", true);
        Hashtable<String, String> oWeights = LoadWeights(mOutputPath + "/weights.txt");
        GenerateTX(oDocs, oListOfTerms, oWeights, mLabels, mOutputPath, true);
        Hashtable<String, Hashtable<String, Double>> oTxCat = LoadTxCat(mLabels, mOutputPath);
        GenerateProbabilities(oListOfTerms, oTxCat, mLabels, mOutputPath, true);
    }
    
    
    
    private void SaveCTF(ArrayList<String> oListOfTerms, Hashtable<String, Integer> oTerms, String path) {
        try {
            FileWriter oFw = new FileWriter(path);
            
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                int iFreq = oTerms.get(sTerm);
                oFw.write("\"" + sTerm + "\",\"" + iFreq + "\"\n");
            }
            oFw.flush();
            oFw.close();
        } catch (Exception ex) {
            
        }
    }
    
    
    private void GenerateWeights(Hashtable<String, String> oDocs, Hashtable<String, Integer>oTerms, ArrayList<String>oListOfTerms, String sOutput, boolean verbose) throws IOException {
        int N = oDocs.size();
        Enumeration docs = oDocs.keys();
        FileWriter oFW = new FileWriter(sOutput);
        int iFile = 0;
        while (docs.hasMoreElements()) {
            String sDoc = (String)docs.nextElement();
            String sInfo = oDocs.get(sDoc);
            String []info = sInfo.split(",");
            String sAuthor = info[0];
            String sClass = info[1];
            
            if (verbose) {
                System.out.println("-->" + (iFile+1) + "/" + N);
                iFile++;
            }
            
            StringBuilder Weight = new StringBuilder();
            String sWeight = sAuthor + "," + sClass + ",";
            int length = oListOfTerms.size();
            for (int i=0;i<length;i++) {
                String sTerm = oListOfTerms.get(i);
                double iTF = Double.valueOf(info[i+2]);
                double iCTF = oTerms.get(sTerm);
                
                double weight = Math.log(iTF+1) * Math.log(N / (1+iCTF));
                
                Weight.append(weight);
                if (i<length-1) {
                    Weight.append(",");
                } else {
                    Weight.append("\n");
                }
            }
            sWeight += Weight.toString();
            oFW.write(sWeight);
            oFW.flush();
        }
        oFW.close();
    }
    
    private Hashtable<String, String> LoadWeights(String path) throws IOException {
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
    
    private void GenerateTX(Hashtable<String, String> oDocs, ArrayList<String>oListOfTerms, Hashtable<String, String> oWeights, ArrayList<String> Labels, String sOutput, boolean verbose) throws IOException {
        Hashtable<String, Hashtable<String, Double>> oTxCat = new Hashtable<String, Hashtable<String, Double>>();
        Enumeration docs = oWeights.keys();
            
        int iDoc = 1;
        while (docs.hasMoreElements()) {
            if (verbose) {
                System.out.println("-->" + iDoc + "/" + oWeights.size());
                iDoc++;
            }
            String sDoc = (String)docs.nextElement();
            String sInfo = oDocs.get(sDoc);
            String []info = sInfo.split(",");
            String sAuthor = info[0];
            String sClass = info[1].toLowerCase();
                
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                double weight = Double.valueOf(info[i+2]);

                Hashtable<String, Double>oTx = new Hashtable<String, Double>();
                if (oTxCat.containsKey(sClass)) {
                    oTx = oTxCat.get(sClass);
                }
                double t = 0;
                if (oTx.containsKey(sTerm)) {
                    t = oTx.get(sTerm);
                }
                t += weight;
                oTx.put(sTerm, t);
                oTxCat.put(sClass, oTx);
            }
        }
        
        for (int l=0;l<Labels.size();l++) {
            String sClass = Labels.get(l).toLowerCase();
            FileWriter oFW = new FileWriter(sOutput + "/tx_" + sClass.toLowerCase() + ".txt");
            Hashtable<String, Double>oTx = oTxCat.get(sClass);
            
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                double iAccWeight = 0;
                if (oTx.containsKey(sTerm)) {
                    iAccWeight = oTx.get(sTerm);
                } 
                oFW.write(sTerm + ":::" + iAccWeight + "\n");
            }
            oFW.close();
        }
    }
    
    private Hashtable<String, Hashtable<String, Double>>LoadTxCat(ArrayList<String> Labels, String path) throws FileNotFoundException, IOException {
        Hashtable<String, Hashtable<String, Double>> oTxCat = new Hashtable<String, Hashtable<String, Double>>();
        for (int l=0;l<Labels.size();l++) {
            String sClass = Labels.get(l);
            Hashtable<String, Double> oTx = new Hashtable<String, Double>();
            String sPathToClusterInfo = path + "/tx_" + sClass.toLowerCase() + ".txt";
            FileReader fr = new FileReader(sPathToClusterInfo);
            BufferedReader bf = new BufferedReader(fr);
            String sLine = "";

            while ((sLine = bf.readLine())!=null){
                String []info = sLine.split(":::");
                oTx.put(info[0], Double.valueOf(info[1]));
            }
            oTxCat.put(sClass, oTx);
        }
        return oTxCat;
    }
    
    private void GenerateProbabilities(ArrayList<String>oListOfTerms, Hashtable<String, Hashtable<String, Double>> oTxCat, ArrayList<String> Labels, String sOutput, boolean verbose) throws IOException {
        Hashtable<String, Hashtable<String, Double>> oProbCat = CalculateProbabilities(oListOfTerms, oTxCat, Labels);
        for (int l=0;l<Labels.size();l++) {
            String sClass = Labels.get(l);
            FileWriter oFW = new FileWriter(sOutput + "/prob_" + sClass.toLowerCase() + ".txt");
            Hashtable<String, Double>oProb = oProbCat.get(sClass);
            
            if (verbose) {
                System.out.println("-->" + sClass);
            }
            
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                double iProb = 0;
                if (oProb.containsKey(sTerm)) {
                    iProb = oProb.get(sTerm);
                } 
                oFW.write(sTerm + ":::" + iProb + "\n");
                oFW.flush();
            }
            oFW.close();
        }
    }
    
    private Hashtable<String, Hashtable<String, Double>> CalculateProbabilities(ArrayList<String> oListOfTerms, Hashtable<String, Hashtable<String, Double>> oTxCat, ArrayList<String> Labels) {
        Hashtable <String, Hashtable<String, Double>> oProbCat = new Hashtable<String, Hashtable<String, Double>>();
        
        for (int i=0;i<oListOfTerms.size();i++) {
            String sTerm = oListOfTerms.get(i);
            for (int l1=0;l1<Labels.size();l1++) {
                String classTarget = Labels.get(l1).toLowerCase();
                Hashtable<String, Double>oTxTarget = oTxCat.get(classTarget);
                double ta = 0;
                double tc = 0;
                if (oTxTarget.containsKey(sTerm)) {
                    ta = oTxTarget.get(sTerm);
                }
                for (int l2=0;l2<Labels.size();l2++) {
                    String classOther = Labels.get(l2).toLowerCase();
                    if (classTarget.equalsIgnoreCase(classOther)) {
                        continue;
                    }
                    Hashtable<String, Double>oTxOther = oTxCat.get(classOther);
                    
                    if (oTxOther.containsKey(sTerm)) {
                        tc += oTxOther.get(sTerm);
                    }
                }
                double prob = 0;
                if (ta+tc>0) {
                    prob = ta / (ta + tc);
                } else {
                    prob = ta / (ta + tc + 1);
                }
                Hashtable<String, Double> oProbTerm = new Hashtable<String, Double>();
                if (oProbCat.containsKey(classTarget)) {
                    oProbTerm = oProbCat.get(classTarget);
                }
                oProbTerm.put(sTerm, prob);
                oProbCat.put(classTarget, oProbTerm);
            }
        }
        
        return oProbCat;
    }
}
