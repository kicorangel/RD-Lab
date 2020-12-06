package com.kicorangel.repr.ldse;

import com.kicorangel.repr.base.iWeka;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class Weka {
    
    public static String Header2Weka(ArrayList<String> Labels, int LDRVersion) {
        String sHeader =  "@relation 'LDSEv" + LDRVersion + "-by-@kicorangel'\n" ;
        for (int l=0;l<Labels.size();l++) {
            String sLabel = Labels.get(l).toUpperCase();
            if (LDRVersion==1) {
                sHeader += "@attribute '" + sLabel + "-avg' real\n";
                sHeader += "@attribute '" + sLabel + "-std' real\n";
                sHeader += "@attribute '" + sLabel + "-min' real\n";
                sHeader += "@attribute '" + sLabel + "-max' real\n";
                sHeader += "@attribute '" + sLabel + "-prob' real\n";
                sHeader += "@attribute '" + sLabel + "-prop' real\n";
            } else if (LDRVersion==2) {
                sHeader += "@attribute '" + sLabel + "-min' real\n";
                sHeader += "@attribute '" + sLabel + "-max' real\n";
                sHeader += "@attribute '" + sLabel + "-avg' real\n";
                sHeader += "@attribute '" + sLabel + "-med' real\n";
                sHeader += "@attribute '" + sLabel + "-Q1' real\n";
                sHeader += "@attribute '" + sLabel + "-Q3' real\n";
                sHeader += "@attribute '" + sLabel + "-G1' real\n";
                sHeader += "@attribute '" + sLabel + "-G2' real\n";
                for (int m=2;m<=10;m++) {
                    sHeader += "@attribute '" + sLabel + "-M" + m + "' real\n";
                }
            }
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
    
    public static String Features2Weka(Hashtable<String, ProbStats> oProbPerCountry, ArrayList<String> Labels, String classValue, int LDRVersion) {
        String sFeatures = "";
        for (int l=0;l<Labels.size();l++) {
            String sLabel = Labels.get(l).toLowerCase();
            ProbStats oProbStats = new ProbStats();
            if (oProbPerCountry.containsKey(sLabel)) {
                oProbStats = oProbPerCountry.get(sLabel);
            } else {    // Bilal fix for bug NaN
                oProbStats.LDRVersion = LDRVersion;
                oProbStats.Sum = 0.0;
                oProbStats.N = 0.0;
                oProbStats.Total = 1.0;
                oProbStats.Min = 0.0;
                oProbStats.CalculateStats();
            }
            
            if (LDRVersion==1) {
                sFeatures += String.format("%.10f", oProbStats.Avg).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.Std).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.Min).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.Max).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.Sum / oProbStats.Total).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.N / oProbStats.Total).replace(",", ".") + ",";
            } else if (LDRVersion==2) {
                sFeatures += String.format("%.10f", oProbStats.Min).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.Max).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.Avg).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.Median).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.Q1).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.Q3).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.G1).replace(",", ".") + ",";
                sFeatures += String.format("%.10f", oProbStats.G2).replace(",", ".") + ",";
                for (int m=2;m<=10;m++) {
                    sFeatures += String.format("%.10f", oProbStats.M.get(m-2)).replace(",", ".") + ",";
                }
            }
        }
        sFeatures += classValue.toUpperCase() + "\n";
        return sFeatures;
    }
}
