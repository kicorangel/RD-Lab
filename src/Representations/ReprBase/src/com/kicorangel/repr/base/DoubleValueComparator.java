/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.base;

import com.kicorangel.repr.enumerations.Ordering;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public class DoubleValueComparator implements Comparator<String> {

    Map<String, Double> base;
    Ordering order;
    public DoubleValueComparator(Map<String, Double> base, Ordering order) {
        this.base = base;
        this.order = order;
    }
    
//    public int compare(String a, String b) {
//        return (a.compareToIgnoreCase(b));
//    }
    

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) <= base.get(b)) {
            if (order==Ordering.ASC) return -1;
            else return 1;
        } else {
            if (order==Ordering.ASC) return 1;
            else return -1;
        } // returning 0 would merge keys
        
    }
}
