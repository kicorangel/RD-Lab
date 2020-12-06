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
public class CMUQ100_Arap_variety implements iLoadDocsxClass  {
    private int mMinFreq;
    private int mMinSize;
    private String msCorpusPath;
    private String msLDRPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    private int mLDRVersion;
    private int mNTweets;
    
    
    public CMUQ100_Arap_variety(int minFreq, int minSize, int nTweets, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff, int LDRVersion) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        msCorpusPath = corpusPath;
        msLDRPath = LDRPath;
        msTrainingArff = trainingArff;
        msTestPath = testPath;
        msTestArff = testArff;
        mLDRVersion = LDRVersion;
        mNTweets = nTweets;
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
        
        oLabels.add("palestine jordan");
        oLabels.add("saudia arabia");
        oLabels.add("lebanon syria");
        oLabels.add("tunisia");
        oLabels.add("morroco");
        oLabels.add("algeria");
        oLabels.add("kuwait");
        oLabels.add("yemen");
        oLabels.add("sudan");
        oLabels.add("qatar");
        oLabels.add("libya");
        oLabels.add("egypt");
        oLabels.add("oman");
        oLabels.add("iraq");
        oLabels.add("uae");
        
        
        return oLabels;
    }
    
    public ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        
        FileReader fr = new FileReader(sDataPath + "/truth.txt");
        BufferedReader bf = new BufferedReader(fr);
        String sCadena = "";

        while ((sCadena = bf.readLine())!=null) {
            String []data = sCadena.split(":::");
            if (data.length==5 && data[3].equalsIgnoreCase(label)) {
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
        
            for (int iFile=0;iFile<oTruth.size();iFile++) {
                String sAuthor = oTruth.get(iFile);
                if (verbose) {
                    System.out.println(label + ": " + (iFile + 1) + "/" + oTruth.size());
                }
                
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
                
                }
            }
        } catch (Exception ex) {

        }
        
        return oDocs;
    }
    
    
    
    public String GetDocumentText(String authorFile) {
        String sText = "";
        try {
            File fXmlFile = new File(authorFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            NodeList documents = doc.getDocumentElement().getElementsByTagName("document");
            for (int i = 0; i < documents.getLength(); i++) {
                try {
                    Element element = (Element) documents.item(i);
                    String sHtmlContent = element.getTextContent();
                    String sContent = Tools.GetText(sHtmlContent);
                    sText += Tools.Prepare(sContent) + " ";
                } catch (Exception ex) {

                }
            }
        } catch (Exception ex) {

        }
        
        return sText;
    }
    
    public static void Balance(String source, String destination, int n) throws FileNotFoundException, IOException {
        
        for (String gender:new String[]{"male","female"}) {
            FileWriter fw = new FileWriter(destination + "/truth.txt", true);
            FileReader fr = new FileReader(source + "/truth.txt");
            BufferedReader bf = new BufferedReader(fr);
            String sCadena = "";
            int iElem = 0;

            while ((sCadena = bf.readLine())!=null) {
                String []data = sCadena.split(":::");
                if (data.length==3 && data[1].equalsIgnoreCase(gender)) {
                    if (iElem<n) {
                        fw.write(sCadena + "\n");
                        FileUtils.copyFile(new File(source + "/" + data[0] + ".xml"), new File(destination + "/" + data[0] + ".xml"));
                    }
                    iElem++;
                }
            }
            
            bf.close();
            fr.close();
            fw.close();
        }
    }
}
