/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.Tagger;

import ScriptListening.Index.IndexMngr;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
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

public class Tagger {
    private static GenderPredictor mGenderPredictor;
    private static AgePredictor mAgePredictor;
    private static LVIPredictor mLVIPredictor;
    private static SentimentPredictor mSentimentPredictor;
    private static EmotionPredictor mEmotionPredictor;
    private static PersonalityPredictor mPersonalityOPredictor;
    private static PersonalityPredictor mPersonalityCPredictor;
    private static PersonalityPredictor mPersonalityEPredictor;
    private static PersonalityPredictor mPersonalityAPredictor;
    private static PersonalityPredictor mPersonalityNPredictor;
    
    public static void main(String[] args) throws IOException, ParseException, LangDetectException, Exception {
        /* Testing zone */
//        PersonalityPredictor oPredictor = new PersonalityPredictor("C");
//        Prediction oPrediction = oPredictor.Predict("Estoy contento y alegre por este éxito");
//        System.out.println(oPrediction.sPredictedClass + " " + oPrediction.Confidence);
        
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        String sProyecto = args[0];
        String sINDEXPath = TaggerMngr.GetIndexPath(sProyecto);
        ArrayList<TaggerFilters> oFilters = TaggerMngr.GetFilters(sProyecto);
        TaggerFields oFields = TaggerMngr.GetFields(sProyecto);
        boolean bForce = TaggerMngr.GetForce(sProyecto);
        
        LoadModels(oFields);
        
        IndexWriter writer = IndexMngr.GetWriter(sINDEXPath, false, false, new StandardAnalyzer(Version.LUCENE_20));
        
        if (oFilters.size()==0) {   // NO FILTER, ITERATE AMONG THE WHOLE DATASET
            IndexReader reader = IndexMngr.GetReader(sINDEXPath);
            for (int i=0; i<reader.maxDoc(); i++) {
                if (reader.isDeleted(i)) continue;
                Document doc = reader.document(i); 
                
                System.out.println("Processing " + (i+1) + " of " + reader.maxDoc());
                ProcessDocument(doc, oFields, writer, bForce);
                
            }
        } else {    // There are filters
            BooleanQuery q = new BooleanQuery();
            for (int iFilter=0;iFilter<oFilters.size();iFilter++) {
                TaggerFilters oFilter = oFilters.get(iFilter);
                
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
                    System.out.println("Processing " + (i+1) + " of " + Total);
                    int docId = hits.scoreDocs[i].doc;
                    Document doc = is.doc(docId);
                    ProcessDocument(doc, oFields, writer, bForce);
                }
            }
        }
       
