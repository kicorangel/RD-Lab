/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.nGrams.Test;

import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.nGrams.Datasets.BotType;
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
public class BotTypes {
    public static void Test() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (String botType:new String[]{/*"astroturf", "spammer", "financial", "fakefollower", "selfdeclared",*/ "other"}) {
            for (int t:new Integer[] {1000}) {
//                for (NGRAMTYPE ntype:NGRAMTYPE.values()) {
                    NGRAMTYPE ntype = NGRAMTYPE.WORD; {
                    for (int n=1;n<=5;n++) {
                        System.out.println("-->Processing " + ntype.toString() + ".n-" + n + ".t-" + t);
                        String sTraining = "C:\\mnt\\data\\botometer\\XML\\";
                        String sTrainingArff = "C:\\mnt\\data\\botometer\\arff\\train." + botType + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                        String sTest = "";
                        String sTestArff = "";
                        String sTmp = "C:\\mnt\\data\\botometer\\tmp\\tmp." + botType + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".txt";

                        new BotType(botType, n, t, ntype, sTraining, sTmp, sTrainingArff, sTest, sTestArff, 
                                BotType.GetLabels(), SET.TRAINING, 
                                new PreprocessingOptions(true, false, false, false, 0, new String[0])).Run();
                    }
                }
            }
        }
    }
}
