package com.kicorangel.repr.ldse;

import static com.kicorangel.repr.ldse.FreqMngr.*;
import java.util.ArrayList;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class ProbStats {
    public double LDRVersion = 1;   // LDR Version
    
    public double Total = 0;    // Total terms in document
    public double N = 0;        // Total terms in document corresponding to cluster couples
    public double Sum = 0;      // Total frequency of couples in document
    public double Min = Double.MAX_VALUE;
    public double Max = 0;
    public double Avg = 0;
    public double Std = 0;
    public double Pearson = 0;      // Std / Avg
    public double Gini = 0;         // Gini concentration index
    
    public ArrayList<Double> Values = new ArrayList<Double>();
    
    // Version 2
    public double Q1 = 0;
    public double Q3 = 0;
    public double Median = 0;
    public ArrayList<Double> M = new ArrayList<Double>();
    public double G1 = 0;           // Fisher asymethry index
    public double G2 = 0;           // Fisher curtosis index
    
    public void CalculateStats() {
        if (N>0) {
            Avg = Sum / N;
//        if (LDRVersion==1) {
            Std = Std(Values);
            Pearson = Std / Avg;
//        }
        } else  {
            Avg = Std = Pearson = 0;
        }
        
        
        if (LDRVersion==2) {
            Values = SortValues(Values);
            Q1 = Q1(Values);
            Q3 = Q3(Values);
            Median = Median(Values);
            
            for (int i=2;i<=10;i++) {
                M.add(M(Values, i));
            }
            
            if (Std!=0) {
                G1 = (M.get(3-2)/Math.pow(Std, 3));
                G2 = ((M.get(4-2)/Math.pow(Std, 4))-3);
            } else {
                G1 = G2 = 0;
            }
        }
    }
    
}
