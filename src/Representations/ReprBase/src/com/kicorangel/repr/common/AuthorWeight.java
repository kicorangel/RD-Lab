/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.common;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public class AuthorWeight {
    public String Author = "";
    public String Lang = "";
    public Hashtable<String, Double> WordWeight = new Hashtable<String, Double>();
    public ArrayList<Double> ListOfWeights = new ArrayList<Double>();
}
