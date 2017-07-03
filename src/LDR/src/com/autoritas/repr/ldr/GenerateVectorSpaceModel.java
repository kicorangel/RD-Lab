package com.autoritas.repr.ldr;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class GenerateVectorSpaceModel {
    private ArrayList<String> mLabels;
    private String mLDRPath;
    private int mMinSize;
    private int mMinFreq;
    private String mText;
    
    private ArrayList<String> mListOfTerms;
    private Hashtable<String, Double> mDoc;
    private Hashtable<String, ProbStats> mDocProb;
    private Hashtable<String, Hashtable<String, Double>> mProbCat;
    
    private Instance mInstance;
    
    private Common mCommon;
    
    public GenerateVectorSpaceModel(ArrayList<String> labels, String ldrPath, int minSize) throws IOException {
        mLabels = labels;
        mLDRPath = ldrPath;
        mMinSize = minSize;
        mCommon = new Common(1, minSize, labels, null, "", ldrPath, false);
        mProbCat = mCommon.LoadProbabilities(mLDRPath, mLabels);
        LoadListOfTerms();
    }
    
    public Instance GetInstance() {
        return mInstance;
    }
    
    public ArrayList<String> GetListOfTerms() {
        return mListOfTerms;
    }
    
    public Hashtable<String, Double> GetDocument() {
        return mDoc;
    }
    public Hashtable<String, ProbStats> GetDocumentProbabilities() {
        return mDocProb;
    }
    
    public Hashtable<String, Hashtable<String, Double>> GetProbabilities() {
        return mProbCat;
    } 
    
    public void Clear() {
        mInstance = null;
        mDoc = new Hashtable<String, Double>();
        mDocProb = new Hashtable<String, ProbStats>();
    }
    
    public void Process(String text) throws FileNotFoundException, IOException {
        mText = text;
        Clear();
        
        GenerateDocTF();
        CalculateDocProbabilities();
        CreateInstance();
    }
    
    private void LoadListOfTerms() throws FileNotFoundException, IOException {
        mListOfTerms = new ArrayList<String>();
        FileReader fr = new FileReader(mLDRPath + "/ctf.txt");
        BufferedReader bf = new BufferedReader(fr);
        String sLine = "";

        while ((sLine = bf.readLine())!=null) {
            String sTerm = sLine.substring(1, sLine.indexOf(",") - 1);
            mListOfTerms.add(sTerm);
        }
    }
    
    private void GenerateDocTF() {
        mDoc = new Hashtable<String, Double>();
        String sHtmlContent = mText;
        String sContent = Tools.GetText(sHtmlContent);
        sContent = Tools.Prepare(sContent);

        String[] tokens = sContent.split(" ");
        for (String t : tokens) {
            String sLemma = t.toLowerCase().trim();

            if (sLemma.length() < mMinSize) {
                continue;
            }
            double iFreq = 0;
            if (mDoc.containsKey(sLemma)) {
                iFreq = mDoc.get(sLemma);
            }
            mDoc.put(sLemma, ++iFreq);
        }
    }
    
    private void CalculateDocProbabilities() {
        mDocProb = new Hashtable<String, ProbStats>();
        for (int i=0;i<mListOfTerms.size();i++) {
            String sTerm = mListOfTerms.get(i);

            //double iFreq = Double.valueOf(info[i+2]);
            double iFreq = 0;
            if (mDoc.containsKey(sTerm)) {
                iFreq = (Double)mDoc.get(sTerm);
            }

            if (iFreq==0) {
                continue;
            }

            for (int l=0;l<mLabels.size();l++) {
                String sLabel = mLabels.get(l).toLowerCase();
                Hashtable<String, Double> oProb = mProbCat.get(sLabel);
                if (oProb.containsKey(sTerm)) {
                    double iProb = oProb.get(sTerm);

                    if (iProb!=0) {
                        ProbStats oProbStats = new ProbStats();
                        if (mDocProb.containsKey(sLabel)) {
                            oProbStats = mDocProb.get(sLabel);
                        }

                        oProbStats.Max = Math.max(oProbStats.Max, iProb);
                        oProbStats.Min = Math.min(oProbStats.Min, iProb);
                        oProbStats.Sum += iProb;
                        oProbStats.N++;
                        oProbStats.Values.add(iProb);
                        if (oProbStats.Total==0){
                            oProbStats.Total = mListOfTerms.size();
                        }

                        mDocProb.put(sLabel, oProbStats);
                    } else {
//                            System.out.println("Term with frequency 0: " + sTerm);
                    }
                }
            }
        }

        for (int l=0;l<mLabels.size();l++) {
            String sLabel = mLabels.get(l).toLowerCase();
            if (mDocProb.containsKey(sLabel)) {
                ProbStats oProbStats = mDocProb.get(sLabel);
                oProbStats.CalculateStats();
                mDocProb.put(sLabel, oProbStats);
            }
        }
    }
    
    
    private void CreateInstance() {
        int nAttrib = mLabels.size() * 6 + 1;
        int nClasses = mLabels.size();
        int iClass = nAttrib - 1;
        try
        {
            FastVector attInfo = new FastVector(nAttrib);
            FastVector oFv = new FastVector(nClasses);
            for (int i=0;i<mLabels.size();i++) {
                String sLabel = mLabels.get(i);
                attInfo.addElement(new Attribute(sLabel + "-avg"));
                attInfo.addElement(new Attribute(sLabel + "-std"));
                attInfo.addElement(new Attribute(sLabel + "-min"));
                attInfo.addElement(new Attribute(sLabel + "-max"));
                attInfo.addElement(new Attribute(sLabel + "-prob"));
                attInfo.addElement(new Attribute(sLabel + "-prop"));
                
                oFv.addElement(sLabel);
            }
            attInfo.addElement(new Attribute("class", oFv));

            Instances instances = new Instances("LDRAttributes", attInfo, nAttrib);
            instances.setClassIndex(iClass);

            Instance inst = new DenseInstance(nAttrib);
            
            int iAttrib = 0;
            for (int i=0;i<nClasses;i++) {
                String sLabel = mLabels.get(i);
                ProbStats oProbStats = mDocProb.get(sLabel);
                
                inst.setValue(iAttrib++, oProbStats.Avg);
                inst.setValue(iAttrib++, oProbStats.Std);
                inst.setValue(iAttrib++, oProbStats.Min);
                inst.setValue(iAttrib++, oProbStats.Max);
                inst.setValue(iAttrib++, oProbStats.Sum/oProbStats.Total);
                inst.setValue(iAttrib++, oProbStats.N/oProbStats.Total);
            }
            
//            inst.setValue(iClass, 0);
            instances.add(inst);

            mInstance = instances.instance(0);
        }
        catch (Exception ex)
        {
        }
    }
}
