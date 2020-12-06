/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.Lucene2Topics;

import org.apache.lucene.search.BooleanClause;

/**
 *
 * @author @kicorangel
 */
public class Lucene2TopicsFilters {
    public String Type = "";
    public String Field = "";
    public String Value = "";
    public String Ini = "";
    public boolean IncludeIni = false;
    public String End = "";
    public boolean IncludeEnd = false;
    public BooleanClause.Occur Occur;
}
