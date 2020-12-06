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
public class FakeRealNewsMergeQatar implements iLoadDocsxClass  {
    private int mMinFreq;
    private int mMinSize;
    private String msCorpusPath;
    private String msLDRPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    private int mLDRVersion;
//    private int mNTweets;
    
    // These are specific for this dataset
    private Hashtable<String, String> mCorpus;
    
    
    public FakeRealNewsMergeQatar(int minFreq, int minSize,   String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff, int LDRVersion) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        msCorpusPath = corpusPath;
        msLDRPath = LDRPath;
        msTrainingArff = trainingArff;
        msTestPath = testPath;
        msTestArff = testArff;
        mLDRVersion = LDRVersion;
//        mNTweets = nTweets;
    }

//    public Credibility(int minFreq, int minSize, String replace, String sLDRPath, String sTrainingArff, String replace0, String sTestArff, Object object, SET set, int LDRVersion) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
    
    public void Run() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        ArrayList<String> Labels = GetLabels();
        if (!msCorpusPath.isEmpty()) {
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
        }
        
        if (!msTestPath.isEmpty()) {
        // Step 3: Generate test ARFF
        mCorpus = LoadTweets(msTestPath);   // This is specific for this dataset
        {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, Labels, this, msCorpusPath, msLDRPath, msTestPath, msTestArff, mLDRVersion, true);
            oGenerate.Process();
        }
        }
    }
    
    
    public ArrayList<String> GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
        oLabels.add("truth");
        oLabels.add("lie");
        
        return oLabels;
    }
    
    public ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        
        FileReader fr = new FileReader(sDataPath);
        BufferedReader bf = new BufferedReader(fr);
        String sLine = "";

        while ((sLine = bf.readLine())!=null) {
            String []data = sLine.split(",");
            
            try {
                if (data[1].equalsIgnoreCase(label)) {
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
                
                
                /*
                try {
                    File fXmlFile = new File(sData + "/" + oTruth.get(iFile) + ".xml");
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(fXmlFile);
                    NodeList documents = doc.getDocumentElement().getElementsByTagName("document");
                    for (int i = 0; i < documents.getLength() && i<mNTweets; i++) {
                        try {
                            Element element = (Element) documents.item(i);
                            String sHtmlContent = element.getTextContent();
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
                        } catch (Exception ex) {
                            
                        }
                    }
                } catch (Exception ex) {
                
                }*/
            }
        } catch (Exception ex) {

        }
        
        return oDocs;
    }
    
    
    
    public String GetDocumentText(String id) {
        String sLine = mCorpus.get(id);
        String []data = sLine.split(",");
        String sText = "";
        for (int i=2;i<data.length;i++) {
            sText += data[i];
            if (i<data.length-1) {
                sText += ",";
            }
        }
        
        sText += Tools.Prepare(sText);       
        return sText;
    }
        
    private Hashtable<String, String> LoadTweets(String sDataPath) throws FileNotFoundException, IOException {
        Hashtable<String, String> oTweets = new Hashtable<String, String>();
        
        FileReader fr = new FileReader(sDataPath);
        BufferedReader bf = new BufferedReader(fr);
        String sLine = "";
        while ((sLine = bf.readLine())!=null) {
            String []data = sLine.split(",");
            String sId = data[0];
            oTweets.put(sId, sLine);
        }
        
        return oTweets;
    }
}
