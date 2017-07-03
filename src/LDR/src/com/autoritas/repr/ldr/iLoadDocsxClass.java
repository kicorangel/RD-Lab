package com.autoritas.repr.ldr;

import java.util.Hashtable;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public interface iLoadDocsxClass {
    public Hashtable<String, Hashtable<String, Integer>> LoadDocsxClass(String sData, String label, int minSize, boolean verbose);
}
