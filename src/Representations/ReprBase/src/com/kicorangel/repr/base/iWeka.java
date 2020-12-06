/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kicorangel.repr.base;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public interface iWeka {
    public String Header2Weka(ArrayList<String> Labels);
    public String Features2Weka(Hashtable documents, ArrayList<String> Labels, String classValue);
}
