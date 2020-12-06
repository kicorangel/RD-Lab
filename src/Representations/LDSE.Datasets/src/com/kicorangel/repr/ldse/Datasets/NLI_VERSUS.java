/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kicorangel.repr.ldse.Datasets;

import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.ldse.GenerateArff;
import com.kicorangel.repr.ldse.Prepare;
import com.kicorangel.repr.base.Tools;
import com.kicorangel.repr.base.iLoadDocsxClass;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class NLI_VERSUS implements iLoadDocsxClass  {
    private int mMinFreq;
    private int mMinSize;
    private String msCorpusPath;
    private String msLDRPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    private ArrayList<String> mLabels;
    private SET mSet;   
    private int mLDRVersion;
    
    public NLI_VERSUS() {}
    
    public NLI_VERSUS(int minFreq, int minSize, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff, ArrayList<String> labels, SET set, int LDRVersion) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        msCorpusPath = corpusPath;
        msLDRPath = LDRPath;
        msTrainingArff = trainingArff;
        msTestPath = testPath;
        msTestArff = testArff;
        mLabels = labels;
        mSet = set;
        mLDRVersion = LDRVersion;
    }
    
    public void RefineDataset(String lang, String evalDataset, String srcFolder, int size) {
        ArrayList<String> oFiles4Lang = new ArrayList<String>();
        ArrayList<String> oFiles4Other = new ArrayList<String>();
        Hashtable<String, String> oFiles2Delete = new Hashtable<String, String>();
        
        File corpus = new File(srcFolder + "/" + lang + "/" + evalDataset + "/training/");
        String []files = corpus.list();
        if (files!=null) {
            for (int iFile=0;iFile<files.length;iFile++) {
                String sFileName = files[iFile];
                String []info = sFileName.split("-");
                if (info.length>1) {
                    String sLang = info[1];
                    if (sLang.equalsIgnoreCase(lang)) {
                        oFiles4Lang.add(sFileName);
                    } else {
                        oFiles4Other.add(sFileName);
                    }
                }
            }
        }
        
        Random oRnd = new Random();
        for (int i=size;i<oFiles4Lang.size();i++) {
            boolean bRepeat = true;
            do {
                int iFile = oRnd.nextInt(oFiles4Lang.size());
                String sFile = oFiles4Lang.get(iFile);
                if (!(bRepeat = oFiles2Delete.containsKey(sFile))) {
                    oFiles2Delete.put(sFile, sFile);
                }
                
            } while(bRepeat);
        }
        
        for (int i=size;i<oFiles4Other.size();i++) {
            boolean bRepeat = true;
            do {
                int iFile = oRnd.nextInt(oFiles4Other.size());
                String sFile = oFiles4Other.get(iFile);
                if (!(bRepeat = oFiles2Delete.containsKey(sFile))) {
                    oFiles2Delete.put(sFile, sFile);
                }
                
            } while(bRepeat);
        }
        
        Enumeration keys = oFiles2Delete.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            String sFileName = (String)key;
            String sFile2Delete = srcFolder + "/" + lang + "/" + evalDataset + "/training/" + sFileName;
            new File(sFile2Delete).delete();
        }
        
    }
    
    public void PrepareDataset(String lang, String evalDataset, String srcFolder, String dstFolder) throws IOException {
        if (new File(srcFolder + "/" + lang + "/" + evalDataset + "/").exists()) {
            {
                // Copy test
                String sSrcFolder = srcFolder + "/" + lang + "/" + evalDataset + "/";
                String sDstFolder = dstFolder + "/" + lang + "/" + evalDataset + "/test/";
                File testCorpus = new File(sSrcFolder);
                String []files = testCorpus.list();
                System.out.print("Copying test set for " + lang + " on " + evalDataset + " (" + files.length + ")");
                for (int iFile=0;iFile<files.length;iFile++) {
                    FileUtils.copyFile(new File(sSrcFolder + files[iFile]), new File(sDstFolder + files[iFile]));
                    if ((iFile>0) && (iFile%1000)==0) {
                        System.out.print("*");
                    } else if ((iFile>0) && (iFile%100)==0) {
                        System.out.print(".");
                    }
                }
                System.out.println();
            }
            
            

            {
                // Copy training
                String sSrcFolder = srcFolder + "/" + lang + "/";
                String sDstFolder = dstFolder + "/" + lang + "/" + evalDataset + "/training/";
                File directory = new File(sSrcFolder);
                String [] dir = directory.list();

                for (int iDir=0;iDir<dir.length;iDir++) {
                    String sDirName = dir[iDir];
                    if (!sDirName.equalsIgnoreCase(evalDataset)) {
                        File testCorpus = new File(sSrcFolder + "/" + sDirName + "/");
                        if (testCorpus.isDirectory()) {
                            String []files = testCorpus.list();
                            System.out.print("Copying training set for " + lang + " on " + sDirName + " (" + files.length + ")");
                            for (int iFile=0;iFile<files.length;iFile++) {
                                FileUtils.copyFile(new File(sSrcFolder + "/" + sDirName + "/" + files[iFile]), new File(sDstFolder + files[iFile]));
                                if ((iFile>0) && (iFile%1000)==0) {
                                    System.out.print("*");
                                } else if ((iFile>0) && (iFile%100)==0) {
                                    System.out.print(".");
                                }
                            }
                            
                            System.out.println();
                        }
                    }
                }
                
            }
        }
    }
    
    
    public void Run() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        if (mLabels==null) {
            mLabels = GetLabels();
        }
        // Step 1: Prepare LDR weights and probabilities
        if (mSet==SET.TRAINING || mSet==SET.BOTH) {
            Prepare oPrepare = new Prepare(mMinFreq, mMinSize, mLabels, this, msCorpusPath, msLDRPath, true);
            oPrepare.Process();
        }   
        
        // Step 2: Generate training ARFF
        if (mSet==SET.TRAINING || mSet==SET.BOTH) {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, mLabels,  this, msCorpusPath, msLDRPath, "", msTrainingArff, mLDRVersion, true);
            oGenerate.Process();
        }
        
        // Step 3: Generate test ARFF
        if (mSet==SET.TEST || mSet==SET.BOTH) {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, mLabels, this, msCorpusPath, msLDRPath, msTestPath, msTestArff, mLDRVersion, true);
            oGenerate.Process();
        }
    }
    
    public ArrayList<String> GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
        
        
        return oLabels;
    }
    
    private ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        
        File dirCorpus = new File(sDataPath);
        String []files = dirCorpus.list();
        for (int iFile=0;iFile<files.length;iFile++) {
            String sFileName = files[iFile].replace(".txt", "");
            String []info = sFileName.split("-");
            if (info.length>1) {
                String sLang = info[1];

                if (sLang.equalsIgnoreCase(label)) {
                    oTruth.add(sFileName);
                }
            }
        }
        
         
        return oTruth;
    }
    
    
    
    public Hashtable<String, Hashtable<String, Integer>> LoadDocsxClass(String sData, String label, int minSize, boolean verbose) {
        Hashtable<String, Hashtable<String, Integer>> oDocs = new Hashtable<String, Hashtable<String, Integer>>();

        ArrayList<String> oTruth = new ArrayList<String>();
        
        try {
            oTruth = LoadTruth(sData, label);
        
            for (int iFile=0;(iFile<oTruth.size()) /*&& (iFile<25000)*/;iFile++) {
                String sAuthor = oTruth.get(iFile);
                if (verbose) {
                    if ((iFile+1)%1000==0) {
                        System.out.println(label + ": " + (iFile + 1) + "/" + oTruth.size());
                    }
                }
                
                try { 
                    FileReader fr = new FileReader(sData + "/" + oTruth.get(iFile) + ".txt");
                    BufferedReader bf = new BufferedReader(fr);
                    String sCadena = "";
//                    int iLine = 0;
                    while ((sCadena = bf.readLine())!=null) {
//                        if (iLine==0) {
//                            iLine++;
//                            continue;
//                        }
                        sCadena = sCadena.replaceAll("\"", "");
//                        String []data = sCadena.split("\t");
//                        if (data.length==4) {
//                            String sHtmlContent = data[3];
//                            String sContent = Tools.GetText(sHtmlContent);
                            String sContent = Tools.Prepare(sCadena);

                            String[] tokens = sContent.split(" ");
                            for (String t : tokens) {
                                String sLemma = t.toLowerCase().trim();

                                if (sLemma.length() < minSize) {
                                    continue;
                                }
                                Hashtable<String, Integer> oFreq = new Hashtable<String, Integer>();
                                if (oDocs.containsKey(sAuthor)) {
                                    oFreq = oDocs.get(sAuthor);
                                }
                                int iFreq = 0;
                                if (oFreq.containsKey(sLemma)) {
                                    iFreq = oFreq.get(sLemma);
                                }
                                oFreq.put(sLemma, ++iFreq);
                                oDocs.put(sAuthor, oFreq);
                            }
                            
//                        }
//                        iLine++;
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        
        return oDocs;
    }
}
