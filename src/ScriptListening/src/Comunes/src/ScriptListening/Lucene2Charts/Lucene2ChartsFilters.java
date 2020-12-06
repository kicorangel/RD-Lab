/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.Lucene2Charts;

import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.search.BooleanClause.Occur;

/**
 *
 * @author @kicorangel
 */
public class Lucene2ChartsFilters {
    public String Type = "";
    public String Field = "";
    public String Value = "";
    public String Ini = "";
    public boolean IncludeIni = false;
    public String End = "";
    public boolean IncludeEnd = false;
    public Occur Occur;
}
