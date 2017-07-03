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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class IberEval17_Stance implements iLoadDocsxClass {
     
    private int mMinFreq;
    private int mMinSize;
    private String msCorpusPath;
    private String msLDRPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    
    public IberEval17_Stance(int minFreq, int minSize, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff) {
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
        
        oLabels.add("neutral");
        oLabels.add("against");
        oLabels.add("favor");
        
        return oLabels;
    }
     
     public ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        
        FileReader fr = new FileReader(sDataPath.replace("tweets", "truth"));
        BufferedReader bf = new BufferedReader(fr);
        String sCadena = "";

        while ((sCadena = bf.readLine())!=null) {
            String []data = sCadena.split(":::");
            if (data.length==3 && data[1].equalsIgnoreCase(label)) {
                oTruth.add(data[0]);
            }
        }
    
        return oTruth;
    }
    
    public Hashtable<String, Hashtable<String, Integer>> LoadDocsxClass(String sData, String label, int minSize, boolean verbose) {
        Hashtable<String, Hashtable<String, Integer>> oDocs = new Hashtable<String, Hashtable<String, Integer>>();

        ArrayList<String> oTruth = new ArrayList<String>();
        
        try {
            oTruth = LoadTruth(sData, label);
            
            FileReader fr = new FileReader(sData);
            BufferedReader bf = new BufferedReader(fr);
            String sCadena = "";

            while ((sCadena = bf.readLine())!=null) {
                String []data = sCadena.split(":::");
                if (data.length==2 && oTruth.contains(data[0])) {
                    String sAuthor = data[0];
                    String sContent = data[1];
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
            }
        } catch (Exception ex) {

        }
        
        return oDocs;
    }
}
