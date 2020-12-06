/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.json2lucene;

import ScriptListening.Index.IndexMngr;
import ScriptListening.JSON2Lucene.JSON2LuceneFields;
import ScriptListening.JSON2Lucene.JSON2LuceneMngr;
import ScriptListening.comunes.Tools;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

/**
 *
 * @author @kicorangel
 */
public class JSON2Lucene {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        String sProyecto = args[0];
        String sJSONPath = JSON2LuceneMngr.GetJSONPath(sProyecto);
        String sIndexPath = JSON2LuceneMngr.GetIndexPath(sProyecto);
        ArrayList<JSON2LuceneFields> oFields = JSON2LuceneMngr.GetFields(sProyecto);
        Double dateMin = JSON2LuceneMngr.GetMinDate(sProyecto);
        Double dateMax = JSON2LuceneMngr.GetMaxDate(sProyecto);
        
        IndexWriter writer = IndexMngr.GetWriter(sIndexPath, false, false, new StandardAnalyzer(Version.LUCENE_20));
        IndexReader reader = writer.getReader();
                
        File directory = new File(sJSONPath);
        String [] folders = directory.list();
        Arrays.sort(folders);
        int iDoc = 0;
        for (int iFolder = 0; iFolder < folders.length; iFolder++) {
            String sFolderName = folders[iFolder];
            try {
                if (Double.valueOf(sFolderName)<dateMin || Double.valueOf(sFolderName)>dateMax) { 
                    continue;
                }
            } catch (Exception ex) {
                continue;
            }
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

                        Document doc = new Document();
                        boolean bAdd = true;
                        for (int iField=0;iField<oFields.size();iField++) {
                            JSON2LuceneFields oField = oFields.get(iField);
                            if (oField.Process) {
                                String sFieldValue = GetFieldValue(oField.Field, json, "");
                                
                                if (oField.IndexField.equalsIgnoreCase("text") && !sFieldValue.isEmpty()) {
                                    doc.removeField("text");
                                }
                                doc.add(new Field(oField.IndexField, sFieldValue, oField.Store, oField.Index, oField.TermVector));
                                
                                if (oField.Convert.equalsIgnoreCase("ctimestamp")) {
                                    doc.add(new Field("ctimestamp", Tools.ToCTIMESTAMP(sFieldValue), oField.Store, oField.Index, oField.TermVector));
                                }
                                
                                if (oField.Unique) {
                                    Term indexTerm = new Term(oField.IndexField, String.valueOf(sFieldValue));
                                    TermDocs docs = reader.termDocs(indexTerm);
                                    if (docs.next()) {
                                        bAdd = false;
                                        break;
                                    }
                                }
                                
                                
                            }
                        }
                        if (bAdd) {
                            writer.addDocument(doc);
                            iDoc++;
                        }
                    } catch (Exception ex) {
                        
                    }
                }
            }
            
//            if (iDoc>=500000) {
//                break;
//            }
        }              
        writer.optimize();
        writer.close();
    }
    
    private static String GetFieldValue(String fieldName, JSONObject json, String fieldValue) throws JSONException {
        try {
            if (!fieldName.contains(":")) {
                return json.optString(fieldName, fieldValue);
            } else {
                String []data = fieldName.split(":");
                JSONObject myJson = json.getJSONObject(data[0]);
                String myFieldName = fieldName.replaceFirst(data[0] + ":", "");
                return GetFieldValue(myFieldName, myJson, fieldValue);
            }
        } catch(Exception ex) {
            return fieldValue;
        }
    }
    
    
    
    
}
