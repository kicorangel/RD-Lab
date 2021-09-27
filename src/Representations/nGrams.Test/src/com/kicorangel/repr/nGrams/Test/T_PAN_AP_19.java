/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.nGrams.Test;

import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.nGrams.Datasets.PAN19_bots;
import com.kicorangel.repr.nGrams.Datasets.PAN20_fakers;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author frangel
 */
public class T_PAN_AP_19 {
    public static void Test() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        PAN19_Atribus();
    }
    
    private static void PAN19_Atribus() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
        
        /* EN CHAR */
        String lang = "en";
        int t = 500;
        int n = 5;
        NGRAMTYPE ntype = NGRAMTYPE.CHAR;
        String sCorpusPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-training-2019-02-18/" + lang + "/";
        String sTmpPath = "/mnt/data/PAN/ngrams/pan19/tmp/bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".txt";
        String sTestPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-test-2019-04-29/" + lang + "/";
        String sTrainingArff = "/mnt/data/PAN/ngrams/pan19/arff/training.bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".arff";
        String sTestArff = "/mnt/data/PAN/ngrams/pan19/arff/test.bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".arff";
        
        
//        new PAN19_bots(n, t, ntype, sCorpusPath, sTmpPath, sTrainingArff, sTestPath, sTestArff, PAN19_bots.GetLabels(), SET.BOTH, new PreprocessingOptions(true, false, true, false, 1, new String[0]))
//                .Run();
        
        /* EN WORD */
//        lang = "en";
        t = 200;
        n = 1;
        ntype = NGRAMTYPE.WORD;
        sCorpusPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-training-2019-02-18/" + lang + "/";
        sTmpPath = "/mnt/data/PAN/ngrams/pan19/tmp/bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".txt";
        sTestPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-test-2019-04-29/" + lang + "/";
        sTrainingArff = "/mnt/data/PAN/ngrams/pan19/arff/training.bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".arff";
        sTestArff = "/mnt/data/PAN/ngrams/pan19/arff/test.bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".arff";
        
        
//        new PAN19_bots(n, t, ntype, sCorpusPath, sTmpPath, sTrainingArff, sTestPath, sTestArff, PAN19_bots.GetLabels(), SET.BOTH, new PreprocessingOptions(true, false, true, false, 1, new String[0]))
//                .Run();
        
        /* ES CHAR */
        lang = "es";
        t = 2000;
        n = 5;
        ntype = NGRAMTYPE.CHAR;
        sCorpusPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-training-2019-02-18/" + lang + "/";
        sTmpPath = "/mnt/data/PAN/ngrams/pan19/tmp/bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".txt";
        sTestPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-test-2019-04-29/" + lang + "/";
        sTrainingArff = "/mnt/data/PAN/ngrams/pan19/arff/training.bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".arff";
        sTestArff = "/mnt/data/PAN/ngrams/pan19/arff/test.bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".arff";
        
        
//        new PAN19_bots(n, t, ntype, sCorpusPath, sTmpPath, sTrainingArff, sTestPath, sTestArff, PAN19_bots.GetLabels(), SET.BOTH, new PreprocessingOptions(true, false, true, false, 1, new String[0]))
//                .Run();
        
        /* EN WORD */
        lang = "es";
        t = 100;
        n = 1;
        ntype = NGRAMTYPE.WORD;
        sCorpusPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-training-2019-02-18/" + lang + "/";
        sTmpPath = "/mnt/data/PAN/ngrams/pan19/tmp/bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".txt";
        sTestPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-test-2019-04-29/" + lang + "/";
        sTrainingArff = "/mnt/data/PAN/ngrams/pan19/arff/training.bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".arff";
        sTestArff = "/mnt/data/PAN/ngrams/pan19/arff/test.bots." + lang + "." + ntype.toString() + ".n" + n + ".t" + t + ".arff";
        
        
        new PAN19_bots(n, t, ntype, sCorpusPath, sTmpPath, sTrainingArff, sTestPath, sTestArff, PAN19_bots.GetLabels(), SET.TEST, new PreprocessingOptions(true, false, true, false, 1, new String[0]))
                .Run();
        
    }
    
    private static void PAN19_Test() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int t:new Integer[] {1000}) {
            for (NGRAMTYPE ntype:NGRAMTYPE.values()) {
                for (int n=1;n<=5;n++) {
                    for (String lang:new String[]{"es","en"}) {
                        System.out.println("-->Processing " + ntype.toString() + ".n-" + n + ".t-" + t);
                        String sTraining = "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-training-2020-02-23\\" + lang + "\\";
                        String sTrainingArff = "C:\\mnt\\data\\PAN\\nGrams\\pan20\\arff/train." + lang + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                        String sTest = "C:\\mnt\\data\\PAN\\datasets\\pan20\\4.dataset\\pan20-author-profiling-test-2020-02-23\\" + lang + "\\";
                        String sTestArff = "C:\\mnt\\data\\PAN\\nGrams\\pan20\\arff/test." + lang + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                        String sTmp = "C:\\mnt\\data\\PAN\\nGrams\\pan20\\tmp\\tmp." + lang + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".txt";

                        new PAN20_fakers(n, t, ntype, sTraining, sTmp, sTrainingArff, sTest, sTestArff, 
                                PAN20_fakers.GetLabels(), SET.BOTH, 
                                new PreprocessingOptions(true, false, false, false, 0, new String[0])).Run();
                    }
                }
            }
        }
    }
}
