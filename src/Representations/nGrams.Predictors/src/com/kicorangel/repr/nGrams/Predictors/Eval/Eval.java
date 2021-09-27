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
        Hashtable<String, Info> oTruth = predictor.LoadTruth(TEST, LANG);
        System.out.print(SimpleTest(predictor, oTruth, 100, 0, TEST, LANG));
    }
    
    private static String SimpleTest(iPredictor predictor, Hashtable<String, Info> oTruth, int nTweets, int firstTweet, String TEST, String LANG) throws Exception {
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
    //                System.out.println(sId + "->" + sClass);
                    Prediction oPrediction = predictor.Predict(sToPredict);
                    if (oPrediction.sPredictedClass.equalsIgnoreCase(sClass)) {
                        accuracy++;
                    }
                }
            }
        }
        
        accuracy = accuracy / oTruth.size();    
        return String.format("%.4f", accuracy);
    }
    
    public static void ComplexTest(String name, iPredictor predictor, String TEST, String LANG) throws Exception {
        
//        String lang = "es";
//        int t = 2000;
//        int n = 5;
//        NGRAMTYPE ntype = NGRAMTYPE.CHAR;
//        String sCorpusPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-training-2019-02-18/" + lang + "/";
//        
//        String sTrainingArff = "/mnt/data/PAN/ngrams/pan19/arff/training_kico.bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".arff";
//        
//        String sTmpPath = "C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp\\bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".txt";
//        String sTestPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-test-2019-04-29/" + lang + "/";
//        String sTestArff = "/mnt/data/PAN/ngrams/pan19/arff/test_kico.bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".arff";
        
        
//        new PAN19_bots(n, t, ntype, sCorpusPath, sTmpPath, sTrainingArff, sTestPath, sTestArff, PAN19_bots.GetLabels(), SET.TEST, new PreprocessingOptions(true, false, true, false, 1, new String[0]))
//                .Run();
        
        
//        BotsPredictor predictor = new BotsPredictor(NGRAMS_PATH, 
//            NGRAMS,
//                MODEL,
//                nGramType, N, total, length);
//        
        Hashtable<String, Info> oTruth = predictor.LoadTruth(TEST, LANG);
//        System.out.print(SimpleTest(predictor, oTruth, 100, 1, LANG));
        
        for (int iTweets=INI;iTweets<=NTWEETS;iTweets++) {
            System.out.print(name + "_" + LANG + "_" + iTweets + " <- c(");
            for (int iPos=1;iPos<=NTWEETS-iTweets+1;iPos++) {
                if (iPos>1) {
                    System.out.print(",");
                }
                System.out.print(SimpleTest(predictor, oTruth, iTweets, iPos, TEST, LANG));
            }
            System.out.println(")");
        }
            
    }
    
    
    
//    private static Hashtable<String, Info> LoadTruth(String truthPath, String lang) throws FileNotFoundException, IOException, URISyntaxException
//    {
//        Hashtable<String, Info>oTruth = new Hashtable<String, Info>();
//        
//        FileReader fr = null;
//        
//        if (!truthPath.endsWith("/")) {
//            truthPath+="/";
//        }
//        
//        truthPath += lang;
//        
////        if (truthPath.contains("training")) {
//            fr = new FileReader(truthPath + "/truth.txt");
////        } else {
////            fr = new FileReader(truthPath + ".txt");
////        }
//        
//        
//        BufferedReader bf = new BufferedReader(fr);
//        String sCadena = "";
//
//        while ((sCadena = bf.readLine())!=null)
//        {
//            // userid:::gender:::variety
//
//            String []data = sCadena.split(":::");
//            
//            try
//            {
//                String sUser = data[0];
//                Info oInfo = new Info();
//                if (oTruth.containsKey(sUser)) {
//                    oInfo = oTruth.get(sUser);
//                }
//                Info info = new Info();
//                info.User = data[0];
//                info.Lang = lang;
//                info.Type = data[1]; // .replaceAll("M", "male").replace("F", "female");
//
//                oTruth.put(sUser, info);
//            }
//            catch (Exception ex)
//            {
//                String s = ex.toString();
//            }
//        }
//        
//        bf.close();
//        fr.close();
//        
//        return oTruth;
//    }
    
    
    public static void BasicTest() throws Exception {
//        BotsPredictor predictor = new BotsPredictor("C:\\mnt\\data\\PAN\\ldr\\pan19\\data\\es.mf10.ms1.v1", 
//            "C:\\mnt\\data\\PAN\\ldr\\pan19\\models\\bots.es.mf10.ms1.v1.NaiveBayes.model",
//            10, 1, 1);
//        
//        Prediction oPrediction = predictor.Predict(GetTweetsFromFile("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\1a4a00c01aaed1eebb72e3a3d9850bf2.xml")); // human
//        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");
//        oPrediction = predictor.Predict(GetTweetsFromFile("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\3ad4a95c4fa92d34c24f0a1491c1d62f.xml")); // bot
//        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");
    }
}
