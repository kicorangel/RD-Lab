/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.nGrams.Predictors;

import com.kicorangel.repr.common.Prediction;
import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.nGrams.Predictors.Eval.Eval;
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

//        Prediction oPrediction = predictor.Predict(GetDocumentText("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\1a4a00c01aaed1eebb72e3a3d9850bf2.xml")); // human
//        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");
//        oPrediction = predictor.Predict(GetDocumentText("C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29\\es\\3ad4a95c4fa92d34c24f0a1491c1d62f.xml")); // bot
//        System.out.println(oPrediction.sPredictedClass + "(" + oPrediction.sConfidence + ")");

//        Eval.SimpleTest(
//                new BotsPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp", 
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp\\bots.es.WORD.n1.t100.txt", 
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\models\\bots.es.WORD.n1.t100.RandomForest.model",
//                        NGRAMTYPE.WORD, 1, 100, 0),  
//                "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29", 
//                "es");
//        Eval.SimpleTest(
//                new BotsPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp", 
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp\\bots.en.WORD.n1.t200.txt", 
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\models\\bots.en.WORD.n1.t200.RandomForest.model",
//                        NGRAMTYPE.WORD, 1, 200, 0),
//                "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29", 
//                "en");
//        Eval.SimpleTest(
//                new BotsPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp", 
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp\\bots.es.CHAR.n5.t2000.txt", 
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\models\\bots.es.CHAR.n5.t2000.RandomForest.model",
//                        NGRAMTYPE.CHAR, 5, 2000, 0), 
//                "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29", 
//                "es");
//        Eval.SimpleTest(
//                new BotsPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp", 
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp\\bots.en.CHAR.n5.t500.txt", 
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\models\\bots.en.CHAR.n5.t500.RandomForest.model", 
//                        NGRAMTYPE.CHAR, 5, 500, 0), 
//                "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29", 
//                "en");
        

        /* Bots Type */
//        Eval.SimpleTest(
//                new BotsTypePredictor("astroturf",
//                   "C:\\mnt\\data\\botometer\\tmp", 
//                        "C:\\mnt\\data\\botometer\\tmp\\bots.astroturf.es.CHAR.n-3.t-1000.txt", 
//                        "C:\\mnt\\data\\botometer\\models\\bots.astroturf.es.CHAR.n3.t1000.SVM.model", 
//                        NGRAMTYPE.CHAR, 3, 1000, 0), 
//                "C:\\mnt\\data\\botometer\\XML\\", 
//                "es");
        


        /* Test Fake News */
//        Eval.SimpleTest(
//                new FakersPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp\\fakers.en.CHAR.n-3.t-1000.txt",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\models\\fakers.en.CHAR.n3.t1000.RandomForest.model",
//                        NGRAMTYPE.CHAR, 3, 1000, 0), 
//                "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23",
//                "en");
//        
//        Eval.SimpleTest(
//                new FakersPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp\\fakers.en.WORD.n-2.t-1000.txt",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\models\\fakers.en.WORD.n2.t1000.RandomForest.model",
//                        NGRAMTYPE.WORD, 2, 1000, 0), 
//                "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23",
//                "en");
//        
//        Eval.SimpleTest(
//                new FakersPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp\\fakers.es.CHAR.n-5.t-1000.txt",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\models\\fakers.es.CHAR.n5.t1000.SGD.model",
//                        NGRAMTYPE.CHAR, 5, 1000, 0), 
//                "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23",
//                "es");
//        
//        Eval.SimpleTest(
//                new FakersPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp\\fakers.es.WORD.n-1.t-1000.txt",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\models\\fakers.es.WORD.n1.t1000.SGD.model",
//                        NGRAMTYPE.WORD, 1, 1000, 0), 
//                "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23",
//                "es");

//        Eval.ComplexTest("fakers", new FakersPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp\\fakers.en.CHAR.n-3.t-1000.txt",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\models\\fakers.en.CHAR.n3.t1000.RandomForest.model",
//                        NGRAMTYPE.CHAR, 3, 1000, 0), 
//                "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23",
//                "en");
//        
//        Eval.ComplexTest("fakers", 
//                new FakersPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp\\fakers.en.WORD.n-2.t-1000.txt",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\models\\fakers.en.WORD.n2.t1000.RandomForest.model",
//                        NGRAMTYPE.WORD, 2, 1000, 0), 
//                "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23",
//                "en");
//        
//        Eval.ComplexTest("fakers", 
//                new FakersPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp\\fakers.es.CHAR.n-5.t-1000.txt",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\models\\fakers.es.CHAR.n5.t1000.SGD.model",
//                        NGRAMTYPE.CHAR, 5, 1000, 0), 
//                "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23",
//                "es");
//        
//        Eval.ComplexTest("fakers", 
//                new FakersPredictor("C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\tmp\\fakers.es.WORD.n-1.t-1000.txt",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan20\\models\\fakers.es.WORD.n1.t1000.SGD.model",
//                        NGRAMTYPE.WORD, 1, 1000, 0), 
//                "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23",
//                "es");

//        Eval.SimpleTest(
//                new BotsPredictorWithMetadata("C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp",
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp\\bots.meta.en.WORD.n-1.t-100.txt", 
//                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\models\\bots.meta.en.WORD.n1.t100.RandomForest.model",
//                        NGRAMTYPE.WORD, 1, 100, 0),  
//                "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29", 
//                "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset_with_meta\\Bots - BOTS_TEST_EN_META.tsv",
//                "en");
        
        Eval.ComplexTest("bots",
                new BotsPredictorWithMetadata("C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp",
                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\tmp\\bots.meta.en.CHAR.n-4.t-100.txt", 
                        "C:\\mnt\\data\\PAN\\ngrams\\pan19\\models\\bots.meta.en.CHAR.n4.t100.RandomForest.model",
                        NGRAMTYPE.CHAR, 4, 100, 0),  
                "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset\\pan19-author-profiling-test-2019-04-29", 
                "C:\\mnt\\data\\PAN\\datasets\\pan19\\dataset_with_meta\\Bots - BOTS_TEST_EN_META.tsv",
                "en");
    }
    
}
