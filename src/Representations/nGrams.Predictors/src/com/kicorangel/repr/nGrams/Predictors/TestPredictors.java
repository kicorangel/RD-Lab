/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.nGrams.Predictors;

import com.kicorangel.repr.common.Prediction;
import com.kicorangel.repr.enumerations.NGRAMTYPE;
import static com.kicorangel.repr.nGrams.Predictors.Tools.GetDocumentText;

/**
 *
 * @author @kicorangel
 */
public class TestPredictors {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        BotsPredictor predictor = new BotsPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp", 
            "C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp\\bots.es.WORD.n1.t100.txt",
                "C:\\mnt\\data\\PAN\\ngrams\\pan19\\models\\bots.es.WORD.n1.t100.RandomForest.model",
                NGRAMTYPE.WORD, 1, 100, 1);
        
        Prediction oPrediction = predictor.Predict(GetDocumentText("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\1a4a00c01aaed1eebb72e3a3d9850bf2.xml")); // human
        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");
        oPrediction = predictor.Predict(GetDocumentText("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\3ad4a95c4fa92d34c24f0a1491c1d62f.xml")); // bot
        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");
    }
    
}
