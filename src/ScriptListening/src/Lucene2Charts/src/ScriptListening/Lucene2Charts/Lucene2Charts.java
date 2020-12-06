/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.Lucene2Charts;

import ScriptListening.CommonTools.IntegerComparator;
import ScriptListening.CommonTools.Tools;
import ScriptListening.Index.IndexMngr;
import ScriptListening.Lucene2CSVFields.Lucene2CSVFilters;
import ScriptListening.Lucene2Charts.Lucene2ChartsFilters;
import ScriptListening.Lucene2Charts.Lucene2ChartsInfo;
import ScriptListening.Lucene2Charts.Lucene2ChartsMngr;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeMap;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
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
public class Lucene2Charts {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        String sProyecto = args[0];
        Lucene2ChartsInfo oInfo = Lucene2ChartsMngr.GetChartInfo(sProyecto);
        
        switch(oInfo.Type.toUpperCase()) {
            case "DISTRIBUTION":
                DistributionChart(sProyecto, oInfo);
                break;
            case "EVOLUTION":
                EvolutionChart(sProyecto, oInfo);
                break;
            case "COUNT":
                CountChart(sProyecto, oInfo);
                break;
        }
        
        
    }
    
    private static void EvolutionChart(String proyecto, Lucene2ChartsInfo oInfo) throws IOException, ParseException {
        int iCnt = 0;
        
        StringBuilder sbChartData = new StringBuilder();
        StringBuilder sbChartCategories = new StringBuilder();
        
        String sINDEXPath = Lucene2ChartsMngr.GetINDEXPath(proyecto);
        ArrayList<Lucene2ChartsFilters> oFilters = Lucene2ChartsMngr.GetFilters(proyecto);
        
        IndexReader ir = IndexMngr.GetReader(sINDEXPath);
        IndexSearcher is = new IndexSearcher(ir);
        is.setDefaultFieldSortScoring(true, false);
        
        for (int i=oInfo.Ini;i<=oInfo.End;i++) {
            String sDate = i + "";
            String sIni = i + "0000000";
            String sEnd = i + "9999999";
            
            BooleanQuery q = new BooleanQuery();
            Query dtQuery = new TermRangeQuery("ctimestamp", 
                                sIni, 
                                sEnd, 
                                true, 
                                true);

                        q.add(dtQuery, BooleanClause.Occur.MUST);
                        
            if (oFilters.size()>0) {    // There are filters
                for (int iFilter=0;iFilter<oFilters.size();iFilter++) {
                    Lucene2ChartsFilters oFilter = oFilters.get(iFilter);

                    if (oFilter.Type.equalsIgnoreCase("TermRangeQuery")) {
                        dtQuery = new TermRangeQuery(oFilter.Field, 
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
            }

            
            iCnt = is.search(q, 1).totalHits;
            
            if (!Tools.IsValidDate(sDate)) {
                continue;
            }

            System.out.println(sDate + "," + iCnt);
            
            
            if (sbChartData.length()>0) {
                sbChartData.append(",");
            }
            sbChartData.append(iCnt);
            
            
            if (sbChartCategories.length()>0) {
                sbChartCategories.append(",");
            }
            sbChartCategories.append("'").append(Tools.FormatDate(sDate)).append("'");
        }
        
        is.close();
        ir.close();
        
        String sTemplate = Lucene2ChartsMngr.GetTemplate(proyecto);
        FileReader fr = new FileReader(sTemplate);
        BufferedReader bf = new BufferedReader(fr);
        StringBuilder sbTemplateData = new StringBuilder();
        String sLine = "";
        while ((sLine = bf.readLine())!=null) {
            sbTemplateData.append(sLine + "\n");
        }
        String sTemplateData = sbTemplateData.toString()
                .replace("##TITLE##", oInfo.Title)
                .replace("##SUBTITLE##", oInfo.SubTitle)
                .replace("##CHARTDATA##", sbChartData.toString())
                .replaceAll("##PROYECTO##", Tools.GetNOMBRE(proyecto))
                .replace("##XAXIS##", oInfo.XAxis)
                .replace("##YAXIS##", oInfo.YAxis)
                .replace("##CATEGORIES##", sbChartCategories.toString())
        ;
        String sChartPath = Lucene2ChartsMngr.GetChartPath(proyecto);
        FileWriter fw = new FileWriter(sChartPath);
        fw.write(sTemplateData);
        fw.close();
    }
    
    private static void DistributionChart(String proyecto, Lucene2ChartsInfo oInfo) throws IOException, ParseException {
        Hashtable<String, Integer> oDistribution = new Hashtable<String, Integer>();
        
        String sINDEXPath = Lucene2ChartsMngr.GetINDEXPath(proyecto);
        ArrayList<Lucene2ChartsFilters> oFilters = Lucene2ChartsMngr.GetFilters(proyecto);
        
        if (oFilters.size()>0) {    // There are filters
            IndexReader ir = IndexMngr.GetReader(sINDEXPath);
            IndexSearcher is = new IndexSearcher(ir);
            is.setDefaultFieldSortScoring(true, false);

            BooleanQuery q = new BooleanQuery();
        
            for (int iFilter=0;iFilter<oFilters.size();iFilter++) {
                Lucene2ChartsFilters oFilter = oFilters.get(iFilter);

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
                    if (doc.getValues(oInfo.GroupField.toLowerCase()).length>0) {
                        String sKey = doc.getValues(oInfo.GroupField.toLowerCase())[0];
                        if (sKey.trim().isEmpty()) {
                            sKey = "NORECONOCIDO";
                        }
                        int iVal = 0;
                        if (oDistribution.containsKey(sKey)) {
                            iVal = oDistribution.get(sKey);
                        }
                        oDistribution.put(sKey, ++iVal);
                    }
                }
            }
            
            is.close();
            ir.close();
        } else {
            IndexReader reader = IndexMngr.GetReader(sINDEXPath);
            for (int i=0; i<reader.maxDoc(); i++) {
                if (reader.isDeleted(i)) continue;
                Document doc = reader.document(i); 
                if (doc.getValues(oInfo.GroupField.toLowerCase()).length>0) {
                    String sKey = doc.getValues(oInfo.GroupField.toLowerCase())[0];
                    if (sKey.trim().isEmpty()) {
                        sKey = "NORECONOCIDO";
                    }
                    int iVal = 0;
                    if (oDistribution.containsKey(sKey)) {
                        iVal = oDistribution.get(sKey);
                    }
                    oDistribution.put(sKey, iVal++);
                }
            }
            reader.close();
        }
        
        IntegerComparator bvc = new IntegerComparator(oDistribution);
        TreeMap<String, Integer> sortedMap = new TreeMap<String, Integer>(bvc);
        sortedMap.putAll(oDistribution);
        
        StringBuilder sbChartData = new StringBuilder();
        int iGroup = 0;
        int theRest = 0;
        Set keySet = sortedMap.keySet();
        Object[] keys = keySet.toArray();
        for (int i=0;i<keys.length;i++) {
            String sKey = (String)keys[i];
            int iVal = oDistribution.get(sKey);
            
            System.out.println(sKey + ": " + iVal);
            iGroup++;
            
            if (iGroup<=oInfo.NGroups) {
                if (sbChartData.length()>0) {
                    sbChartData.append(",");
                }
                sbChartData.append("{name:'").append(sKey).append("',y:").append(iVal).append("}");
            } else {
                theRest += iVal;
            } 
        }
        if (theRest>0) {
            sbChartData.append(",{name:'Otros',y:").append(theRest).append("}");
        }
        
        String sTemplate = Lucene2ChartsMngr.GetTemplate(proyecto);
        FileReader fr = new FileReader(sTemplate);
        BufferedReader bf = new BufferedReader(fr);
        StringBuilder sbTemplateData = new StringBuilder();
        String sLine = "";
        while ((sLine = bf.readLine())!=null) {
            sbTemplateData.append(sLine + "\n");
        }
        String sTemplateData = sbTemplateData.toString()
                .replace("##TITLE##", oInfo.Title)
                .replace("##SUBTITLE##", oInfo.SubTitle)
                .replace("##CHARTDATA##", sbChartData.toString())
                .replaceAll("##PROYECTO##", Tools.GetNOMBRE(proyecto))
                .replaceAll("##NGROUPS##", String.valueOf(oInfo.NGroups))
                .replaceAll("##TOTAL##", String.valueOf(oDistribution.size()))
        ;
        String sChartPath = Lucene2ChartsMngr.GetChartPath(proyecto);
        FileWriter fw = new FileWriter(sChartPath);
        fw.write(sTemplateData);
        fw.close();
    }
    
    private static void CountChart(String proyecto, Lucene2ChartsInfo oInfo) throws IOException, ParseException {
        int iCnt = 0;
        
        String sINDEXPath = Lucene2ChartsMngr.GetINDEXPath(proyecto);
        ArrayList<Lucene2ChartsFilters> oFilters = Lucene2ChartsMngr.GetFilters(proyecto);
        
        if (oFilters.size()>0) {    // There are filters
            IndexReader ir = IndexMngr.GetReader(sINDEXPath);
            IndexSearcher is = new IndexSearcher(ir);
            is.setDefaultFieldSortScoring(true, false);

            BooleanQuery q = new BooleanQuery();
        
            for (int iFilter=0;iFilter<oFilters.size();iFilter++) {
                Lucene2ChartsFilters oFilter = oFilters.get(iFilter);

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
            iCnt = is.search(q, 1).totalHits;
            
            is.close();
            ir.close();
        } else {
            IndexReader reader = IndexMngr.GetReader(sINDEXPath);
            iCnt = reader.maxDoc();
            reader.close();
        }
            
        System.out.println(oInfo.Title + iCnt);
    }
}
