package com.autoritas.repr.ldr;

import com.autoritas.repr.ldr.datasets.AutoritasSentimentAnalysis;
import com.autoritas.repr.ldr.datasets.HCE_HIL;
import com.autoritas.repr.ldr.datasets.HispaBlogs;
import com.autoritas.repr.ldr.datasets.IberEval17_Gender;
import com.autoritas.repr.ldr.datasets.IberEval17_Stance;
import com.autoritas.repr.ldr.datasets.PAN_AP_14_age;
import com.autoritas.repr.ldr.datasets.PAN_AP_14_age_3groups;
import com.autoritas.repr.ldr.datasets.PAN_AP_16_age;
import com.autoritas.repr.ldr.datasets.PAN_AP_17_gender;
import com.autoritas.repr.ldr.datasets.PAN_AP_17_variety;
import com.autoritas.repr.ldr.datasets.Replab14;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import weka.core.Instance;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class LowDimensionalityRepresentation {

    public static void main(String[] args) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        /* AUTORITAS SENTIMENT ANALYSIS EXAMPLE */
        new AutoritasSentimentAnalysis(5, 1, 
                    "/mnt/data/SentimentAnalysis/tass2016.general.csv",
                "/mnt/data/SentimentAnalysis/ldr.tass2016.2/",
                "/mnt/data/SentimentAnalysis/ldr.tass2016.2/tass2016.arff",
                "/mnt/data/SentimentAnalysis/tass2016.general.test.csv",
                "/mnt/data/SentimentAnalysis/ldr.tass2016.2/test.tass2016.arff"
        ).Run();
        /**/   
    }
    
}


