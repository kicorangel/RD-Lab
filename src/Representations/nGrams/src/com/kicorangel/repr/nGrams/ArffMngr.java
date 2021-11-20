/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kicorangel.repr.nGrams;

import java.io.FileWriter;
import java.util.ArrayList;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public class ArffMngr {
    public static void GenerateHeader(ArrayList<String> nGrams, ArrayList<String> classes, String type, int n, int total, FileWriter fw) {
        GenerateHeader(nGrams, new ArrayList<String>(), classes, type, n, total, fw);
    }
    
    public static void GenerateHeader(ArrayList<String> nGrams, ArrayList<String> custom, ArrayList<String> classes, String type, int n, int total, FileWriter fw) {
        try {
            fw.write("@relation '" + type + "-" + total + "-" + n + "-grams-by-@kicorangel'\n");
            for (int i=0;i<nGrams.size();i++) {
                fw.write("@attribute '" + CleanToken(nGrams.get(i), type, i) + "' real\n");
            }
            
            for (int i=0;i<custom.size();i++) {
                fw.write("@attribute 'Custom-" + custom.get(i) + "' real\n");
            }
            
            fw.write("@attribute 'class' {");
            for (int i=0;i<classes.size();i++) {
                fw.write("'" + classes.get(i) + "'");
                if (i<classes.size()-1) {
                    fw.write(",");
                }
            }
            fw.write("}\n");
            fw.write("@data\n");
        } catch (Exception ex) {
            
        }
    }
    
    public static void AddLine(String line, FileWriter fw) {
        try {
            fw.write(line + "\n");
        } catch (Exception ex) {
            
        }
    }
    
    private static String CleanToken(String token, String type, int i) {
        
        if (token.equalsIgnoreCase("class")) {
            token = "the_class";
        }
        
        token = token.replaceAll("'", "scom");
        while (token.contains("\\")) {
            token = token.replace("\\", "backslash");
        }
        
        return type + "-" + token + "-" + i;
    }
}
