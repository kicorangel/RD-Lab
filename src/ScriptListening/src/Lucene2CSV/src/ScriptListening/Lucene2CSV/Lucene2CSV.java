/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.Lucene2CSV;

import ScriptListening.Index.IndexMngr;
import ScriptListening.JSON2CSV.JSON2CSVFields;
import java.util.ArrayList;
import ScriptListening.Lucene2CSVFields.*;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;

/**
 *
 * @author @kicorangel
 */
public class Lucene2CSV {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        String sProyecto = args[0];
        String sINDEXPath = Lucene2CSVMngr.GetINDEXPath(sProyecto);
        String sCSVPath = Lucene2CSVMngr.GetCSVPath(sProyecto);
        ArrayList<Lucene2CSVFields> oFields = Lucene2CSVMngr.GetFields(sProyecto);
        ArrayList<Lucene2CSVFilters> oFilters = Lucene2CSVMngr.GetFilters(sProyecto);
        
        
        GenerateUniqueCSV(sProyecto, sINDEXPath, sCSVPath, oFields, oFilters);
    }
    
    private static void GenerateUniqueCSV(String proyecto, String indexPath, String csvPath, ArrayList<Lucene2CSVFields> fields, ArrayList<Lucene2CSVFilters> filters) throws IOException, ParseException {
        FileWriter fw = new FileWriter(csvPath);
        
        // CSV Header
        StringBuilder sb = new StringBuilder();
        for (int iField=0;iField<fields.size();iField++) {
            Lucene2CSVFields oField = fields.get(iField);
            if (oField.Process) {
                sb.append("\"" + oField.Field + "\",");
            }
        }
        String sLine = sb.toString();
        if (sLine.endsWith(",")) {
            sLine = sLine.substring(0, sLine.length() - 1);
        }
        fw.write(sLine + "\n");
        
        if (filters.size()==0) {   // NO FILTER, ITERATE AMONG THE WHOLE DATASET
            IndexReader reader = IndexMngr.GetReader(indexPath);
            for (int i=0; i<reader.maxDoc(); i++) {
                if (reader.isDeleted(i)) continue;
                Document doc = reader.document(i); 

                ProcessDocument(doc, fields, fw);
                
            }
        } else {    // There are filters
            BooleanQuery q = new BooleanQuery();
            for (int iFilter=0;iFilter<filters.size();iFilter++) {
                Lucene2CSVFilters oFilter = filters.get(iFilter);
                
                if (oFilter.Type.equalsIgnoreCase("TermRangeQuery")) {
                    Query dtQuery = new TermRangeQuery(oFilter.Field, 
                            oFilter.Ini, 
                            oFilter.End, 
                            oFilter.IncludeIni, 
                            oFilter.IncludeEnd);
                    
                    q.add(dtQuery, oFilter.Occur);
                } else if (oFilter.Type.equalsIgnoreCase("TermQuery")) {
                    Query tQuery = new TermQuery(new Term(oFilter.Field, oFilter.Value));
                    q.add(tQuery, oFilter.Occur);
                } else {
                    QueryParser qp = new QueryParser(Version.LUCENE_30, oFilter.Field, new StandardAnalyzer(Version.LUCENE_20));
                    Query textQuery = qp.parse(oFilter.Value);
                    q.add(textQuery, oFilter.Occur);
                }
            }
            
            IndexReader ir = IndexMngr.GetReader(indexPath);
            IndexSearcher is = new IndexSearcher(ir);
            is.setDefaultFieldSortScoring(true, false);
            int iNDocs = is.search(q, 1).totalHits;
            if (iNDocs>0) {
                TopDocs hits = is.search(q, iNDocs);
                int Total = hits.scoreDocs.length;
                for (int i=0;i<Total;i++) {
                    int docId = hits.scoreDocs[i].doc;
                    Document doc = is.doc(docId);
                    ProcessDocument(doc, fields, fw);
                }
            }
            
            fw.close();
        }
    }
    
    private static void ProcessDocument(Document doc, ArrayList<Lucene2CSVFields> fields, FileWriter fw) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int iField=0;iField<fields.size();iField++) {
            Lucene2CSVFields oField = fields.get(iField);
            if (oField.Process) {
//                String sFieldValue = GetFieldValue(oField.Field, json, "");
                String sFieldValue = doc.getValues(oField.Field)[0];
                sFieldValue = sFieldValue.replaceAll("\n", " ");
                sb.append("\"").append(sFieldValue).append("\",");
            }
        }
        String sLine = sb.toString();
        if (sLine.endsWith(",")) {
            sLine = sLine.substring(0, sLine.length() - 1);
        }
        fw.write(sLine + "\n");
    }
    
}
