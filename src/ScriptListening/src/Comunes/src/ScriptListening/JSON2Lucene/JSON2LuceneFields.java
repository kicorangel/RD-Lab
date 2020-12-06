/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.JSON2Lucene;

import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;

/**
 *
 * @author @kicorangel
 */

public class JSON2LuceneFields {
    public String Field = "";
    public String IndexField = "";
    public boolean Process = false;
    public Store Store;
    public Index Index;
    public TermVector TermVector;
    public String Convert = "";
    public boolean Unique = false;
}
