/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.nGrams.Test;

import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.nGrams.Datasets.NADI2021_Subtask1x;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author frangel
 */
public class T_NADI_2021 {
    public static void Test() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        NADI2021_Subtask1x_Test();
    }
    
    private static void NADI2021_Subtask1x_Test() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int t:new Integer[] {1000}) {
            for (NGRAMTYPE ntype:NGRAMTYPE.values()) {
                for (int n=1;n<=5;n++) {
                    for (int x:new int[]{1,2}) {
                        System.out.println("-->Processing " + ntype.toString() + ".n-" + n + ".t-" + t);
                        String sBasePath = "C:\\mnt\\data\\NADI2021\\NADI2021_DEV.1.0\\";
                        String sTraining = sBasePath + ((x==1)?"Subtask_1.1+2.1_MSA\\MSA_train_labeled.tsv":"Subtask_1.2+2.2_DA\\DA_train_labeled.tsv");
                        String sTrainingArff = "C:\\mnt\\data\\NADI2021\\nGrams\\arff/train." + x + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                        String sTest = sBasePath + ((x==1)?"Subtask_1.1+2.1_MSA\\MSA_dev_labeled.tsv":"Subtask_1.2+2.2_DA\\DA_dev_labeled.tsv");
                        String sTestArff = "C:\\mnt\\data\\NADI2021\\nGrams\\arff/test." + x + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                        String sTmp = "C:\\mnt\\data\\NADI2021\\nGrams\\tmp\\tmp." + x + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".txt";

                        new NADI2021_Subtask1x(n, t, ntype, sTraining, sTmp, sTrainingArff, sTest, sTestArff, 
                                NADI2021_Subtask1x.GetLabels(), SET.BOTH, 
                                new PreprocessingOptions(true, false, false, false, 0, new String[0])).Run();
                    }
                }
            }
        }
    }
}
