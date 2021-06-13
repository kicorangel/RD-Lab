/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.nGrams.Test;

import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.nGrams.Datasets.PAN20_fakers;
import com.kicorangel.repr.nGrams.Datasets.PAN21_haters;
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
        PAN21_Test();
    }
    
    private static void PAN21_Test() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int t:new Integer[] {100,500,1000}) {
            for (NGRAMTYPE ntype:NGRAMTYPE.values()) {
                for (int n=1;n<=5;n++) {
                    for (String lang:new String[]{"es","en"}) {
                        System.out.println("-->Processing " + ntype.toString() + ".n-" + n + ".t-" + t);
                        String sTraining = "/mnt/data/PAN/datasets/pan21/4. Packaging/pan21-author-profiling-training-2021-03-14/" + lang;
                        String sTrainingArff = "/mnt/data/PAN/nGrams/pan21/arff/train." + lang + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                        String sTest = "/mnt/data/PAN/datasets/pan21/4. Packaging/pan21-author-profiling-test-2021-04-12/" + lang;
                        String sTestArff = "/mnt/data/PAN/nGrams/pan21/arff/test." + lang + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                        String sTmp = "/mnt/data/PAN/nGrams/pan21/tmp/tmp." + lang + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".txt";

                        new PAN21_haters(n, t, ntype, sTraining, sTmp, sTrainingArff, sTest, sTestArff, 
                                PAN21_haters.GetLabels(), SET.BOTH, 
                                new PreprocessingOptions(true, false, false, false, 0, new String[0])).Run();
                    }
                }
            }
        }
    }
}
