/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.Index;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

/**
 *
 * @author @kicorangel
 */


public class IndexMngr {
    public static IndexReader GetReader(String path2Index) throws IOException {
        Directory directory = null;
        IndexReader reader = null;
        directory = new SimpleFSDirectory(new File(path2Index));
        reader = IndexReader.open(directory, true);
        return reader;        
    }
    
    public static IndexWriter GetWriter(String path, boolean create, boolean forceUnlock, Analyzer analyzer) throws IOException
    {
        Directory directory = new SimpleFSDirectory(new File(path));
        
        IndexWriter writer;
        
        if (create)
            writer = new IndexWriter(directory,  analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
        else
        {
            if (new File(path).exists() && directory.listAll().length>0)
            {
                if (forceUnlock)
                {
                    if (IndexWriter.isLocked(directory))
                    {
                        IndexWriter.unlock(directory);
                    }
                }

                writer = new IndexWriter(directory,  analyzer, false, IndexWriter.MaxFieldLength.UNLIMITED);
            }
            else
                writer = new IndexWriter(directory,  analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
        }
        
        return writer;
    }
}
