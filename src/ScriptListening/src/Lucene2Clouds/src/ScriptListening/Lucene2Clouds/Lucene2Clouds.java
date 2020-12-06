/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.Lucene2Clouds;

import ScriptListening.CommonTools.IntegerComparator;
import ScriptListening.CommonTools.TermMngr;
import ScriptListening.CommonTools.Tools;
import ScriptListening.Index.IndexMngr;
import ScriptListening.Lucene2Clouds.Lucene2CloudsFilters;
import ScriptListening.Lucene2Clouds.Lucene2CloudsInfo;
import ScriptListening.Lucene2Clouds.Lucene2CloudsMngr;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeMap;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
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
public class Lucene2Clouds {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        
        String sProyecto = args[0];
        Lucene2CloudsInfo oInfo = Lucene2CloudsMngr.GetChartInfo(sProyecto);
        
        String sINDEXPath = Lucene2CloudsMngr.GetINDEXPath(sProyecto);
        ArrayList<Lucene2CloudsFilters> oFilters = Lucene2CloudsMngr.GetFilters(sProyecto);
        
        StringBuilder sbText = new StringBuilder();
        
        if (oFilters.size()>0) {    // There are filters
            IndexReader ir = IndexMngr.GetReader(sINDEXPath);
            IndexSearcher is = new IndexSearcher(ir);
            is.setDefaultFieldSortScoring(true, false);

            BooleanQuery q = new BooleanQuery();
        
            for (int iFilter=0;iFilter<oFilters.size();iFilter++) {
                Lucene2CloudsFilters oFilter = oFilters.get(iFilter);

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
            int iNDocs = is.search(q, 1).totalHits;
            if (iNDocs>0) {
                TopDocs hits = is.search(q, iNDocs);
                int Total = hits.scoreDocs.length;
                for (int i=0;i<Total;i++) {
                    int docId = hits.scoreDocs[i].doc;
                    Document doc = is.doc(docId);
                    sbText.append(doc.getValues(oInfo.Field)[0]).append(" ");
                }
            }
            
            is.close();
            ir.close();
        } else {
            IndexReader reader = IndexMngr.GetReader(sINDEXPath);
            for (int i=0; i<reader.maxDoc(); i++) {
                if (reader.isDeleted(i)) continue;
                Document doc = reader.document(i); 
                sbText.append(doc.getValues(oInfo.Field)[0]).append(" ");
            }
            reader.close();
        }
        
        TermFreqVector terms = TermMngr.GetTermVector(sbText.toString(), new StopAnalyzer(Version.LUCENE_30, new File(oInfo.StopWords)));
        Hashtable<String, Integer> oTerms = new Hashtable<String, Integer>();
        for (int i=0;i<terms.size();i++) {
            oTerms.put(terms.getTerms()[i], terms.getTermFrequencies()[i]);
        }
        
        IntegerComparator bvc = new IntegerComparator(oTerms);
        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
        sortedMap.putAll(oTerms);
        
        sbText = new StringBuilder();
        Set keySet = sortedMap.keySet();
        Object[] keys = keySet.toArray();
        for (int i=0;i<keys.length;i++) {
            String sKey = (String)keys[i];
            int iVal = oTerms.get(sKey);
            
            System.out.println(sKey + "," + iVal);
            if (i<oInfo.NWords) {
                int iTimes = Scale(iVal, oInfo.Scale);
                for (int j=0;j<iTimes;j++) {
                    sbText.append(sKey).append(" ");
                }
            }
        }
       
        String sTemplate = Lucene2CloudsMngr.GetTemplate(sProyecto);
        FileReader fr = new FileReader(sTemplate);
        BufferedReader bf = new BufferedReader(fr);
        StringBuilder sbTemplateData = new StringBuilder();
        String sLine = "";
        while ((sLine = bf.readLine())!=null) {
            sbTemplateData.append(sLine + "\n");
        }
        String sTemplateData = sbTemplateData.toString()
                .replaceAll("##PROYECTO##", Tools.GetNOMBRE(sProyecto))
                .replace("##TITLE##", oInfo.Title)
                .replace("##TEXT##", sbText.toString())
        ;
        String sChartPath = Lucene2CloudsMngr.GetChartPath(sProyecto);
        FileWriter fw = new FileWriter(sChartPath, Charset.forName("iso-8859-1"));
        fw.write(sTemplateData);
        fw.close();
    }
    
    private static int Scale(int toScale, String scale) {
        if (scale.equalsIgnoreCase("LOG")) {
            return (int)Math.round(Math.log(toScale));
        } else if (scale.equalsIgnoreCase("LOG10")) {
            return (int)Math.round(Math.log10(toScale));
        } else if (scale.equalsIgnoreCase("SQR")) {
            return (toScale*toScale);
        } else {
            return toScale;
        }
    }
}
