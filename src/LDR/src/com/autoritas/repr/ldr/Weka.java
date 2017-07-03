package com.autoritas.repr.ldr;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class Weka {
    
    public static String Header2Weka(ArrayList<String> Labels) {
        String sHeader =  "@relation 'LDR-by-@kicorangel'\n" ;
        for (int l=0;l<Labels.size();l++) {
            String sLabel = Labels.get(l).toUpperCase();
            sHeader += "@attribute '" + sLabel + "-avg' real\n";
            sHeader += "@attribute '" + sLabel + "-std' real\n";
            sHeader += "@attribute '" + sLabel + "-min' real\n";
            sHeader += "@attribute '" + sLabel + "-max' real\n";
            sHeader += "@attribute '" + sLabel + "-prob' real\n";
            sHeader += "@attribute '" + sLabel + "-prop' real\n";
        }
                
        sHeader += "@attribute 'class' {";
        for (int l=0;l<Labels.size();l++) {
            sHeader += "'" + Labels.get(l).toUpperCase() + "'";
            if (l<Labels.size()-1) {
                sHeader += ",";
            }
        }
        sHeader += "}\n";
        sHeader += "@data\n";
        return sHeader;
    }
    
    public static String Features2Weka(Hashtable<String, ProbStats> oProbPerCountry, ArrayList<String> Labels, double n, String classValue) {
        String sFeatures = "";
        for (int l=0;l<Labels.size();l++) {
            String sLabel = Labels.get(l).toLowerCase();
            ProbStats oProbStats = new ProbStats();
            if (oProbPerCountry.containsKey(sLabel)) {
                oProbStats = oProbPerCountry.get(sLabel);
            }
            sFeatures += String.format("%.10f", oProbStats.Avg).replace(",", ".") + ",";
            sFeatures += String.format("%.10f", oProbStats.Std).replace(",", ".") + ",";
            sFeatures += String.format("%.10f", oProbStats.Min).replace(",", ".") + ",";
            sFeatures += String.format("%.10f", oProbStats.Max).replace(",", ".") + ",";
            sFeatures += String.format("%.10f", oProbStats.Sum / oProbStats.Total).replace(",", ".") + ",";
            sFeatures += String.format("%.10f", oProbStats.N / oProbStats.Total).replace(",", ".") + ",";
        }
        sFeatures += classValue.toUpperCase() + "\n";
        return sFeatures;
    }
}
