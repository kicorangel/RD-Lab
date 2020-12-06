/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ScriptListening.Lucene2GEO;

import ScriptListening.CommonTools.Tools;
import ScriptListening.Index.IndexMngr;
import ScriptListening.Lucene2GEXF.Lucene2GEXFFilters;
import ScriptListening.Lucene2GEXF.Lucene2GEXFMngr;
import ScriptListening.Lucene2Geo.Lucene2GeoFilters;
import ScriptListening.Lucene2Geo.Lucene2GeoMngr;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.json.JSONObject;

/**
 *
 * @author @kicorangel
 */
public class Lucene2Geo {

    public static void main(String[] args) throws IOException, ParseException {
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        String sProyecto = args[0];
        
//        LoadFromJSON(sProyecto);
        LoadFromIndex(sProyecto);
    }
    
    private static void LoadFromIndex(String proyecto) throws IOException, ParseException {
        String sINDEXPath = Lucene2GeoMngr.GetINDEXPath(proyecto);
        String sMAPPath = Lucene2GeoMngr.GetMAPPath(proyecto);
        String sTemplate = Lucene2GeoMngr.GetTEMPLATE(proyecto);
        String sMultiplicador = Lucene2GeoMngr.GetMULTIPLICADOR(proyecto);
        
        ArrayList<Lucene2GeoFilters> oFilters = Lucene2GeoMngr.GetFilters(proyecto);
        
        String sNombreProyecto = Tools.GetNOMBRE(proyecto);
        int iTotal = 0;
        int iGeoPos = 0;
        
        StringBuilder sbMapData = new StringBuilder();
        
        FileWriter fw = new FileWriter(sMAPPath);
        
        
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
                Lucene2GeoFilters oFilter = oFilters.get(iFilter);
                
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
                    
                    if (doc.getValues("longitude").length>0 && doc.getValues("latitude").length>0) {
                        String sLongitude = doc.getValues("longitude")[0];
                        String sLatitude = doc.getValues("latitude")[0];
                        if (!sLongitude.isEmpty() && !sLatitude.isEmpty()) {
                            if (sbMapData.length()>0) {
                                sbMapData.append(",");
                            }
                            sbMapData.append("{lat: ")
                                    .append(sLatitude)
                                    .append(", lon: ")
                                    .append(sLongitude)
                                    .append(", count: ")
                                    .append(sMultiplicador)
                                    .append("}");
                            iGeoPos++;
                        }
                    }
                    iTotal++;
                }
            }
        }
        
        
        fw.close();
        
        FileReader fr = new FileReader(sTemplate);
        BufferedReader bf = new BufferedReader(fr);
        StringBuilder sbTemplateData = new StringBuilder();
        String sLine = "";
        while ((sLine = bf.readLine())!=null) {
            sbTemplateData.append(sLine + "\n");
        }
        String sTemplateData = sbTemplateData.toString()
                .replace("##MAPDATA##", sbMapData.toString())
                .replaceAll("##PROYECTO##", sNombreProyecto)
                .replaceAll("##TOTAL##", iTotal + "")
                .replaceAll("##GEOPOS##", iGeoPos + "")
                .replaceAll("##PORCENTAJE##", String.format("%.2f", (double)((double)100*(double)iGeoPos/(double)iTotal)));
        
        fw = new FileWriter(sMAPPath);
        fw.write(sTemplateData);
        fw.close();
    }
    
    private static void LoadFromJSON(String proyecto) throws IOException {
        String sJSONPath = Lucene2GeoMngr.GetJSONPath(proyecto);
        String sMAPPath = Lucene2GeoMngr.GetMAPPath(proyecto);
        String sTemplate = Lucene2GeoMngr.GetTEMPLATE(proyecto);
        String sMultiplicador = Lucene2GeoMngr.GetMULTIPLICADOR(proyecto);
        
        String sNombreProyecto = Tools.GetNOMBRE(proyecto);
        int iTotal = 0;
        int iGeoPos = 0;
        
        StringBuilder sbMapData = new StringBuilder();
        
        FileWriter fw = new FileWriter(sMAPPath);
        File directory = new File(sJSONPath);
        String [] folders = directory.list();
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
                        JSONObject geo = json.optJSONObject("custom-geo");
                       
                        if (geo!=null) {
                            String latitude = geo.getString("latitude");
                            String longitude = geo.getString("longitude");
                            
                            if (sbMapData.length()>0) {
                                sbMapData.append(",");
                            }
                            sbMapData.append("{lat: " + latitude + ", lon: " + longitude + ", count: " + sMultiplicador +"}");
                            iGeoPos++;
                        }
                        iTotal++;
                        
                    } catch (Exception ex) {
                        
                    }
                }
            }
        }
        
        fw.close();
        
        FileReader fr = new FileReader(sTemplate);
        BufferedReader bf = new BufferedReader(fr);
        StringBuilder sbTemplateData = new StringBuilder();
        String sLine = "";
        while ((sLine = bf.readLine())!=null) {
            sbTemplateData.append(sLine + "\n");
        }
        String sTemplateData = sbTemplateData.toString()
                .replace("##MAPDATA##", sbMapData.toString())
                .replaceAll("##PROYECTO##", sNombreProyecto)
                .replaceAll("##TOTAL##", iTotal + "")
                .replaceAll("##GEOPOS##", iGeoPos + "")
                .replaceAll("##PORCENTAJE##", String.format("%.2f", (double)((double)100*(double)iGeoPos/(double)iTotal)));
        
        fw = new FileWriter(sMAPPath);
        fw.write(sTemplateData);
        fw.close();
    }
    
    private static int IndexOf(String content, int ini)
    {
        int iPos = ini;
        if (iPos>=0 && iPos<content.length())
        {
            for (iPos=ini;iPos<content.length();iPos++)
            {
                if (!Character.isDigit(content.charAt(iPos)) &&
                        !Character.isLetter(content.charAt(iPos)) &&
                        content.charAt(iPos) != '_')
                    break;
            }
        }
        return iPos;
    }
    
    private static void ProcessDocument(Document doc, FileWriter fw) throws IOException {
        
        
        
        
        
    }
}
