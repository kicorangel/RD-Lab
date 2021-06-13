/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kicorangel.repr.ldse.Predictors;

import com.kicorangel.repr.ldse.GenerateVectorSpaceModel;
import com.kicorangel.repr.ldse.Datasets.PAN_AP_19_bots;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import com.kicorangel.repr.common.Prediction;
import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 *
 * @author @kicorangel
 */
public class BotsPredictor {
    private ArrayList<String> mLabels;
    private Classifier mClassifier;
    private GenerateVectorSpaceModel mVSM;
    
    public BotsPredictor(String ldsePath, String modelPath, int minFreq, int minSize, int version) throws IOException, Exception {
        PAN_AP_19_bots Profiler = new PAN_AP_19_bots(minFreq, minSize, "", ldsePath, "", "", "", version);
        mLabels = Profiler.GetLabels();
        mClassifier = ClassifierMngr.LoadClassifier(modelPath);
        mVSM = new GenerateVectorSpaceModel(mLabels, ldsePath, 1, 1);
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
            sPredictionGroup = "bot"; 
        } else if (prediction==1) {    
            sPredictionGroup = "human";
        } 
        
        return sPredictionGroup;
    }
}
