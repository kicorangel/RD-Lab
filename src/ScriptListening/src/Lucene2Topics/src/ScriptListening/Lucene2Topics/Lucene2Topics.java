/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.Lucene2Topics;

import ScriptListening.Index.IndexMngr;
import ScriptListening.Lucene2Topics.Lucene2TopicsFilters;
import ScriptListening.Lucene2Topics.Lucene2TopicsInfo;
import ScriptListening.Lucene2Topics.Lucene2TopicsMngr;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
import org.apache.lucene.util.Version;

/**
 *
 * @author @kicorangel
 * @LDA: https://github.com/kymmt90/LDA
 */
public class Lucene2Topics {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException, Exception {
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        String sProyecto = args[0];
        String sINDEXPath = Lucene2TopicsMngr.GetINDEXPath(sProyecto);
        ArrayList<Lucene2TopicsFilters> oFilters = Lucene2TopicsMngr.GetFilters(sProyecto);
        Lucene2TopicsInfo oInfo = Lucene2TopicsMngr.GetChartInfo(sProyecto);
                
        Hashtable<String, ArrayList<TopicTerms>> oTopics = ProcessTopics(oInfo, oFilters, sINDEXPath);
        Hashtable<Integer, Hashtable<String, Integer>> oTopicsPerDay = LoadTopicsPerDay(sINDEXPath, oInfo, oFilters, oTopics);
        GenerateChart(sProyecto, oInfo, oTopics, oTopicsPerDay);
    }
    
    private static Hashtable<String, ArrayList<TopicTerms>> ProcessTopics(Lucene2TopicsInfo oInfo, ArrayList<Lucene2TopicsFilters> oFilters, String sINDEXPath) throws IOException, ParseException, Exception {
        Hashtable<String, ArrayList<TopicTerms>> oTopics = new Hashtable<String, ArrayList<TopicTerms>>();
        TopicsExtractor topicsExtractor = new TopicsExtractor(oInfo, oFilters, sINDEXPath);
        topicsExtractor.GenerateVocabulary();
        topicsExtractor.GenerateDocWords();
        oTopics = topicsExtractor.ExtractTopics(); 
        return oTopics;
    }
    
    private static void GenerateChart(String proyecto, Lucene2TopicsInfo oInfo, Hashtable<String, ArrayList<TopicTerms>> oTopics, Hashtable<Integer, Hashtable<String, Integer>> oTopicsPerDay) throws FileNotFoundException, IOException {
        String sTemplate = Lucene2TopicsMngr.GetTemplate(proyecto);
        FileReader fr = new FileReader(sTemplate);
        BufferedReader bf = new BufferedReader(fr);
        StringBuilder sbTemplateData = new StringBuilder();
        String sLine = "";
        while ((sLine = bf.readLine())!=null) {
            sbTemplateData.append(sLine).append("\n");
        }
        
        Hashtable<String, StringBuilder> oTopicsFreqList = new Hashtable<String, StringBuilder>();
        StringBuilder sbCategories = new StringBuilder();
        
        for (int iDay=oInfo.Ini;iDay<=oInfo.End;iDay++) {
            if (sbCategories.length()>0) {
                sbCategories.append(",");
            }
            sbCategories.append("'").append(ScriptListening.CommonTools.Tools.FormatDate(iDay + "")).append("'");
            
            Hashtable<String, Integer> oTopicsFreq = oTopicsPerDay.get(iDay);
            for (int iTopic=0;iTopic<oInfo.NTopics;iTopic++) {
                String sTopic = "t" + iTopic;
                int iCnt = oTopicsFreq.get(sTopic);
                
                StringBuilder sb = new StringBuilder();
                if (oTopicsFreqList.containsKey(sTopic)) {
                    sb = oTopicsFreqList.get(sTopic);
                    sb.append(",");
                }
                sb.append(iCnt);
                oTopicsFreqList.put(sTopic, sb);
            }
        }
        
        StringBuilder sbTopics = new StringBuilder();
        StringBuilder sbData = new StringBuilder();
        for (int iTopic=0;iTopic<oInfo.NTopics;iTopic++) {
            String sTopic = "t" + iTopic;
            StringBuilder sbFreqs = oTopicsFreqList.get(sTopic);
            
            if (sbData.length()>0) {
                sbData.append(",\n\t\t");
            }
            sbData.append("{name:'").append(sTopic).append("',data:[").append(sbFreqs.toString()).append("]}");
            
            StringBuilder append = sbTopics.append("<b>").append(sTopic).append("</b>").append(": ");
            ArrayList<TopicTerms> oTerms = oTopics.get(sTopic);
            for (int iTerm=0;iTerm<oTerms.size();iTerm++) {
                sbTopics.append("[").append(oTerms.get(iTerm).Term).append(", ").append(oTerms.get(iTerm).Prob).append("]");
                if (iTerm<oTerms.size()-1) {
                    sbTopics.append(", ");
                } else {
                    sbTopics.append("<br/>\n");
                }
            }
        }
        
        String sTemplateData = sbTemplateData.toString()
                .replaceAll("##PROYECTO##", ScriptListening.CommonTools.Tools.GetNOMBRE(proyecto))
                .replace("##TITLE##", oInfo.Title)
                .replace("##SUBTITLE##", oInfo.SubTitle)
                .replace("##CATEGORIES##", sbCategories.toString())
                .replace("##DATA##", sbData.toString())
                .replace("##TOPICS##", sbTopics.toString())
        ;
        String sChartPath = Lucene2TopicsMngr.GetCHART(proyecto);
        FileWriter fw = new FileWriter(sChartPath, Charset.forName("iso-8859-1"));
        fw.write(sTemplateData);
        fw.close();
    }
        
