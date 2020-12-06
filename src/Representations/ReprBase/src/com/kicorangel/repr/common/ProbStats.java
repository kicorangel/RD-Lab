/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.common;

import java.util.ArrayList;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public class ProbStats {
    public double Total = 0;    // Total terms in document
    public double N = 0;        // Total terms in document corresponding to cluster couples
    public double Sum = 0;      // Total frequency of couples in document
    public double Min = Double.MAX_VALUE;
    public double Max = 0;
    public double Avg = 0;
    public double Std = 0;
    public double Pearson = 0;      // Std / Avg
    public double Gini = 0;         // Gini concentration index
    public double G1 = 0;           // Fisher asymethry index
    public double G2 = 0;           // Fisher curtosis index
    public ArrayList<Double> Values = new ArrayList<Double>();
    
    public void CalculateStats() {
        Avg = Sum / N;
        
        for (int i=0;i<Values.size();i++) {
            Std += Math.pow(Values.get(i) - Avg, 2);
        }
        Std = Math.sqrt(Std/N);
        Pearson = Std / Avg;
    }
}
