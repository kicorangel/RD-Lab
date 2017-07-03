/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.autoritas.repr.ldr.datasets;

import com.autoritas.repr.ldr.GenerateArff;
import com.autoritas.repr.ldr.Prepare;
import com.autoritas.repr.ldr.Tools;
import com.autoritas.repr.ldr.iLoadDocsxClass;
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
    
    public Replab14(int minFreq, int minSize, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        msCorpusPath = corpusPath;
        msLDRPath = LDRPath;
        msTrainingArff = trainingArff;
        msTestPath = testPath;
        msTestArff = testArff;
    }
    
    
    public void Run() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        ArrayList<String> Labels = GetLabels();
        // Step 1: Prepare LDR weights and probabilities
        {
            Prepare oPrepare = new Prepare(mMinFreq, mMinSize, Labels, this, msCorpusPath, msLDRPath, true);
            oPrepare.Process();
        }   
        
        // Step 2: Generate training ARFF
        {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, Labels,  this, msCorpusPath, msLDRPath, "", msTrainingArff, true);
            oGenerate.Process();
        }
        
        // Step 3: Generate test ARFF
        {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, Labels, this, msCorpusPath, msLDRPath, msTestPath, msTestArff, true);
            oGenerate.Process();
        }
    }
    
    public ArrayList<String> GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
        oLabels.add("undecidable");
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
        
        return oLabels;
    }
    
    private ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        
        FileReader fr = new FileReader(sDataPath + "/goldstandard_author_categorization.dat");
        BufferedReader bf = new BufferedReader(fr);
        String sCadena = "";

        while ((sCadena = bf.readLine())!=null) {
            sCadena = sCadena.replaceAll("\"", "");
            String []data = sCadena.split("\t");
            if (data.length==3 && data[2].equalsIgnoreCase(label)) {
                oTruth.add(data[1]);
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
}
