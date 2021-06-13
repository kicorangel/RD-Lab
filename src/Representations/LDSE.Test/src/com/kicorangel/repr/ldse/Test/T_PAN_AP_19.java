/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.ldse.Test;

import com.kicorangel.repr.ldse.Datasets.PAN_AP_19_bots;
import com.kicorangel.repr.ldse.Datasets.PAN_AP_20;
import java.io.File;
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
        
        /* PAN-AP-20 all possibilities */
//        for (String lang:new String[]{"en","es"}) {
//            for (int freq:new Integer[]{1, 2, 3, 5, 10}) {
//                for (int size:new Integer[]{1, 2, 3}) {
//                    for (int v:new Integer[]{1,2}) {
//                        PAN_AP_20_Demo(lang, freq, size, v);
//                    }
//                }
//            }
//        }
        

        /* PAN_AP_19 best systems */
        PAN_AP_19_Demo("es", 10, 1, 1);
        PAN_AP_19_Demo("en", 10, 1, 2);

        
        
        
    }
    
    private static void PAN_AP_19_Demo(String lang, int minFreq, int minSize, int LDRVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sCorpusPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-training-2019-02-18/" + lang + "/";
        String sTestPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-test-2019-04-29/" + lang + "/";
        String sLDRPath = "/mnt/data/PAN/ldr/pan19/data/" + lang + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVer;
        String sTrainingArff = "/mnt/data/PAN/ldr/pan19/arff/" + lang + ".training.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        
        String sTestArff = "/mnt/data/PAN/ldr/pan19/arff/" + lang + ".test.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        
         if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }
         
        new PAN_AP_19_bots(minFreq, minSize, sCorpusPath, sLDRPath, sTrainingArff, sTestPath, sTestArff, LDRVer).Run();
    }
}
