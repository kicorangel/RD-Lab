/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.CommonTools;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author @kicorangel
 */
public class TermMngr {
    public static TermFreqVector GetTermVector(String text)
    {
        return GetTermVector(text, new StandardAnalyzer(Version.LUCENE_30));
    }
    
    public static TermFreqVector GetTermVector(String text, Analyzer analyzer)
    {
        IndexWriter writer = null;
        IndexReader reader = null;
        TermFreqVector tfvector = null;
        
        try
        {
            RAMDirectory ramDirectory = new RAMDirectory();
            writer = new IndexWriter(ramDirectory,  analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
            Document doc = new Document();
            doc.add(new Field("text", text, Store.YES, Index.ANALYZED, TermVector.YES));
            writer.addDocument(doc);
            reader = writer.getReader();
            tfvector = reader.getTermFreqVector(0, "text");  
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
        finally
        {
            if (reader!=null) { try { reader.close(); } catch (Exception xx) {} }
            if (writer!=null) { try { writer.close(); } catch (Exception xx) {} }
        }
        
        return tfvector;
    }
}
