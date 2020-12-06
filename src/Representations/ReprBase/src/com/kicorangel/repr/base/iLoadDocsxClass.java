package com.kicorangel.repr.base;

import java.util.Hashtable;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public interface iLoadDocsxClass {
    public Hashtable<String, Hashtable<String, Integer>> LoadDocsxClass(String sData, String label, int minSize, boolean verbose);
}
