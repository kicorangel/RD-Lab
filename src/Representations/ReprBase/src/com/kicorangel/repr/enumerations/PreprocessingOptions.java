/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kicorangel.repr.enumerations;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public class PreprocessingOptions {
    public boolean tolowerCase = false;
    public boolean removePunctuation = false;
    public boolean removeNumbers = false;
    public boolean removeWhitespaces = false;
    public int minTokenSize = 0;
    public String []stopwords = new String[0];
    
    public PreprocessingOptions(boolean tolowerCase, boolean removePunctuation, boolean removeNumbers, boolean removeWhitespaces, int minTokenSize, String []stopWords) {
        this.tolowerCase = tolowerCase;
        this.removePunctuation = removePunctuation;
        this.removeNumbers = removeNumbers;
        this.removeWhitespaces = removeWhitespaces;
        this.stopwords = stopWords;
        this.minTokenSize = minTokenSize;
    }
}
