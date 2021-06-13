/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.ldse.Test;

import com.kicorangel.repr.ldse.Datasets.NADI2021_Subtask1x;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author frangel
 */
public class T_NADI2021 {
    public static void Test() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
        /* NADI2021_Subtask1x all possibilities */
//        for (int freq:new Integer[]{1, 2, 3, 5, 10}) {
//            for (int size:new Integer[]{1, 2, 3}) {
//                for (int v:new Integer[]{1,2}) {
//                    for (int x:new Integer[]{1,2}) {
//                        NADI2021_Subtask1x(x, freq, size, v);
//                    }
//                }
//            }
//        }

        NADI2021_Subtask1x(2, 10, 2, 1);
    }
    
    private static void NADI2021_Subtask1x(int x, int minFreq, int minSize, int LDRVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBasePath = "C:\\mnt\\data\\NADI2021\\NADI2021_DEV.1.0\\";
        String sCorpusPath = sBasePath + ((x==1)?"Subtask_1.1+2.1_MSA\\MSA_train_labeled.tsv":"Subtask_1.2+2.2_DA\\DA_train_labeled.tsv");
        String sLDRPath = "C:\\mnt\\data\\NADI2021\\ldr\\data\\" + x + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVer;
        String sTrainingArff = "C:\\mnt\\data\\NADI2021\\ldr\\arff/" + x + ".train.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        String sTestPath = sBasePath + ((x==1)?"Subtask_1.1+2.1_MSA\\MSA_dev_labeled.tsv":"Subtask_1.2+2.2_DA\\DA_dev_labeled.tsv");
        String sTestArff = "C:\\mnt\\data\\NADI2021\\ldr\\arff/" + x + ".test.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        
        if (!new File(sLDRPath).exists()) {
            new File(sLDRPath).mkdir();
        }
         
        new NADI2021_Subtask1x(minFreq, minSize, sCorpusPath, sLDRPath, sTrainingArff, sTestPath, sTestArff, LDRVer).Run();
    }
}
