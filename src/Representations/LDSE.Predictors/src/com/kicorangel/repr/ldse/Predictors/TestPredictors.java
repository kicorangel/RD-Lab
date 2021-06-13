/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.ldse.Predictors;

import com.kicorangel.repr.common.Prediction;
import com.kicorangel.repr.ldse.Datasets.PAN_AP_19_bots;
import static com.kicorangel.repr.ldse.Predictors.Tools.GetDocumentText;

/**
 *
 * @author frangel
 */
public class TestPredictors {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        BotsPredictor predictor = new BotsPredictor("C:\\mnt\\data\\PAN\\ldr\\pan19\\data\\es.mf10.ms1.v1", 
            "C:\\mnt\\data\\PAN\\ldr\\pan19\\models\\bots.es.mf10.ms1.v1.NaiveBayes.model",
            10, 1, 1);
        
        Prediction oPrediction = predictor.Predict(GetDocumentText("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\1a4a00c01aaed1eebb72e3a3d9850bf2.xml")); // human
        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");
        oPrediction = predictor.Predict(GetDocumentText("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\3ad4a95c4fa92d34c24f0a1491c1d62f.xml")); // bot
        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");
    }
    
}