        writer.optimize();
        writer.close();
    }
    
    private static void LoadModels(TaggerFields oFields) throws LangDetectException, Exception {
        DetectorFactory.loadProfile("./profiles");
        
        if (oFields.Gender) {
            mGenderPredictor = new GenderPredictor();
        }
        if (oFields.Age) {
            mAgePredictor = new AgePredictor();
        }
        if (oFields.Variety) {
            mLVIPredictor = new LVIPredictor();
        }
        if (oFields.Sentiment) {
            mSentimentPredictor = new SentimentPredictor();
        }
        if (oFields.Emotions) {
            mEmotionPredictor = new EmotionPredictor();
        }
        if (oFields.Personality) {
            mPersonalityOPredictor = new PersonalityPredictor("O");
            mPersonalityCPredictor = new PersonalityPredictor("C");
            mPersonalityEPredictor = new PersonalityPredictor("E");
            mPersonalityAPredictor = new PersonalityPredictor("A");
            mPersonalityNPredictor = new PersonalityPredictor("N");
        }
    }
    
    private static void ProcessDocument(Document doc, TaggerFields fields, IndexWriter writer, boolean force) throws IOException {
        if (fields.Lang) {
            ProcessLang(doc, writer, force);
        } 
        if (fields.Gender) {
            ProcessPredictions(doc, writer, "gender", force);
        } 
        if (fields.Age) {
            ProcessPredictions(doc, writer, "age", force);
        }
        if (fields.Variety) {
            ProcessPredictions(doc, writer, "variety", force);
        }
        if (fields.Sentiment) {
            ProcessPredictions(doc, writer, "sentiment", force);
        }
        
        if (fields.Emotions) {
            ProcessPredictions(doc, writer, "emotion", force);
        }
        
        if (fields.Personality) {
            for (String trait:new String[]{"o","c","e","a","n"}) {
                ProcessPredictions(doc, writer, "personality-" + trait, force);
            }
        }
    }
        
    private static void ProcessLang(Document doc, IndexWriter writer, boolean force) throws IOException {
        if (doc.getValues("lang").length==0 || force) {
            String sText = doc.getValues("text")[0];
            String sLang = "";
            try { sLang = DetectLang(sText); } catch (Exception ex) {}
            
            String id = doc.getValues("id")[0];
            if (doc.getValues("lang").length>0) {
                doc.removeField("lang");
            }
            doc.add(new Field("lang", sLang, Store.YES, Index.NOT_ANALYZED_NO_NORMS, TermVector.NO));
            Term indexTerm = new Term("id", id);
            writer.updateDocument(indexTerm, doc);
        } else {
            String s = "Here an already processed doc.";
        }
    }
    
    private static String DetectLang(String text) throws Exception {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.detect();
    }
        
    private static void ProcessPredictions(Document doc, IndexWriter writer, String task, boolean force) throws IOException {
        if (doc.getValues(task).length==0 || force) {
            String sText = doc.getValues("text")[0];
            String sFullName = doc.getValues("name")[0];
            String sLang = doc.getValues("lang")[0];
            if (!sLang.equalsIgnoreCase("es")) {
                return;
            }
            
            String sPersonalityTrait = "";
            if (task.startsWith("personality")) {
                sPersonalityTrait = task.split("-")[1];
                task = "personality";
            }
            
            com.kicorangel.repr.common.Prediction prediction = null;
            
            switch (task) {
                case "gender": prediction = mGenderPredictor.JointPrediction(sFullName, sText); break;
                case "age": prediction = mAgePredictor.Predict(sText); break;
                case "variety": prediction = mLVIPredictor.Predict(sText); break;
                case "sentiment": prediction = mSentimentPredictor.Predict(sText); break;
                case "emotion": prediction = mEmotionPredictor.Predict(sText); break;
                case "personality": {
                    switch(sPersonalityTrait) {
                        case "o": prediction = mPersonalityOPredictor.Predict(sText); task = "personality-o"; break;
                        case "c": prediction = mPersonalityCPredictor.Predict(sText); task = "personality-c"; break;
                        case "e": prediction = mPersonalityEPredictor.Predict(sText); task = "personality-e"; break;
                        case "a": prediction = mPersonalityAPredictor.Predict(sText); task = "personality-a"; break;
                        case "n": prediction = mPersonalityNPredictor.Predict(sText); task = "personality-n"; break;
                    }
                }
            }
            
            if (prediction != null) {
                String id = doc.getValues("id")[0];
                if (doc.getValues(task).length>0) {
                    doc.removeField(task);
                }
                if (doc.getValues(task + "-conf").length>0) {
                    doc.removeField(task + "-conf");
                }
                doc.add(new Field(task, prediction.sPredictedClass, Store.YES, Index.NOT_ANALYZED_NO_NORMS, TermVector.NO));
                doc.add(new Field(task + "-conf", String.format("%.4f", prediction.Confidence), Store.YES, Index.NOT_ANALYZED_NO_NORMS, TermVector.NO));
                Term indexTerm = new Term("id", id);
                writer.updateDocument(indexTerm, doc);
            }
        } else {
            String s = "Here an already processed doc.";
        }
    }
}
