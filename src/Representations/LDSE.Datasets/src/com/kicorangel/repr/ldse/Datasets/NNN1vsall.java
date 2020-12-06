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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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
public class NNN1vsall implements iLoadDocsxClass  {
    private int mMinFreq;
    private int mMinSize;
    private String msCorpusPath;
    private String msLDRPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    private ArrayList<String> mLabels;
    private String msLabel;
    private SET mSet;   
    private int mLDRVersion;
    
    public NNN1vsall() {}
    
    public NNN1vsall(int minFreq, int minSize, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff, ArrayList<String> labels, String label, SET set, int LDRVersion) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        msCorpusPath = corpusPath;
        msLDRPath = LDRPath;
        msTrainingArff = trainingArff;
        msTestPath = testPath;
        msTestArff = testArff;
        mLabels = labels;
        msLabel = label;
        mSet = set;
        mLDRVersion = LDRVersion;
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
    
    public static ArrayList<String> GetLabels(String label) {
        ArrayList<String> oLabels = new ArrayList<String>();
        oLabels.add(label);
        oLabels.add("other");
        return oLabels;
    }
    
    public ArrayList<String> GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
//        oLabels.add("nas");
//        oLabels.add("nnas");
        oLabels.add("other");
        
        return oLabels;
    }
    
    private ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
//        System.out.println("Loading truth...");
        
        ArrayList<String> oTruth = new ArrayList<String>();
        
        File directory = new File(sDataPath);
        String[] authors = directory.list();
        for (int iAuthor = 0; iAuthor < authors.length; iAuthor++) {
            String sAuthor = authors[iAuthor];

//            FileReader fr = new FileReader(sDataPath + "/" + sAuthor);
            InputStream is = new FileInputStream(sDataPath + "/" + sAuthor);
            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-16"));
            BufferedReader bf = new BufferedReader(isr);
            String sCadena = "";
            while ((sCadena = bf.readLine())!=null) {
                if (sCadena.startsWith("Mother tongue:")) {
                    String []info = sCadena.split(":");
                    String sLabel = info[1].trim().toLowerCase();
                    
                    if (!sLabel.equalsIgnoreCase("arabic")) {
                        if (sLabel.equalsIgnoreCase(label)) {
                            oTruth.add(sAuthor);
                        } else if (label.equalsIgnoreCase("other") &&
                                !sLabel.equalsIgnoreCase(msLabel)) {
                            oTruth.add(sAuthor);
                        }
                    }
                    break;
                }
            }
            bf.close(); isr.close(); is.close();
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
                    InputStream is = new FileInputStream(sData + "/" + oTruth.get(iFile));
                    InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-16"));
                    BufferedReader bf = new BufferedReader(isr);
                    String sCadena = "";
                    boolean bStart = false;
                    while ((sCadena = bf.readLine())!=null) {
                        if (bStart) {
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
                        }
                        if (sCadena.startsWith("Text:")) {
                            bStart = true;
                        }
                    }
                    
                    bf.close(); isr.close(); is.close();
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        
        return oDocs;
    }
    
    public static void PrintStats(String path) throws FileNotFoundException, IOException {
        Hashtable<String, Integer> MotherTonge = new Hashtable<String, Integer>();
        
        File directory = new File(path);
        String[] files = directory.list();
        for (int iFile = 0; iFile < files.length; iFile++) {
            InputStream is = new FileInputStream(path + "/" + files[iFile]);

            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-16"));
            BufferedReader bf = new BufferedReader(isr);
            String sCadena = "";
            while ((sCadena = bf.readLine())!=null) {
                if (sCadena.startsWith("Mother tongue:")) {
                    String []data = sCadena.split(":");
                    String sMotherTonge = data[1].trim().toLowerCase();
                    
                    int iFreq = 0;
                    if (MotherTonge.containsKey(sMotherTonge)) {
                        iFreq = MotherTonge.get(sMotherTonge);
                    }
                    MotherTonge.put(sMotherTonge, ++iFreq);
                }
            }

            bf.close(); isr.close(); is.close();
        }
        
        Enumeration keys = MotherTonge.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            System.out.println(key + "\t" + MotherTonge.get(key));
        }
    }
}
