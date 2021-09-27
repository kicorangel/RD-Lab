/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.ldse.Predictors.Eval;

import com.kicorangel.repr.base.Tools;
import com.kicorangel.repr.common.Prediction;
import com.kicorangel.repr.ldse.Predictors.BotsPredictor;
import static com.kicorangel.repr.ldse.Predictors.Tools.GetTweetsFromFile;
import com.kicorangel.repr.ldse.Predictors.iPredictor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
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
    private static int INI = 1;
    private static int NTWEETS = 100;
    
    public static void SimpleTest(iPredictor predictor, String TEST, String LANG) throws Exception{
        Hashtable<String, Info> oTruth = predictor.LoadTruth(TEST, LANG);
        System.out.print(SimpleTest(predictor, oTruth, 100, 0, TEST, LANG));
    }
    
    public static String SimpleTest(iPredictor predictor, Hashtable<String, Info> oTruth, int nTweets, int firstTweet, String TEST, String LANG) throws Exception {
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
                
                NodeList documents = doc.getDocumentElement().getElementsByTagName("document");
                StringBuilder sb = new StringBuilder();
                for (int i = firstTweet-1; i < firstTweet - 1 + nTweets; i++) {
                    try {
                        Element element = (Element) documents.item(i);
                        String sHtmlContent = element.getTextContent();
                        String sContent = Tools.GetText(sHtmlContent);
                        sContent = Tools.Prepare(sContent);
                        sb.append(sContent).append(" ");
                    } catch (Exception ex) {

                    }
                }
                
                String sClass = oTruth.get(sId).Type;
                Prediction oPrediction = predictor.Predict(sb.toString());
                if (oPrediction.sPredictedClass.equalsIgnoreCase(sClass)) {
                    accuracy++;
                }
            }
        }
        
        accuracy = accuracy / oTruth.size();    
        return String.format("%.4f", accuracy);
    }
    
    public static void ComplexTest(iPredictor predictor, String TEST, String LANG) throws Exception {
        Hashtable<String, Info> oTruth = predictor.LoadTruth(TEST, LANG);
        
        for (int iTweets=INI;iTweets<=NTWEETS;iTweets++) {
            System.out.print("LDSE_" + LANG + "_" + iTweets + " <- c(");
            for (int iPos=1;iPos<=NTWEETS-iTweets+1;iPos++) {
                if (iPos>1) {
                    System.out.print(",");
                }
                System.out.print(SimpleTest(predictor, oTruth, iTweets, iPos, TEST, LANG));
            }
            System.out.println(")");
        }
            
    }
    
//    public static void BasicTest(String LDSE, String MODEL, int MINFREQ, int MINSIZE, int VERSION, String LANG) throws Exception {
//        BotsPredictor predictor = new BotsPredictor("C:\\mnt\\data\\PAN\\ldr\\pan19\\data\\es.mf10.ms1.v1", 
//            "C:\\mnt\\data\\PAN\\ldr\\pan19\\models\\bots.es.mf10.ms1.v1.NaiveBayes.model",
//            10, 1, 1);
//        
//        Prediction oPrediction = predictor.Predict(GetTweetsFromFile("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\1a4a00c01aaed1eebb72e3a3d9850bf2.xml")); // human
//        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");
//        oPrediction = predictor.Predict(GetTweetsFromFile("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\3ad4a95c4fa92d34c24f0a1491c1d62f.xml")); // bot
//        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");
//    }
    
}
