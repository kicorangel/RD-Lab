/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kicorangel.repr.ldse.Datasets;

import com.kicorangel.repr.ldse.GenerateArff;
import com.kicorangel.repr.ldse.Prepare;
import com.kicorangel.repr.base.Tools;
import com.kicorangel.repr.base.iLoadDocsxClass;
import com.kicorangel.repr.enumerations.SET;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
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
public class NADI2021_Subtask1x implements iLoadDocsxClass  {
    private int mMinFreq;
    private int mMinSize;
    private String msCorpusPath;
    private String msLDRPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    private int mLDRVersion;
    
    // These are specific for this dataset
    private Hashtable<String, String> mCorpus;
    
    
    public NADI2021_Subtask1x(int minFreq, int minSize,   String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff, int LDRVersion) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        msCorpusPath = corpusPath;
        msLDRPath = LDRPath;
        msTrainingArff = trainingArff;
        msTestPath = testPath;
        msTestArff = testArff;
        mLDRVersion = LDRVersion;
    }
    
    
    public void Run() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        ArrayList<String> Labels = GetLabels();
        // Step 1: Prepare LDR weights and probabilities
        mCorpus = LoadTweets(msCorpusPath);    // This is specific for this dataset
        {
            Prepare oPrepare = new Prepare(mMinFreq, mMinSize, Labels, this, msCorpusPath, msLDRPath, true);
            oPrepare.Process();
        }   
        
        // Step 2: Generate training ARFF
        {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, Labels,  this, msCorpusPath, msLDRPath, "", msTrainingArff, mLDRVersion, true);
            oGenerate.Process();
        }
        
        // Step 3: Generate test ARFF
        mCorpus = LoadTweets(msTestPath);   // This is specific for this dataset
        {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, Labels, this, msCorpusPath, msLDRPath, msTestPath, msTestArff, mLDRVersion, true);
            oGenerate.Process();
        }
    }
    
    
    public ArrayList<String> GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
        oLabels.add("Libya".toLowerCase());
        oLabels.add("Iraq".toLowerCase());
        oLabels.add("Saudi_Arabia".toLowerCase());
        oLabels.add("Syria".toLowerCase());                
        oLabels.add("Djibouti".toLowerCase());
        oLabels.add("Oman".toLowerCase());
        oLabels.add("Egypt".toLowerCase());                
        oLabels.add("Tunisia".toLowerCase());
        oLabels.add("United_Arab_Emirates".toLowerCase());
        oLabels.add("Morocco".toLowerCase());
        oLabels.add("Algeria".toLowerCase());              
        oLabels.add("Somalia".toLowerCase());
        oLabels.add("Palestine".toLowerCase());            
        oLabels.add("Lebanon".toLowerCase());              
        oLabels.add("Jordan".toLowerCase());
        oLabels.add("Bahrain".toLowerCase());
        oLabels.add("Kuwait".toLowerCase());               
        oLabels.add("Sudan".toLowerCase());
        oLabels.add("Mauritania".toLowerCase());           
        oLabels.add("Qatar".toLowerCase());                
        oLabels.add("Yemen".toLowerCase());
       
        return oLabels;
    }
    
    public ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        
        FileReader fr = new FileReader(sDataPath);
        BufferedReader bf = new BufferedReader(fr);
        String sLine = "";

        while ((sLine = bf.readLine())!=null) {
            String []data = sLine.split("\t");
            
            try {
                if (data[2].equalsIgnoreCase(label)) {
                    oTruth.add(data[0]);
                }
            } catch (Exception ex) {
//                System.out.println(sLine);
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
                
                String sContent = GetDocumentText(sAuthor);
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
        } catch (Exception ex) {

        }
        
        return oDocs;
    }
    
    
    
    public String GetDocumentText(String id) {
        String sLine = mCorpus.get(id);
        String []data = sLine.split("\t");
        String sText = data[1];
        return sText;
    }
        
    private Hashtable<String, String> LoadTweets(String sDataPath) throws FileNotFoundException, IOException {
        Hashtable<String, String> oTweets = new Hashtable<String, String>();
        
        FileReader fr = new FileReader(sDataPath);
        BufferedReader bf = new BufferedReader(fr);
        String sLine = "";
        while ((sLine = bf.readLine())!=null) {
            String []data = sLine.split("\t");
            String sId = data[0];
            oTweets.put(sId, sLine);
        }
        
        return oTweets;
    }
}
