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
public class ICLE implements iLoadDocsxClass  {
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
    
    public ICLE(int minFreq, int minSize, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff, ArrayList<String> labels, SET set, int LDRVersion) {
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
        
        oLabels.add("bulgarian");
        oLabels.add("chinese");
        oLabels.add("czech");
        oLabels.add("dutch");
        oLabels.add("finnish");
        oLabels.add("french");
        oLabels.add("german");
        oLabels.add("italian");
        oLabels.add("japanese");
        oLabels.add("norwegian");
        oLabels.add("polish");
        oLabels.add("russian");
        oLabels.add("spanish");
        oLabels.add("swedish");
        oLabels.add("tswana");
        oLabels.add("turkish");
        
        return oLabels;
    }
    
    private ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        String sFolder = sDataPath;
        File directory = new File(sFolder);
        String[] files = directory.list();
        for (int iFile = 0; iFile < files.length; iFile++) {
            String sFileName = new File(files[iFile]).getName().replace(".txt", "");
            String sClass = GetClass(sFileName);
            if (label.equalsIgnoreCase(sClass)) {
                oTruth.add(sFileName);
            }
        }
        
        return oTruth;
    }
    
    private static String GetClass(String sFile) {
        String sClass = "";
        
        if (sFile.startsWith("BGSU")) {
            sClass = "bulgarian";
        } else if (sFile.startsWith("CN")) {
            sClass = "chinese";
        } else if (sFile.startsWith("CZ")) {
            sClass = "czech";
        } else if (sFile.startsWith("D")) {
            sClass = "dutch";
        } else if (sFile.startsWith("FI")) {
            sClass = "finnish";
        } else if (sFile.startsWith("FR")) {
            sClass = "french";
        } else if (sFile.startsWith("GE")) {
            sClass = "german";
        } else if (sFile.startsWith("IT")) {
            sClass = "italian";
        } else if (sFile.startsWith("JP")) {
            sClass = "japanese";
        } else if (sFile.startsWith("NO")) {
            sClass = "norwegian";
        } else if (sFile.startsWith("PO")) {
            sClass = "polish";
        } else if (sFile.startsWith("RU")) {
            sClass = "russian";
        } else if (sFile.startsWith("SP")) {
            sClass = "spanish";
        } else if (sFile.startsWith("SW")) {
            sClass = "swedish";
        } else if (sFile.startsWith("TS")) {
            sClass = "tswana";
        } else if (sFile.startsWith("TR")) {
            sClass = "turkish";
        }         
        
        return sClass;
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
}
