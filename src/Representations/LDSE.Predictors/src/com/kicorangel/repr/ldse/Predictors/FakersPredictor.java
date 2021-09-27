/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kicorangel.repr.ldse.Predictors;

import com.kicorangel.repr.ldse.GenerateVectorSpaceModel;
import java.io.IOException;
import java.util.ArrayList;
import com.kicorangel.repr.common.Prediction;
import com.kicorangel.repr.ldse.Datasets.PAN_AP_20;
import com.kicorangel.repr.ldse.Predictors.Eval.Info;
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
public class FakersPredictor implements iPredictor {
    private ArrayList<String> mLabels;
    private Classifier mClassifier;
    private GenerateVectorSpaceModel mVSM;
    
    public FakersPredictor(String ldsePath, String modelPath, int minFreq, int minSize, int version) throws IOException, Exception {
        InstantiatePredictor(ldsePath, modelPath, minFreq, minSize, version);
    }
    
    public void InstantiatePredictor(String ldsePath, String modelPath, int minFreq, int minSize, int version) throws IOException {
        PAN_AP_20 Profiler = new PAN_AP_20(minFreq, minSize, "", ldsePath, "", "", "", version);
        mLabels = Profiler.GetLabels();
        mClassifier = ClassifierMngr.LoadClassifier(modelPath);
        mVSM = new GenerateVectorSpaceModel(mLabels, ldsePath, minSize, version);
    }
        
    public Prediction Predict(String text) {
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
            sPredictionGroup = "faker"; 
//            sPredictionGroup = "0";
        } else if (prediction==1) {    
            sPredictionGroup = "normal";
//            sPredictionGroup = "1";
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
                info.Type = (data[1].equals("0")?"normal":"faker"); 
//                info.Type = data[1];

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
}
