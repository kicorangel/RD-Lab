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
import com.kicorangel.repr.base.Tools;
import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import dataset.Dataset;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import lda.LDA;
import static lda.inference.InferenceMethod.CGS;
import org.apache.commons.lang3.tuple.Pair;
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
 * @LDA: https://github.com/kymmt90/LDA
 */
public class TopicsExtractor {
    private String mField;
    private int mIni;
    private int mEnd;
    private String mVocabularyFile;
    private String mStopWordsFile;
    private String mWorkingFolder;
    private ArrayList<Lucene2TopicsFilters> mFilters;
    private String mINDEXPath;
    private Hashtable<String, Integer> mVocabulary;
    private String[] mStopWords;
    private int mMinLength;
    private int mNumTopics;
    private int mNumTerms;
    
    public TopicsExtractor(Lucene2TopicsInfo oInfo, ArrayList<Lucene2TopicsFilters> oFilters, String sINDEXPath) throws IOException {
        mIni = oInfo.Ini;
        mEnd = oInfo.End;
        mFilters = oFilters;
        mINDEXPath = sINDEXPath;
        mStopWordsFile = oInfo.StopWords;
        mVocabularyFile = oInfo.Vocabulary;
        mNumTopics = oInfo.NTopics;
        mNumTerms = oInfo.NTerms;
        mMinLength = oInfo.MinLength;
        mField = oInfo.Field;
        mWorkingFolder = oInfo.WorkingFolder;
        
        LoadStopWords();
    }
    
    public void GenerateVocabulary() throws ParseException, IOException {
        mVocabulary = new Hashtable<String, Integer>();
        
        int tIni = Integer.valueOf(mIni);
        int tEnd = Integer.valueOf(mEnd);
        
        BooleanQuery q = new BooleanQuery();
            
        Query dtQuery = new TermRangeQuery("ctimestamp", 
                        tIni + "0000000", 
                        tEnd + "9999999", 
                        true, 
                        true);

        q.add(dtQuery, BooleanClause.Occur.MUST);
        
        for (int iFilter=0;iFilter<mFilters.size();iFilter++) {
            Lucene2TopicsFilters oFilter = mFilters.get(iFilter);

            if (oFilter.Type.equalsIgnoreCase("TermRangeQuery")) {
//                    Query dtQuery = new TermRangeQuery(oFilter.Field, 
//                            oFilter.Ini, 
//                            oFilter.End, 
//                            oFilter.IncludeIni, 
//                            oFilter.IncludeEnd);
//
//                    q.add(dtQuery, oFilter.Occur);
            } else if (oFilter.Type.equalsIgnoreCase("TermQuery")) {
                Query tQuery = new TermQuery(new Term(oFilter.Field, oFilter.Value));
                q.add(tQuery, oFilter.Occur);
            } else {
                QueryParser qp = new QueryParser(Version.LUCENE_30, oFilter.Field, new StandardAnalyzer(Version.LUCENE_20));
                Query textQuery = qp.parse(oFilter.Value);
                q.add(textQuery, oFilter.Occur);
            }
        }
        
        IndexReader ir = IndexMngr.GetReader(mINDEXPath);
        IndexSearcher is = new IndexSearcher(ir);
        is.setDefaultFieldSortScoring(true, false);
        int iNDocs = is.search(q, 1).totalHits;
        if (iNDocs>0) {
            TopDocs hits = is.search(q, iNDocs);
            int Total = hits.scoreDocs.length;
            for (int i=0;i<Total;i++) {
                System.out.println("Processing Vocabulary " + (i+1) + " of " + Total);
                int docId = hits.scoreDocs[i].doc;
                Document doc = is.doc(docId);
                String sText = doc.getValues(mField)[0];

                    sText = Tools.Preprocess(sText, new PreprocessingOptions(true, true, true, false, mMinLength, mStopWords));
                    Hashtable<String, Integer> nGrams = Tools.LoadNGrams(sText, 1, mMinLength, NGRAMTYPE.WORD);
                    mVocabulary = MergeVocabulary(mVocabulary, nGrams);
//                }
            }
        }
        
        SaveVocabulary();
    }
    
