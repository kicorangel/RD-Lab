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
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author frangel
 */
public class T_PAN_AP_20 {
    public static void Test() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        PAN20_Test();
    }
    
    private static void PAN20_Test() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
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