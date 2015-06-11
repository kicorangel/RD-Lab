/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author kico
 */
public class Stats {

    private static String PATH = "/data/pan13/pan13-author-profiling-test-corpus2-2013-04-29/es/";
    private static String TRUTH = "/data/pan13/pan13-author-profiling-test-corpus2-2013-04-29/truth-es.txt";
    private static String OUTPUT = "/data/pan13/test-es-distribution.csv";
    
    public static void main(String[] args) {
        FileWriter fw = null;
        
        try {
            Hashtable<String, TruthInfo> oTruth = ReadTruth(TRUTH);

            File directory = new File(PATH);
            File []files = directory.listFiles();

            double nDocs = files.length;
            double nWords = 0;
            double avgWords = 0;
            double stdWords = 0;
            double minWords = Integer.MAX_VALUE;
            double maxWords = 0;

            fw = new FileWriter(OUTPUT);
            fw.write("Author, Gender, Age, Docs, Words\n");
            for (int iFile = 0; iFile < files.length; iFile++)  {
                System.out.println("-->" + (iFile+1) + "/" + files.length);
                
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(files[iFile]);
                    NodeList documents = doc.getDocumentElement().getElementsByTagName("conversation");
                    double iWords = 0;
                    double iDocs = documents.getLength();
                    for (int i=0;i<iDocs;i++) {
                        Element element = (Element)documents.item(i);
                        String sContent = element.getTextContent();

                        iWords += GetNumTerms(sContent);
                    }
                    if (iWords<minWords) {
                        minWords = iWords;
                    }
                    if (iWords>maxWords) {
                        maxWords = iWords;
                    }
                    nWords += iWords;

                    String []aux = files[iFile].toString().replace(PATH, "").split("_");
                    String sAuthor = aux[0];
                    if (oTruth.containsKey(sAuthor)) {
                        TruthInfo oInfo = oTruth.get(sAuthor);
                        fw.write(sAuthor + "," + oInfo.Gender + "," + oInfo.Age + "," + iDocs + "," + iWords + "\n");
                        fw.flush();
                    }

                } catch (Exception ex) {

                }
            }
            avgWords = nWords / nDocs;
            for (int iFile = 0; iFile < files.length; iFile++)  {
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(files[iFile]);
                    NodeList documents = doc.getDocumentElement().getElementsByTagName("document");
                    double iWords = 0;
                    for (int i=0;i<documents.getLength();i++) {
                        Element element = (Element)documents.item(i);
                        String sContent = element.getTextContent();

                        iWords += GetNumTerms(sContent);
                    }
                    stdWords += Math.pow(iWords - avgWords, 2);
                } catch (Exception ex) {

                }
            }
            stdWords = Math.sqrt(stdWords / nDocs);

            System.out.println(String.format("%.0f", nDocs) + ";" + String.format("%.0f", nWords) + ";" + String.format("%.0f", maxWords) + ";" + String.format("%.0f", minWords) + ";" + String.format("%.2f", avgWords) + ";" + String.format("%.2f", stdWords));
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            if (fw!=null) { try { fw.close(); } catch(Exception k) {}}
        }
    }
    
    
    public static int GetNumTerms(String text)
    {
        int iNumTerms = 0;
        
        IndexWriter writer = null;
        IndexReader reader = null;
        try {
            writer = GetRAMWriter(new String[0]);
            writer = IndexData(writer, "text", text);
            reader = writer.getReader();
            iNumTerms = GetNumTerms(reader, 0, "text");
        } catch(Exception ex) {
            
        } finally {
            if (writer!=null) { try { writer.close(); } catch (Exception ex) {}}
        }
        
        return iNumTerms;
    }
    
    public static int GetNumTerms(IndexReader reader, int iDoc, String fieldName)
    {
        int iNumWords = 0;
        try
        {
            TermFreqVector term = reader.getTermFreqVector(iDoc, fieldName);
            int []TermFrecuencies = term.getTermFrequencies();
            for (int iTerm=0;iTerm<term.size();iTerm++) {
                iNumWords += TermFrecuencies[iTerm];
            }
        }
        catch (Exception ex)
        {
            
        }
        return iNumWords;
    }
    
    public static IndexWriter GetRAMWriter(String []stopWords) throws Exception
    {
        Directory ramDirectory;
        IndexWriter ramWriter;
        
        try
        {
            ramDirectory = new RAMDirectory();
            ramWriter = new IndexWriter(ramDirectory, new CustomAnalyser(stopWords), true, IndexWriter.MaxFieldLength.UNLIMITED);
        }
        catch (Exception ex)
        {
            throw ex;
        }
        
        return ramWriter;
    }
    
    public static IndexWriter IndexData(IndexWriter writer, String fieldName, String fieldValue) throws IOException
    {
        return IndexData(writer, fieldName, fieldValue, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES);
    }
    
    public static IndexWriter IndexData(IndexWriter writer, String fieldName, String fieldValue, Field.Store store, Field.Index index, Field.TermVector termVector) throws IOException
    {
        org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
        doc.add(new Field(fieldName, fieldValue, store, index, termVector));
        writer.addDocument(doc);       
        writer.commit();
        return writer;
    }
    

    private static Hashtable<String, TruthInfo> ReadTruth(String path) {
        Hashtable<String, TruthInfo> oTruth = new Hashtable<String, TruthInfo>();
        
        FileReader fr = null;
        BufferedReader bf = null;
        
        try {
            fr = new FileReader(path);
            bf = new BufferedReader(fr);
            String sCadena = "";

            while ((sCadena = bf.readLine())!=null)
            {
                String []data = sCadena.split(":::");
                if (data.length==3) {
                    String sAuthorId = data[0];
                    if (!oTruth.containsKey(sAuthorId)) {
                        TruthInfo info = new TruthInfo();
                        info.Gender = data[1];
                        info.Age= data[2];
                        oTruth.put(sAuthorId, info);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            if (bf!=null) { try { bf.close(); } catch (Exception k) {} }
            if (fr!=null) { try { fr.close(); } catch (Exception k) {} }
        }
        
        return oTruth;
    }
}
