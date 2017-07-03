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
public class PAN_AP_14_age_3groups implements iLoadDocsxClass  {
    private int mMinFreq;
    private int mMinSize;
    private String msCorpusPath;
    private String msLDRPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    
    public PAN_AP_14_age_3groups(int minFreq, int minSize, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff) {
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
        
        oLabels.add("18-34");
        oLabels.add("35-49");
        oLabels.add("50-xx");
        
        return oLabels;
    }
    
    private ArrayList<String> LoadTruth(String sDataPath, String label) throws FileNotFoundException, IOException {
        ArrayList<String> oTruth = new ArrayList<String>();
        
        if (new File(sDataPath + ".txt").exists()) {   // TEST SET
            FileReader fr = new FileReader(sDataPath + ".txt");
            BufferedReader bf = new BufferedReader(fr);
            String sCadena = "";

            while ((sCadena = bf.readLine())!=null) {
                String []data = sCadena.split(":::");
                String sAge = data[2];
//                sAge = Get3GAge(sAge);
                if (data.length==3 && sAge.equalsIgnoreCase(label)) {
                    oTruth.add(data[0]);
                }
            }
        } else {    // TRAINING SET
            File directory = new File(sDataPath);
            String[] files = directory.list();
            for (int iFile = 0; iFile < files.length; iFile++) {
                try {
                    String sFileName = files[iFile];
                    File fXmlFile = new File(sDataPath + "/" + sFileName);
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(fXmlFile);
                    String sAge = doc.getDocumentElement().getAttribute("age_group");
//                    sAge = Get3GAge(sAge);
                    if (sAge.equalsIgnoreCase(label)) {
                        String[] fileInfo = sFileName.split("_");
                        String sAuthor = fileInfo[0];
                        oTruth.add(sAuthor);
                    }
                } catch (Exception ex) {

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
                
                try { // FRP HARDCODED the language
                    File fXmlFile = new File(sData + "/" + oTruth.get(iFile) + "_es_" + label.toLowerCase() + "_male.xml");
                    
                    if (!fXmlFile.exists()) {
                        fXmlFile = new File(sData + "/" + oTruth.get(iFile) + "_es_" + label.toLowerCase() + "_female.xml");
                    }
                    
                    if (!fXmlFile.exists()) {
                        fXmlFile = new File(sData + "/" + oTruth.get(iFile) + "_es_xx_xx.xml");
                    }
                    
                    if (!fXmlFile.exists()) {
                        fXmlFile = new File(sData + "/" + oTruth.get(iFile) + "_en_" + label.toLowerCase() + "_male.xml");
                    }
                    
                    if (!fXmlFile.exists()) {
                        fXmlFile = new File(sData + "/" + oTruth.get(iFile) + "_en_" + label.toLowerCase() + "_female.xml");
                    }
                    
                    if (!fXmlFile.exists()) {
                        fXmlFile = new File(sData + "/" + oTruth.get(iFile) + "_en_xx_xx.xml");
                    }
                    
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(fXmlFile);
                    NodeList documents = doc.getDocumentElement().getElementsByTagName("document");
                    for (int i = 0; i < documents.getLength(); i++) {
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
                            System.out.println(ex.toString());
                        }
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
    
    private static String Get3GAge(String age) {
        if (age.equals("18-24") || age.equals("25-34")) {
            age = "18-34";
        } else if (age.equals("50-64") || age.equals("65-xx")) {
            age = "50-xx";
        }
        
        return age;
    }
}
