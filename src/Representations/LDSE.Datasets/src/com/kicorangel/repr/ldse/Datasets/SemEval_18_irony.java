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
public class SemEval_18_irony implements iLoadDocsxClass  {
    private int mMinFreq;
    private int mMinSize;
    private String msCorpusPath;
    private String msLDRPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    private int mLDRVersion;
    
    public SemEval_18_irony(int minFreq, int minSize, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff, int LDRVersion) {
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
        {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, Labels, this, msCorpusPath, msLDRPath, msTestPath, msTestArff, mLDRVersion, true);
            oGenerate.Process();
        }
    }
    
    
    public ArrayList<String> GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
        oLabels.add("0");
        oLabels.add("1");
        
        return oLabels;
    }
    
    public ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        
        FileReader fr = null;
        
        try {
            fr = new FileReader(sDataPath);
        
            BufferedReader bf = new BufferedReader(fr);
            String sCadena = "";

            while ((sCadena = bf.readLine())!=null) {
                String []data = sCadena.split("\t");
                if (data.length>=3 && data[1].equalsIgnoreCase(label)) {
                    oTruth.add(data[0]);
                }
            }
        } catch (Exception ex) {
            
        } finally {
            if (fr!=null) { try { fr.close(); } catch (Exception ex) {} }
        }
        
        return oTruth;
    }
    
    public Hashtable<String, Hashtable<String, Integer>> LoadDocsxClass(String sData, String label, int minSize, boolean verbose) {
        Hashtable<String, Hashtable<String, Integer>> oDocs = new Hashtable<String, Hashtable<String, Integer>>();

        
        
        FileReader fr = null;
        
        try {
            fr = new FileReader(sData);
        
            BufferedReader bf = new BufferedReader(fr);
            String sCadena = "";

            while ((sCadena = bf.readLine())!=null) {
                String []data = sCadena.split("\t");
                if (data.length>=3 && data[1].equalsIgnoreCase(label)) {
                    String id = data[0];
                    String sContent = data[2];
                    sContent = Tools.GetText(sContent);
                    sContent = Tools.Prepare(sContent);
                    
                    String[] tokens = sContent.split(" ");
                    for (String t : tokens) {
                        String sLemma = t.toLowerCase().trim();

                        if (sLemma.length() < minSize) {
                            continue;
                        }
                        Hashtable<String, Integer> oFreq = new Hashtable<String, Integer>();
                        if (oDocs.containsKey(id)) {
                            oFreq = oDocs.get(id);
                        }
                        int iFreq = 0;
                        if (oFreq.containsKey(sLemma)) {
                            iFreq = oFreq.get(sLemma);
                        }
                        oFreq.put(sLemma, ++iFreq);
                        oDocs.put(id, oFreq);
                    }
                }
            }
        } catch (Exception ex) {
            
        } finally {
            if (fr!=null) { try { fr.close(); } catch (Exception ex) {} }
        }
        
        return oDocs;
    }
}
