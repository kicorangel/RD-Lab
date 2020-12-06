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
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class Replab14 implements iLoadDocsxClass  {
    private int mMinFreq;
    private int mMinSize;
    private String msCorpusPath;
    private String msLDRPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    
    private int msConfig;       // 1: INFLUENCER VS. NO INFLUENCER
                                // 2: ALL GROUPS
                                // 3: public: PUBLIC INSTITUTIOS + NGO
                                //    company:  PRIVATE COMPANIES + EMPLOYEE
                                //    celebrity:  CELEBRITIES & SPORTMEN & 
                                //    journ-prof:  JOURNALIST & PROFESSIONALS
                                //      UNDECIDABLE
                                // 4: JOURNALIST VS. PROFESSIONAL
                                // 5: JOURNALIST + PROFESSIONAL vs. OTHERs
    
    private String msDomain;    // D01:
                                // D02:
    
    private boolean mbIncludeUndecidable;
    private SET mSET; 
    private Hashtable<String, String> mInfluencer;
    
    private int mLDRVersion;
    
    public Replab14(int minFreq, int minSize, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff, int config, String domain, boolean includeUndecidable, SET set, int LDRVersion) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        msCorpusPath = corpusPath;
        msLDRPath = LDRPath;
        msTrainingArff = trainingArff;
        msTestPath = testPath;
        msTestArff = testArff;
        msConfig = config;
        msDomain = domain;
        mbIncludeUndecidable = includeUndecidable;
        mInfluencer = ReadInfluencer();
        mSET = set;
        mLDRVersion = LDRVersion;
    }
    
    
    public void Run() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        ArrayList<String> Labels = GetLabels();
        // Step 1: Prepare LDR weights and probabilities
        if (mSET==SET.BOTH || mSET==SET.TRAINING) {
            Prepare oPrepare = new Prepare(mMinFreq, mMinSize, Labels, this, msCorpusPath, msLDRPath, true);
            oPrepare.Process();
        }   
        
        // Step 2: Generate training ARFF
        if (mSET==SET.BOTH || mSET==SET.TRAINING) {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, Labels,  this, msCorpusPath, msLDRPath, "", msTrainingArff, mLDRVersion, true);
            oGenerate.Process();
        }
        
        // Step 3: Generate test ARFF
        if (mSET==SET.BOTH || mSET==SET.TEST) {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, Labels, this, msCorpusPath, msLDRPath, msTestPath, msTestArff, mLDRVersion, true);
            oGenerate.Process();
        }
    }
    
    public ArrayList<String> GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
        if (msConfig==2) {
            if (mbIncludeUndecidable) {
                oLabels.add("undecidable");
            }
            oLabels.add("professional");
            oLabels.add("journalist");

            oLabels.add("public_institutions");
            oLabels.add("ngo");
            oLabels.add("company");
            oLabels.add("sportsmen");
            oLabels.add("celebrity");

    //        oLabels.add("stockholder");
    //        oLabels.add("investor");
            oLabels.add("employee");
        } else if (msConfig==1) {
            oLabels.add("0");
            oLabels.add("1");
        } else if (msConfig==3) {
            oLabels.add("public");
            oLabels.add("company");
            oLabels.add("celebrity");
            oLabels.add("journ-prof");
            if (mbIncludeUndecidable) {
                oLabels.add("undecidable");
            }
        } else if (msConfig==4) {
            oLabels.add("professional");
            oLabels.add("journalist");
        } else if (msConfig==5) {
            oLabels.add("journ-prof");
            oLabels.add("others");
            if (mbIncludeUndecidable) {
                oLabels.add("undecidable");
            }
        }
        
        return oLabels;
    }
    
    private ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        String sFileName = sDataPath + "/goldstandard_author_categorization.dat";
        
        if (msConfig==1) {
            FileReader fr = new FileReader(sFileName);
            BufferedReader bf = new BufferedReader(fr);
            String sCadena = "";

            while ((sCadena = bf.readLine())!=null) {
                sCadena = sCadena.replaceAll("\"", "");
                String []data = sCadena.split("\t");
                
                String sUser = data[1];
                if (mInfluencer.containsKey(sUser.toLowerCase())) {
                    String inf = mInfluencer.get(sUser.toLowerCase());

                    if (inf.equals(label)) {
                        oTruth.add(sUser);
                    }
                } else {
//                    System.out.println(sUser);
                }
            }
            
        } else {
            sFileName = sDataPath + "/goldstandard_author_categorization." + msConfig + ".dat";
            
            FileReader fr = new FileReader(sFileName);
            BufferedReader bf = new BufferedReader(fr);
            String sCadena = "";

            while ((sCadena = bf.readLine())!=null) {
                sCadena = sCadena.replaceAll("\"", "");
                String []data = sCadena.split("\t");

                if (data.length==3 && data[2].equalsIgnoreCase(label)) {
                    if (data[0].endsWith(msDomain)) {
                        if (sDataPath.contains("test") && mInfluencer.containsKey(data[1].toLowerCase()) && mInfluencer.get(data[1].toLowerCase()).equals("1")) {
                            oTruth.add(data[1]);
                        } else if (sDataPath.contains("training")) {
                            oTruth.add(data[1]);
                        }
                    }
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
        
            for (int iFile=0;iFile<oTruth.size();iFile++) {
                String sAuthor = oTruth.get(iFile);
                if (verbose) {
                    System.out.println(label + ": " + (iFile + 1) + "/" + oTruth.size());
                }
                
                try { 
                    FileReader fr = new FileReader(sData + "/text/" + oTruth.get(iFile) + "_texts.tsv");
                    BufferedReader bf = new BufferedReader(fr);
                    String sCadena = "";
                    int iLine = 0;
                    while ((sCadena = bf.readLine())!=null) {
                        if (iLine==0) {
                            iLine++;
                            continue;
                        }
                        sCadena = sCadena.replaceAll("\"", "");
                        String []data = sCadena.split("\t");
                        if (data.length==4) {
                            String sHtmlContent = data[3];
                            String sContent = Tools.GetText(sHtmlContent);
                            sContent = Tools.Prepare(sContent);

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
                        iLine++;
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
    
    
    private Hashtable<String, String> ReadInfluencer() {
        Hashtable<String, String> oInfluencers = new Hashtable<String, String>();
        try {
            if (msDomain.equalsIgnoreCase("D01")) {
                oInfluencers = ReadInfluencer("/mnt/data/RepLab/replab_dataset/RL2014D01/RL2014D01_train.txt", oInfluencers);
                oInfluencers = ReadInfluencer("/mnt/data/RepLab/replab_dataset/RL2014D01/RL2014D01_test.txt", oInfluencers);
            } else {
                oInfluencers = ReadInfluencer("/mnt/data/RepLab/replab_dataset/RL2014D02/RL2014D02_train.txt", oInfluencers);
                oInfluencers = ReadInfluencer("/mnt/data/RepLab/replab_dataset/RL2014D02/RL2014D02_test.txt", oInfluencers);
            }            
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return oInfluencers;
    }
    
    private static Hashtable<String, String> ReadInfluencer(String fileName, Hashtable<String, String> oInfluencers) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(fileName);
        BufferedReader bf = new BufferedReader(fr);
        String sCadena = "";
        while ((sCadena = bf.readLine())!=null) {
            String []data = sCadena.split("\t");
            String bInf = data[1];
            String sInf = data[3].toLowerCase();
            
            if (oInfluencers.containsKey(sInf)) {
                String b = oInfluencers.get(sInf);
                if (!b.equalsIgnoreCase(bInf)) {
                    System.out.println(sInf);
                }
            } else {
                oInfluencers.put(sInf, bInf);
            }
        }
    
        return oInfluencers;
    }
}
