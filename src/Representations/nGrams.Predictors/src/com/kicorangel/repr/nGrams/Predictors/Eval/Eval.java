/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.nGrams.Predictors.Eval;

import com.kicorangel.repr.base.Tools;
import com.kicorangel.repr.common.Prediction;
import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.nGrams.Datasets.PAN19_bots;
import com.kicorangel.repr.nGrams.Predictors.BotsPredictor;
import com.kicorangel.repr.nGrams.Predictors.iPredictor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author frangel
 */
public class Eval {
//    private static String NGRAMS_PATH = "C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp";
//    private static String NGRAMS = "C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp\\bots.es.WORD.n1.t100.txt";
//    private static String MODEL = "C:\\mnt\\data\\PAN\\ngrams\\pan19\\models\\bots.es.WORD.n1.t100.RandomForest.model";
//    private static String TEST = "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29";
    
    private static int INI = 1;
    private static int NTWEETS = 100;
    
    public static void SimpleTest(iPredictor predictor, String TEST, String LANG) throws Exception{
        SimpleTest(predictor, TEST, LANG, "");
    }
    
    public static void SimpleTest(iPredictor predictor, String TEST, String META, String LANG) throws Exception{
        Hashtable<String, Info> oTruth = predictor.LoadTruth(TEST, LANG);
        Hashtable<String, String> oMetaValues = new Hashtable<String, String>();
        if (!META.isEmpty()) {
            oMetaValues = predictor.LoadMeta(META);
        }
        System.out.print(SimpleTest(predictor, oTruth, 100, 0, TEST, LANG, oMetaValues));
    }
    
    private static String SimpleTest(iPredictor predictor, Hashtable<String, Info> oTruth, int nTweets, int firstTweet, String TEST, String LANG, Hashtable<String, String> metaValues) throws Exception {
        File directory = new File(TEST + "/" + LANG);
        File []files = directory.listFiles();
        
        if (files==null) {
            return "";
        }
        
        double accuracy = 0;
        
        for (int iFile = 0; iFile < files.length; iFile++) {
            if (!files[iFile].getName().endsWith("xml")) {
                continue;
            }
            File fXmlFile = new File(TEST + "/" + LANG + "/" + files[iFile].getName());

            if (fXmlFile.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);

                String sId = files[iFile].getName().replace(".xml", "");
                
                if (sId.equalsIgnoreCase("a7aa2cafc69fcafb77107ec7ff1a3b72")) {
                    String stop = "stop";
                }
                
                NodeList documents = doc.getDocumentElement().getElementsByTagName("document");
                documents.getLength();
                StringBuilder sb = new StringBuilder();
                for (int i = firstTweet-1; i < firstTweet + nTweets - 1; i++) {
                    try {
                        Element element = (Element) documents.item(i);
                        String sHtmlContent = element.getTextContent();
                        String sContent = com.kicorangel.repr.base.Tools.GetText(sHtmlContent);
                        sContent = com.kicorangel.repr.base.Tools.Prepare(sContent);                        
                        sb.append(sContent).append(" ");
                    } catch (Exception ex) {

                    }
                }
                
                String sToPredict = sb.toString();
               
                String sClass = "";
                
                if (oTruth.containsKey(sId)) {
                    sClass = oTruth.get(sId).Type;
                    
                    ArrayList<String> oMetaValues = new ArrayList<String>();
                    Prediction oPrediction = new Prediction();
                            
                    if (metaValues.containsKey(sId)) {
                        String sData = (String)metaValues.get(sId);
                        String []oData = sData.split("\t");
                        
                        for (int iMD=3;iMD<oData.length;iMD++) {
                            oMetaValues.add(oData[iMD]);
                        }
                        oPrediction = predictor.Predict(sToPredict, oMetaValues);
                    }
                    
    //                System.out.println(sId + "->" + sClass);
                    
                    if (oPrediction.sPredictedClass.equalsIgnoreCase(sClass)) {
                        accuracy++;
                    }
                }
            }
        }
        
        accuracy = accuracy / oTruth.size();    
        return String.format("%.4f", accuracy);
    }
    
    public static void ComplexTest(String name, iPredictor predictor, String TEST, String META, String LANG) throws Exception {
        Hashtable<String, Info> oTruth = predictor.LoadTruth(TEST, LANG);
        Hashtable<String, String> oMetaValues = new Hashtable<String, String>();
        if (!META.isEmpty()) {
            oMetaValues = predictor.LoadMeta(META);
        }
        
        for (int iTweets=INI;iTweets<=NTWEETS;iTweets++) {
            System.out.print(name + "_" + LANG + "_" + iTweets + " <- c(");
            for (int iPos=1;iPos<=NTWEETS-iTweets+1;iPos++) {
                if (iPos>1) {
                    System.out.print(",");
                }
                System.out.print(SimpleTest(predictor, oTruth, iTweets, iPos, TEST, LANG, oMetaValues));
            }
            System.out.println(")");
        }
    }
}
