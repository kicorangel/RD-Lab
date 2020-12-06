/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.common;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public class Triplet {
    public String Lemma1 = "";
    public String PoS1 = "";
    public String Relation = "";
    public String Lemma2 = "";
    public String PoS2 = "";
    
    public double tfcicf = 0;
    
    public Triplet() {
        
    }
    
    public Triplet(String line, boolean triplets) {
        if (triplets) {
            PopulateTriplet(line);
        } else {
            PopulateList(line);
        }
        
    }
    
    public String toString() {
        String sText = "";
        
        sText += Lemma1 + ":::" + Lemma2 + ":::" + tfcicf;
        
        return sText;
    }
    
    public void PopulateList(String line) {
        Lemma1 = line;
    }
    
    public void PopulateTriplet(String line) {
        String []info = line.split("\t");
        if (info.length==3) {
            String []s1 = info[0].split("_");
            if (s1.length>=2) {
                for (int i=0;i<s1.length-1;i++) {
                    Lemma1 += s1[i];
                }
                PoS1 = s1[s1.length-1];
            }
            
            String []s2 = info[2].split("_");
            if (s2.length>=2) {
                for (int i=0;i<s2.length-1;i++) {
                    Lemma2 += s2[i];
                }
                PoS2 = s2[s2.length-1];
            }
            
            Relation = info[1].replaceAll("<:", "");
            
            if (Lemma1.isEmpty() || Lemma2.isEmpty()) {
                Lemma1=Lemma1;
            }
        }
        
    }
}
