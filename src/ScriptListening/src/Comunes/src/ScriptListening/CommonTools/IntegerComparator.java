/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.CommonTools;

import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author @kicorangel
 */
public class IntegerComparator implements Comparator<String> {

    Map<String, Integer> base;
    public IntegerComparator(Map<String, Integer> base) {
        this.base = base;
    }
    
    

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}

