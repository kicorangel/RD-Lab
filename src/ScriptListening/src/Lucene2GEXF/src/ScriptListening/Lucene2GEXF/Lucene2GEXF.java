package ScriptListening.Lucene2GEXF;

import ScriptListening.Index.IndexMngr;
import ScriptListening.Lucene2GEXF.Lucene2GEXFFilters;
import ScriptListening.Lucene2GEXF.Lucene2GEXFMngr;
import ScriptListening.comunes.Tools;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.json.JSONObject;

/**
 *
 * @author @kicorangel
 */

public class Lucene2GEXF {

    public static void main(String[] args) throws IOException, ParseException {
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        String sProyecto = args[0];
        
        LoadFromIndex(sProyecto);
//        LoadFromJSON(sProyecto);
        
    }
    
    private static void LoadFromIndex(String proyecto) throws IOException, ParseException {
        String sINDEXPath = Lucene2GEXFMngr.GetINDEXPath(proyecto);
        String sGEXFPath = Lucene2GEXFMngr.GetGEXFPath(proyecto);
        ArrayList<Lucene2GEXFFilters> oFilters = Lucene2GEXFMngr.GetFilters(proyecto);

        FileWriter fw = new FileWriter(sGEXFPath);
        fw.write("source,target,reltype\n");

        
        if (oFilters.size()==0) {   // NO FILTER, ITERATE AMONG THE WHOLE DATASET
            IndexReader reader = IndexMngr.GetReader(sINDEXPath);
            for (int i=0; i<reader.maxDoc(); i++) {
                if (reader.isDeleted(i)) continue;
                Document doc = reader.document(i); 

                ProcessDocument(doc, fw);
                
            }
        } else {    // There are filters
            BooleanQuery q = new BooleanQuery();
            for (int iFilter=0;iFilter<oFilters.size();iFilter++) {
                Lucene2GEXFFilters oFilter = oFilters.get(iFilter);
                
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
            
            IndexReader ir = IndexMngr.GetReader(sINDEXPath);
            IndexSearcher is = new IndexSearcher(ir);
            is.setDefaultFieldSortScoring(true, false);
            int iNDocs = is.search(q, 1).totalHits;
            if (iNDocs>0) {
                TopDocs hits = is.search(q, iNDocs);
                int Total = hits.scoreDocs.length;
                for (int i=0;i<Total;i++) {
                    int docId = hits.scoreDocs[i].doc;
                    Document doc = is.doc(docId);
                    ProcessDocument(doc, fw);
                }
            }
        }
        
        fw.close();
    }
            
    private static void ProcessDocument(Document doc, FileWriter fw) throws IOException {
        String sUser = doc.getValues("screenname")[0].toLowerCase();
        String sTweet = doc.getValues("text")[0];

        if (sTweet.contains("@")) {
            int iIni = sTweet.indexOf("@"); if (iIni>=0) iIni++;
            int iEnd = Tools.IndexOf(sTweet, iIni);

            while (iIni<iEnd && iIni!=-1)
            {
                String sToUser = sTweet.substring(iIni, iEnd);
                if (sToUser.endsWith(":"))
                    sToUser = sToUser.substring(0, sToUser.length()-1);

                String sRelationType = "MENTION";
                if (sTweet.contains("RT @" + sToUser))
                    sRelationType = "RT";

                sToUser = sToUser.toLowerCase();

                System.out.println(sUser + "->" + sToUser);
                fw.write("\"" + sUser + "\",\"" + sToUser + "\",\"" + sRelationType + "\"\n");


                iIni = sTweet.indexOf("@", iEnd); if (iIni>=0) iIni++;
                iEnd = Tools.IndexOf(sTweet, iIni);
            }
        }
    }
    
    private static void LoadFromJSON(String proyecto) throws IOException {
        String sJSONPath = Lucene2GEXFMngr.GetJSONPath(proyecto);
        String sGEXFPath = Lucene2GEXFMngr.GetGEXFPath(proyecto);
        
        FileWriter fw = new FileWriter(sGEXFPath);
        File directory = new File(sJSONPath);
        String [] folders = directory.list();
        int iDoc = 0;
        for (int iFolder = 0; iFolder < folders.length; iFolder++) {
            String sFolderName = folders[iFolder];
            String sFolder = sJSONPath + "/" + sFolderName;
            if (new File(sFolder).isDirectory()) {
                File subdir = new File(sFolder);
                String []files = subdir.list();
                for (int iFile=0;iFile<files.length;iFile++) {
                    String sFileName = sFolder + "/" + files[iFile];
                    System.out.println("Processing " + sFileName);
                    try {
                        byte[] encoded = Files.readAllBytes(Paths.get(sFileName));
                        String sJSON = new String(encoded);

                        JSONObject json = new JSONObject(sJSON);
                        String sTweet = json.optString("text", "");
                        
                        if (sTweet.contains("@"))
                        {
                            String sUser = json.getJSONObject("user").optString("screen_name", "");
                            sUser = sUser.toLowerCase();

                            int iIni = sTweet.indexOf("@"); if (iIni>=0) iIni++;
                            int iEnd = Tools.IndexOf(sTweet, iIni);

                            while (iIni<iEnd && iIni!=-1)
                            {
                                String sToUser = sTweet.substring(iIni, iEnd);
                                if (sToUser.endsWith(":"))
                                    sToUser = sToUser.substring(0, sToUser.length()-1);
                                
                                String sRelationType = "MENTION";
                                if (sTweet.contains("RT @" + sToUser))
                                    sRelationType = "RT";
                                
                                sToUser = sToUser.toLowerCase();

                                fw.write("\"" + sUser + "\",\"" + sToUser + "\",\"" + sRelationType + "\"\n");
                                iDoc++;
                                
                                iIni = sTweet.indexOf("@", iEnd); if (iIni>=0) iIni++;
                                iEnd = Tools.IndexOf(sTweet, iIni);
                            }
                        }
                    } catch (Exception ex) {
                        
                    }
                }
            }
            if (iDoc>=10000) {
                break;
            }
        }
        
        fw.close();
    }
    
    
}
