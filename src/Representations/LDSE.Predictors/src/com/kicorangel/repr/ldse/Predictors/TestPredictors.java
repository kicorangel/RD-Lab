/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.ldse.Predictors;

import com.kicorangel.repr.common.Prediction;
import com.kicorangel.repr.ldse.Predictors.Eval.Eval;
import static com.kicorangel.repr.ldse.Predictors.Tools.GetTweetsFromFile;

/**
 *
 * @author @kicorangel
 */
public class TestPredictors {

    /**ç4
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        Prediction oPrediction = new BotsPredictor("C:\\mnt\\data\\PAN\\ldr\\pan19\\data\\bots.es.LDSE.mf10.ms1.v1", 
                "C:\\mnt\\data\\PAN\\ldr\\pan19\\models\\bots.es.mf10.ms1.v1.NaiveBayes.model",
                10, 1, 1)
                .Predict(GetTweetsFromFile("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\1a4a00c01aaed1eebb72e3a3d9850bf2.xml")); // human
        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");
        oPrediction = new BotsPredictor("C:\\mnt\\data\\PAN\\ldr\\pan19\\data\\bots.es.LDSE.mf10.ms1.v1", 
                "C:\\mnt\\data\\PAN\\ldr\\pan19\\models\\bots.es.mf10.ms1.v1.NaiveBayes.model",
                10, 1, 1)
                .Predict(GetTweetsFromFile("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\3ad4a95c4fa92d34c24f0a1491c1d62f.xml")); // bot
        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");

//        Eval.SimpleTest(new BotsPredictor("C:\\mnt\\data\\PAN\\ldr\\pan19\\data\\bots.en.LDSE.mf10.ms1.v2", 
//                "C:\\mnt\\data\\PAN\\ldr\\pan19\\models\\bots.en.mf10.ms1.v2.NaiveBayes.model",
//                10, 1, 2), "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29", "en"); 

//        Eval.SimpleTest(new BotsPredictor("C:\\mnt\\data\\PAN\\ldr\\pan19\\data\\bots.es.LDSE.mf10.ms1.v1", 
//                "C:\\mnt\\data\\PAN\\ldr\\pan19\\models\\bots.es.mf10.ms1.v1.NaiveBayes.model",
//                10, 1, 1), "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29", "es");

//        Eval.ComplexTest("C:\\mnt\\data\\PAN\\ldr\\pan19\\data\\bots.es.LDSE.mf10.ms1.v1", 
//                "C:\\mnt\\data\\PAN\\ldr\\pan19\\models\\bots.es.mf10.ms1.v1.NaiveBayes.model",
//                10, 1, 1, "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29", "es"); 
        
//        Eval.ComplexTest("C:\\mnt\\data\\PAN\\ldr\\pan19\\data\\bots.en.LDSE.mf10.ms1.v2", 
//                "C:\\mnt\\data\\PAN\\ldr\\pan19\\models\\bots.en.mf10.ms1.v2.NaiveBayes.model",
//                10, 1, 2, "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29", "en"); 



//        Eval.SimpleTest(new FakersPredictor("C:\\mnt\\data\\PAN\\ldr\\pan20\\data\\fakers.en.LDSE.mf3.ms3.v1", 
//                "C:\\mnt\\data\\PAN\\ldr\\pan20\\models\\fakers.en.mf3.ms3.v1.HoeffdingTree.model",
//                3, 3, 1), "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23", "en");
        
//        Eval.SimpleTest(new FakersPredictor("C:\\mnt\\data\\PAN\\ldr\\pan20\\data\\fakers.es.LDSE.mf5.ms3.v1", 
//                "C:\\mnt\\data\\PAN\\ldr\\pan20\\models\\fakers.es.mf5.ms3.v1.LogisticRegression.model",
//                5, 3, 1), "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23", "es");

//        Eval.ComplexTest(new FakersPredictor("C:\\mnt\\data\\PAN\\ldr\\pan20\\data\\fakers.en.LDSE.mf3.ms3.v1", 
//                "C:\\mnt\\data\\PAN\\ldr\\pan20\\models\\fakers.en.mf3.ms3.v1.HoeffdingTree.model",
//                3, 3, 1), "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23", "en");
        
        Eval.ComplexTest(new FakersPredictor("C:\\mnt\\data\\PAN\\ldr\\pan20\\data\\fakers.es.LDSE.mf5.ms3.v1", 
                "C:\\mnt\\data\\PAN\\ldr\\pan20\\models\\fakers.es.mf5.ms3.v1.LogisticRegression.model",
                5, 3, 1), "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23", "es");
    }
    
    
    
}
