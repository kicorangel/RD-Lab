/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kicorangel.repr.nGrams.Predictors;

import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.nGrams.GenerateVectorSpaceModel;
import com.kicorangel.repr.nGrams.Datasets.PAN19_bots;
import java.io.IOException;
import java.util.ArrayList;
import com.kicorangel.repr.common.Prediction;
import com.kicorangel.repr.nGrams.Datasets.PAN19_bots_metadata;
import static com.kicorangel.repr.nGrams.Predictors.ClassifierMngr.LoadClassifier;
import com.kicorangel.repr.nGrams.Predictors.Eval.Info;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.Hashtable;
import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 *
 * @author @kicorangel
 */
public class BotsPredictor implements iPredictor{
    private ArrayList<String> mLabels;
    private Classifier mClassifier;
    private GenerateVectorSpaceModel mVSM;
        
    public BotsPredictor(String nGramsPath, String nGramsFile, String modelPath, NGRAMTYPE nGramsType, int n, int total, int length) throws IOException, Exception {
        InstantiatePredictor(nGramsPath, nGramsFile, modelPath, nGramsType, n, total, length);
    }
    
    public void InstantiatePredictor(String nGramsPath, String nGramsFile, String modelPath, NGRAMTYPE nGramsType, int n, int total, int length) throws IOException {
        PAN19_bots oPredictor = new PAN19_bots(n, total, nGramsType, "", nGramsPath, "", "", "", PAN19_bots.GetLabels(), SET.BOTH, 
                            new PreprocessingOptions(true, false, true, false, length, new String[0]));
        mLabels = oPredictor.GetLabels();
        mClassifier = LoadClassifier(modelPath);
        mVSM = new GenerateVectorSpaceModel(mLabels, nGramsFile, n, total, length, nGramsType, new PreprocessingOptions(true, false, true, false, length, new String[0]));
    }
    
    public Prediction Predict(String text) {
        return Predict(text, new ArrayList<String>());
    }
    
    public Prediction Predict(String text, ArrayList<String> metaData) {
        Prediction prediction = new Prediction();
        
        try {
            mVSM.Process(text);
            Instance inst = mVSM.GetInstance();
            prediction.PredictedClass = mClassifier.classifyInstance(inst); 
            prediction.ConfidenceValues = mClassifier.distributionForInstance(inst);
            prediction.Confidence = prediction.ConfidenceValues[(int)prediction.PredictedClass];
            prediction.sPredictedClass = GetPredictionClass(prediction.PredictedClass);
            prediction.sConfidence = String.format("%.0f", (double)Math.round(100*prediction.Confidence)) + "%";
        } catch (Exception ex) {
//            System.out.println(ex.toString());
            prediction.PredictedClass = -1;
            prediction.ConfidenceValues = new double[0];
            prediction.Confidence = 0;
            prediction.sPredictedClass = "";
            prediction.sConfidence = "";
        }
        
        return prediction;
    }
    
    private String GetPredictionClass(double prediction) {
        String sPredictionGroup = "";
        
        if (prediction==0) {
            sPredictionGroup = "bot"; 
        } else if (prediction==1) {    
            sPredictionGroup = "human";
        } 
        
        return sPredictionGroup;
    }
    
    public Hashtable<String, Info> LoadTruth(String truthPath, String lang) throws FileNotFoundException, IOException, URISyntaxException
    {
        Hashtable<String, Info>oTruth = new Hashtable<String, Info>();
        
        FileReader fr = null;
        
        if (!truthPath.endsWith("/")) {
            truthPath+="/";
        }
        
        truthPath += lang;
        fr = new FileReader(truthPath + "/truth.txt");
        
        BufferedReader bf = new BufferedReader(fr);
        String sCadena = "";

        while ((sCadena = bf.readLine())!=null)
        {
            // userid:::gender:::variety

            String []data = sCadena.split(":::");
            
            try
            {
                String sUser = data[0];
                Info oInfo = new Info();
                if (oTruth.containsKey(sUser)) {
                    oInfo = oTruth.get(sUser);
                }
                Info info = new Info();
                info.User = data[0];
                info.Lang = lang;
                info.Type = data[1]; // .replaceAll("M", "male").replace("F", "female");

                oTruth.put(sUser, info);
            }
            catch (Exception ex)
            {
                String s = ex.toString();
            }
        }
        
        bf.close();
        fr.close();
        
        return oTruth;
    }
    
    public Hashtable<String, String> LoadMeta(String metaPath) throws FileNotFoundException, IOException {
        return new Hashtable<String, String>();
    }
}