    public void GenerateDocWords() throws ParseException, IOException {
        ReadVocabulary();
        
        ArrayList<String> oDocWords = new ArrayList<String>();
        int iTotalDocs = 0;
        int iDoc = 1;

        BooleanQuery q = new BooleanQuery();

        Query dtQuery = new TermRangeQuery("ctimestamp", 
                        mIni + "0000000", 
                        mEnd + "9999999", 
                        true, 
                        true);

        q.add(dtQuery, BooleanClause.Occur.MUST);

        for (int iFilter=0;iFilter<mFilters.size();iFilter++) {
            Lucene2TopicsFilters oFilter = mFilters.get(iFilter);

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

        IndexReader ir = IndexMngr.GetReader(mINDEXPath);
        IndexSearcher is = new IndexSearcher(ir);
        is.setDefaultFieldSortScoring(true, false);
        int iNDocs = is.search(q, 1).totalHits;
        if (iNDocs>0) {
            TopDocs hits = is.search(q, iNDocs);
            int Total = hits.scoreDocs.length;
            for (int i=0;i<Total;i++) {
                System.out.println("Processing WordDocs " + (i+1) + " of " + Total);
                if (i==6666) {
                    String stop = "";
                }
                int docId = hits.scoreDocs[i].doc;
                Document doc = is.doc(docId);
                String sId = doc.getValues("id")[0];
                String sText = doc.getValues(mField)[0];
                iTotalDocs++;
                sText = Tools.Preprocess(sText, new PreprocessingOptions(true, true, true, false, mMinLength, mStopWords));
                Hashtable<String, Integer> oDocVocab = Tools.LoadNGrams(sText, 1, mMinLength, NGRAMTYPE.WORD);

                Enumeration keys = mVocabulary.keys();
                while (keys.hasMoreElements()) {
                    String key = (String)keys.nextElement();
                    if (oDocVocab.containsKey(key)) {
                        int iVocabPos = mVocabulary.get(key);
                        int iCount = oDocVocab.get(key);

                        oDocWords.add(iDoc + " " + iVocabPos + " " + iCount + "\n");
                    }
                }
                iDoc++;
            }
        }

        FileWriter oFw = new FileWriter(mWorkingFolder + "/docwords." + mIni + "." + mEnd + ".txt", false);
        oFw.write(iTotalDocs + "\n");
        oFw.write(mVocabulary.size() + "\n");
        oFw.write(oDocWords.size() + "\n");
        for (int i=0;i<oDocWords.size();i++) {
            oFw.write(oDocWords.get(i));
        }
        oFw.close();
    }
    
    public void GenerateDocWordsPerDay() throws ParseException, IOException {
        int tIni = Integer.valueOf(mIni);
        int tEnd = Integer.valueOf(mEnd);
        
        ReadVocabulary();
        
        int ini = tIni;
        while (ini<= tEnd) {
            ArrayList<String> oDocWords = new ArrayList<String>();
            int iTotalDocs = 0;
            int iDoc = 1;
           
            BooleanQuery q = new BooleanQuery();

            Query dtQuery = new TermRangeQuery("ctimestamp", 
                            ini + "0000000", 
                            ini + "9999999", 
                            true, 
                            true);

            q.add(dtQuery, BooleanClause.Occur.MUST);

            for (int iFilter=0;iFilter<mFilters.size();iFilter++) {
                Lucene2TopicsFilters oFilter = mFilters.get(iFilter);

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

            IndexReader ir = IndexMngr.GetReader(mINDEXPath);
            IndexSearcher is = new IndexSearcher(ir);
            is.setDefaultFieldSortScoring(true, false);
            int iNDocs = is.search(q, 1).totalHits;
            if (iNDocs>0) {
                TopDocs hits = is.search(q, iNDocs);
                int Total = hits.scoreDocs.length;
                for (int i=0;i<Total;i++) {
                    System.out.println("Worddocs (" + ini + ") -> Processing " + (i+1) + " of " + Total);
                    if (i==6666) {
                        String stop = "";
                    }
                    int docId = hits.scoreDocs[i].doc;
                    Document doc = is.doc(docId);
    //                    ProcessDocument(doc, oFields, writer, bForce);
                    String sId = doc.getValues("id")[0];
                    String sText = doc.getValues(mField)[0];
//                    String sLang = doc.getValues("lang")[0];
//                    if (sLang.equalsIgnoreCase("es")) {
                        iTotalDocs++;
                        sText = Tools.Preprocess(sText, new PreprocessingOptions(true, true, true, false, mMinLength, mStopWords));
                        Hashtable<String, Integer> oDocVocab = Tools.LoadNGrams(sText, 1, mMinLength, NGRAMTYPE.WORD);
                        
                        Enumeration keys = mVocabulary.keys();
                        while (keys.hasMoreElements()) {
                            String key = (String)keys.nextElement();
                            if (oDocVocab.containsKey(key)) {
                                int iVocabPos = mVocabulary.get(key);
                                int iCount = oDocVocab.get(key);
                                
                                oDocWords.add(iDoc + " " + iVocabPos + " " + iCount + "\n");
                            }
                        }
                        iDoc++;
//                    }
                }
            }
            
            FileWriter oFw = new FileWriter(mWorkingFolder + "/docwords." + ini + ".txt", false);
            oFw.write(iTotalDocs + "\n");
            oFw.write(mVocabulary.size() + "\n");
            oFw.write(oDocWords.size() + "\n");
            for (int i=0;i<oDocWords.size();i++) {
                oFw.write(oDocWords.get(i));
            }
            oFw.close();
            
            
            ini += 1;
        }
    }
    
    public Hashtable<String, ArrayList<TopicTerms>> ExtractTopics() throws Exception {
        Hashtable<String, ArrayList<TopicTerms>> oTopics = new Hashtable<String, ArrayList<TopicTerms>>();
        
        Dataset dataset = new Dataset(mWorkingFolder + "/docwords." + mIni + "." + mEnd + ".txt", mVocabularyFile);

        LDA lda = new LDA(0.1, 0.1, mNumTopics, dataset, CGS);
        lda.run();

        FileWriter oFw = new FileWriter(mWorkingFolder + "/lda." + mIni + "." + mEnd + ".txt");
        double perplexity = lda.computePerplexity(dataset);
        System.out.println("Perplexity: " + String.format("%.4f", perplexity));
        oFw.write("Perplexity: " + String.format("%.4f", perplexity) + "\n");
        for (int t = 0; t < mNumTopics; ++t) {
            List<Pair<String, Double>> highRankVocabs = lda.getVocabsSortedByPhi(t);
            System.out.print("t" + t + ": ");
            oFw.write("t" + t + ": ");
            ArrayList<TopicTerms> oTerms = new ArrayList<TopicTerms>();
            for (int i = 0; i < mNumTerms; ++i) {
                TopicTerms oTT = new TopicTerms();
                oTT.Term = highRankVocabs.get(i).getLeft();
                oTT.Prob = highRankVocabs.get(i).getRight();
                oTerms.add(oTT);
                System.out.print("[" + highRankVocabs.get(i).getLeft() + "," + highRankVocabs.get(i).getRight() + "],");
                oFw.write("[" + highRankVocabs.get(i).getLeft() + "," + highRankVocabs.get(i).getRight() + "],");
            }
            System.out.println();
            oFw.write("\n");
            oTopics.put("t" + t, oTerms);
        }
        oFw.close();
        
        return oTopics;
    }
    
    public void ExtractTopicsPerDay() throws Exception {
        
        int tIni = Integer.valueOf(mIni);
        int tEnd = Integer.valueOf(mEnd);
                
        int ini = tIni;
        while (ini<= tEnd) {
            Dataset dataset = new Dataset(mWorkingFolder + "/docwords." + ini + ".txt", mVocabularyFile);
    //        Dataset dataset = new Dataset("/mnt/data/vodafone-models/lda/docword.kos.txt", "/mnt/data/vodafone-models/lda/vocab.kos.txt");

            LDA lda = new LDA(0.1, 0.1, mNumTopics, dataset, CGS);
            lda.run();
//            System.out.println(lda.computePerplexity(dataset));

            for (int t = 0; t < mNumTopics; ++t) {
                List<Pair<String, Double>> highRankVocabs = lda.getVocabsSortedByPhi(t);
                System.out.print("t" + t + ": ");
                for (int i = 0; i < 5; ++i) {
                    System.out.print("[" + highRankVocabs.get(i).getLeft() + "," + highRankVocabs.get(i).getRight() + "],");
                }
                System.out.println();
            }
            
            ini += 1;
        }
    }
    
    private Hashtable<String, Integer> MergeVocabulary(Hashtable<String, Integer> vocabulary, Hashtable<String, Integer> nGrams) {
        Enumeration keys = nGrams.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            int iVal = nGrams.get(key);
            int iValVoc = 0;
            if (vocabulary.containsKey(key)) {
                iValVoc = vocabulary.get(key);
            }
            vocabulary.put(key, iVal + iValVoc);
        }
        
        return vocabulary;
    }
    
    private void SaveVocabulary() throws IOException {
        FileWriter oFw = new FileWriter(mVocabularyFile, false);
        Enumeration keys = mVocabulary.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            oFw.write(key + "\n");
        }
        oFw.close();
    }
    
    private void ReadVocabulary() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(mVocabularyFile);
        BufferedReader bf = new BufferedReader(fr);
        String sLine = "";
        int iPos = 1;
        while ((sLine = bf.readLine())!=null) {
            mVocabulary.put(sLine, iPos);
            iPos++;
        }
    }
    
    private void LoadStopWords() throws FileNotFoundException, IOException {
        ArrayList<String>StopWords = new ArrayList<String>();
        FileReader fr = new FileReader(mStopWordsFile);
        BufferedReader bf = new BufferedReader(fr);
        String sLine = "";
        int iPos = 1;
        while ((sLine = bf.readLine())!=null) {
            StopWords.add(sLine);
            iPos++;
        }
        
        mStopWords = new String[StopWords.size()];
        for (int i=0;i<StopWords.size();i++) {
            mStopWords[i] = StopWords.get(i);
        }
        
    }
}
