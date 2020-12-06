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
public class TOEFL implements iLoadDocsxClass  {
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
    private String msLang;  // If no lang, then all the langs
    
    public TOEFL(int minFreq, int minSize, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff, ArrayList<String> labels, String lang, SET set, int LDRVersion) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        msCorpusPath = corpusPath;
        msLDRPath = LDRPath;
        msTrainingArff = trainingArff;
        msTestPath = testPath;
        msTestArff = testArff;
        mLabels = labels;
        mLDRVersion = LDRVersion;
        mSet = set;
        msLang = lang;
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
        
        if (msLang.isEmpty()) {
            oLabels.add("ara");
            oLabels.add("hin");
            oLabels.add("kor");
            oLabels.add("tel");
            oLabels.add("chi");
            oLabels.add("fre");
            oLabels.add("ger");
            oLabels.add("ita");
            oLabels.add("jpn");
            oLabels.add("spa");
            oLabels.add("tur");
        } else {
            oLabels.add(msLang.toLowerCase());
            oLabels.add("oth");
        }
        
        return oLabels;
    }
    
    private ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        
        String sPath2Truth = sDataPath;
        while (sPath2Truth.endsWith("/")) {
            sPath2Truth = sPath2Truth.substring(0, sPath2Truth.length()-1);
        }
        
        if (!msLang.isEmpty()) {
            sPath2Truth += "." + msLang.toLowerCase();
        } 
        
        sPath2Truth += ".txt";
        
        FileReader fr = new FileReader(sPath2Truth);
        BufferedReader bf = new BufferedReader(fr);
        String sCadena = "";
        while ((sCadena = bf.readLine())!=null) {
            sCadena = sCadena.toLowerCase();
            String []info = sCadena.split(",");
//            if (info.length>=4) {
//                String sLabel = info[2]; sLabel = GetClass(sLabel);
//                String sFile = info[0].replace(".txt", "");
//                if (sLabel.equalsIgnoreCase(label)) {
//                    oTruth.add(sFile);
//                }
//            } else if (info.length==2) {
                String sLabel = info[1]; // sLabel = GetClass(sLabel);
                String sFile = info[0].replace(".txt", "");
                if (sLabel.equalsIgnoreCase(label)) {
                    oTruth.add(sFile);
                }
//            }
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
    
//    private static String GetClass(String sFile) {
//        String sClass = "";
//        
//        if (sFile.startsWith("kor")) {
//            sClass = "korean";
//        } else if (sFile.startsWith("spa")) {
//            sClass = "spanish";
//        } else if (sFile.startsWith("tel")) {
//            sClass = "telegu";
//        } else if (sFile.startsWith("fre")) {
//            sClass = "french";
//        } else if (sFile.startsWith("ger")) {
//            sClass = "german";
//        } else if (sFile.startsWith("ara")) {
//            sClass = "arabic";
//        } else if (sFile.startsWith("hin")) {
//            sClass = "hindi";
//        } else if (sFile.startsWith("chi")) {
//            sClass = "chinese";
//        } else if (sFile.startsWith("ita")) {
//            sClass = "italian";
//        } else if (sFile.startsWith("jpn")) {
//            sClass = "japanese";
//        } else if (sFile.startsWith("tur")) {
//            sClass = "turkish";
//        } 
//        
//        return sClass;
//    }
}