    private static Hashtable<Integer, Hashtable<String, Integer>> LoadTopicsPerDay(String path2Index, Lucene2TopicsInfo oInfo, ArrayList<Lucene2TopicsFilters> oFilters, Hashtable<String, ArrayList<TopicTerms>> oTopics) throws ParseException, IOException {
        Hashtable<Integer, Hashtable<String, Integer>> oTopicsPerDay = new Hashtable<Integer, Hashtable<String, Integer>>();
        
        int ini = oInfo.Ini;
        while (ini<= oInfo.End) {
            if (!ScriptListening.CommonTools.Tools.IsValidDate(ini + "")) {
                ini++;
                continue;
            }
            Hashtable<String, Integer> oTopicsFreq = new Hashtable<String, Integer>();
            if (oTopicsPerDay.containsKey(ini)) {
                oTopicsFreq = oTopicsPerDay.get(ini);
            }
            System.out.println("Processing day " + ini);
            Enumeration keys = oTopics.keys();
            while (keys.hasMoreElements()) {
                String sTopic = (String)keys.nextElement();
                ArrayList<TopicTerms> Terms = oTopics.get(sTopic);
                int iCount = 0;
                for (int iTerm=0;iTerm<Terms.size();iTerm++) {
                    String sTerm = Terms.get(iTerm).Term;
                    
                    BooleanQuery q = new BooleanQuery();
                    Query dtQuery = new TermRangeQuery("ctimestamp", 
                                    ini + "0000000", 
                                    ini + "9999999", 
                                    true, 
                                    true);

                    q.add(dtQuery, BooleanClause.Occur.MUST);
                    
                    QueryParser tp = new QueryParser(Version.LUCENE_30, "text", new StandardAnalyzer(Version.LUCENE_20));
                    Query tq = tp.parse(sTerm);
                    q.add(tq, BooleanClause.Occur.MUST);

                    for (int iFilter=0;iFilter<oFilters.size();iFilter++) {
                        Lucene2TopicsFilters oFilter = oFilters.get(iFilter);

                        if (oFilter.Type.equalsIgnoreCase("TermRangeQuery")) {
                        } else if (oFilter.Type.equalsIgnoreCase("TermQuery")) {
                            Query tQuery = new TermQuery(new Term(oFilter.Field, oFilter.Value));
                            q.add(tQuery, oFilter.Occur);
                        } else {
                            QueryParser qp = new QueryParser(Version.LUCENE_30, oFilter.Field, new StandardAnalyzer(Version.LUCENE_20));
                            Query textQuery = qp.parse(oFilter.Value);
                            q.add(textQuery, oFilter.Occur);
                        }
                    }

                    IndexReader ir = IndexMngr.GetReader(path2Index);
                    IndexSearcher is = new IndexSearcher(ir);
                    is.setDefaultFieldSortScoring(true, false);
                    int iNDocs = is.search(q, 1).totalHits;
                    iCount+=iNDocs;
                }
                
                int iFreq = 0;
                if (oTopicsFreq.containsKey(sTopic)) {
                    iFreq = oTopicsFreq.get(sTopic);
                }
                iFreq += iCount;
                oTopicsFreq.put(sTopic, iFreq);
                oTopicsPerDay.put(ini, oTopicsFreq);
            }
            
            ini++;
        }
        
        return oTopicsPerDay;
    }
}
