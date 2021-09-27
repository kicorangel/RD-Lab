/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.ldse.Test;

import com.kicorangel.repr.ldse.Datasets.PAN_AP_21;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author frangel
 */
public class T_PAN_AP_21 {
    public static void Test() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
        /* PAN-AP-21 all possibilities */
        for (String lang:new String[]{"en","es"}) {
            for (int freq:new Integer[]{1, 2, 3, 5, 10}) {
                for (int size:new Integer[]{1, 2, 3}) {
                    for (int v:new Integer[]{1,2}) {
                        PAN_AP_21_Demo(lang, freq, size, v);
                    }
                }
            }
        }
        

        /* PAN_AP_20 best systems */
//        PAN_AP_20_Demo("es", 5, 3, 1);
//        PAN_AP_20_Demo("en", 3, 3, 1);

        
        
        
    }
    
    private static void PAN_AP_21_Demo(String lang, int minFreq, int minSize, int LDRVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sCorpusPath = "/mnt/data/PAN/datasets/pan21/4. Packaging/pan21-author-profiling-training-2021-03-14/" + lang;
        String sLDRPath = "/mnt/data/PAN/ldr/pan21/data/" + lang + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVer;
        String sTrainingArff = "/mnt/data/PAN/ldr/pan21/arff/" + lang + ".training.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        String sTestPath = "/mnt/data/PAN/datasets/pan21/4. Packaging/pan21-author-profiling-test-2021-04-12/" + lang;
        String sTestArff = "/mnt/data/PAN/ldr/pan21/arff/" + lang + ".test.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        
         if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }
         
        new PAN_AP_21(minFreq, minSize, sCorpusPath, sLDRPath, sTrainingArff, sTestPath, sTestArff, LDRVer).Run();
    }
}