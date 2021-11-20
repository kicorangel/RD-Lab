/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kicorangel.repr.nGrams.Test;

import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.nGrams.Datasets.Credibility;
import com.kicorangel.repr.nGrams.Datasets.EMO_Angelo_Full;
import com.kicorangel.repr.nGrams.Datasets.EMO_UNIFY;
import com.kicorangel.repr.nGrams.Datasets.ICLE;
import com.kicorangel.repr.nGrams.Datasets.IroSvA19;
import com.kicorangel.repr.nGrams.Datasets.Irony;
import com.kicorangel.repr.nGrams.Datasets.NLI_VERSUS;
import com.kicorangel.repr.nGrams.Datasets.NNN;
import com.kicorangel.repr.nGrams.Datasets.NNN1vsall;
import com.kicorangel.repr.nGrams.Datasets.PAN18_gender;
import com.kicorangel.repr.nGrams.Datasets.PAN19_bots;
import com.kicorangel.repr.nGrams.Datasets.PAN19_gender;
import com.kicorangel.repr.nGrams.Datasets.PAN20_fakers;
import com.kicorangel.repr.nGrams.Datasets.QatarDeception;
import com.kicorangel.repr.nGrams.Datasets.SENT_TASS;
import com.kicorangel.repr.nGrams.Datasets.SevenTruthSevenLies;
import com.kicorangel.repr.nGrams.Datasets.TOEFL;
import com.kicorangel.repr.nGrams.Datasets.op_spam_v14;
import com.kicorangel.repr.nGrams.LangDetector;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author @kicorangel
 */
public class nGrams {

    private static String NLIPATH = "/media/kicorangel/Acer HD/mnt/data/NLI";
    
    public static void main(String[] args) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException, Exception {
        
//        TOEFL_ICLE();
        
//        ICLE_TOEFL();
        
//        VERSUS();
        
//        NNN_Demo();
//        NNN1vsall_Demo();
//        op_spam_v14_Demo();
        
//        PAN18_gender_Demo();
//        CMUQ100_variety_Demo();
        
//        Credibility_Demo();
//        Irony_Demo();
//        QatarDeception_Demo();

    
//        for (String dataset:new String[]{"SevenTruthSevenLies_SEP21", "SevenTruthSevenLies_Unhighlighted", "SevenTruthSevenLies_Yellow", "SevenTruthSevenLies_Orange", "SevenTruthSevenLies_Red"}) {
//            SevenTruthSevenLies_Demo(dataset);
//        }
        
        /* CREDIBILITY */
//        if (args.length<4) {
//            System.out.println("USE: java -jar LDR.jar set type n t");
//            return;
//        }
        
//        Credibility_Demo(args[0], (args[1].equalsIgnoreCase("char")?NGRAMTYPE.CHAR:NGRAMTYPE.WORD), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
        
        /* PAN19 */
        
//        if (args.length<5) {
//            System.out.println("USE: java -jar ngrams.jar [bots|gender] type lang n t");
//            return;
//        }
//        PAN_AP_19_Demo(args[0], (args[1].equalsIgnoreCase("char")?NGRAMTYPE.CHAR:NGRAMTYPE.WORD), args[2], Integer.valueOf(args[3]), Integer.valueOf(args[4]));
        
        
        /* IroSvA19 */
//        if (args.length<4) {
//            System.out.println("USE: java -jar ngrams.jar type variety n t");
//            return;
//        }
//        
//        IroSvA19_Demo((args[0].equalsIgnoreCase("char")?NGRAMTYPE.CHAR:NGRAMTYPE.WORD), args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]));

//        EMO_Angelo_Full_Demo();

//        EMO_Unify_Demo();
//            SENT_TASS_Demo();

//        T_NADI_2021.Test();
//        T_PAN_AP_20.Test();
//           T_PAN_AP_21.Test();
        T_PAN_AP_19.Test();
//        BotTypes.Test();
            
    }

    
        
    private static void SENT_TASS_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int t:new Integer[] {100, 500, 1000}) {
            for (NGRAMTYPE ntype:NGRAMTYPE.values()) {
                for (int n=1;n<5;n++) {
                    System.out.println("-->Processing " + ntype.toString() + ".n-" + n + ".t-" + t);
                    String sTraining = "/mnt/data/TASS/dataset/general-train-tagged-3l.tsv";
                    String sTrainingArff = "/mnt/data/TASS/nGrams/arff/train." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                    String sTest = "/mnt/data/TASS/dataset/general-test-tagged-3l.tsv";
                    String sTestArff = "/mnt/data/TASS/nGrams/arff/test." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                    String sTmp = "/mnt/data/TASS/nGrams/tmp/tmp." + ntype.toString() + ".n-" + n + ".t-" + t + ".txt";

                    new SENT_TASS(n, t, ntype, sTraining, sTmp, sTrainingArff, sTest, sTestArff, 
                            SENT_TASS.GetLabels(), SET.BOTH, 
                            new PreprocessingOptions(true, false, false, false, 0, new String[0])).Run();
                }
            }
        }
    }
    private static void EMO_Unify_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int t:new Integer[] {100, 500, 1000}) {
            for (NGRAMTYPE ntype:NGRAMTYPE.values()) {
                for (int n=1;n<5;n++) {
                    System.out.println("-->Processing " + ntype.toString() + ".n-" + n + ".t-" + t);
                    String sTraining = "/mnt/data/UNIFY/dataset/train.tsv";
                    String sTrainingArff = "/mnt/data/UNIFY/nGrams/arff/train." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                    String sTest = "/mnt/data/UNIFY/dataset/test.tsv";
                    String sTestArff = "/mnt/data/UNIFY/nGrams/arff/test." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                    String sTmp = "/mnt/data/UNIFY/nGrams/tmp/tmp." + ntype.toString() + ".n-" + n + ".t-" + t + ".txt";

                    new EMO_UNIFY(n, t, ntype, sTraining, sTmp, sTrainingArff, sTest, sTestArff, 
                            EMO_UNIFY.GetLabels(), SET.BOTH, 
                            new PreprocessingOptions(true, false, false, false, 0, new String[0])).Run();
                }
            }
        }
    }
    
    private static void EMO_Angelo_Full_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int t:new Integer[] {1000, 100, 500, 2000, 5000}) {
            for (NGRAMTYPE ntype:NGRAMTYPE.values()) {
//            {
//                NGRAMTYPE ntype = NGRAMTYPE.WORD;
    
                for (int n=1;n<11;n++) {
                    System.out.println("-->Processing " + ntype.toString() + ".n-" + n + ".t-" + t);
                    String sTraining = "C:\\Local\\Eval\\EXTERN_ANGELO\\prepared\\train.tsv";
                    String sTrainingArff = "C:\\Local\\Eval\\EXTERN_ANGELO\\repr\\nGrams\\arff\\training." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                    String sTest = "C:\\Local\\Eval\\EXTERN_ANGELO\\prepared\\test.tsv";
                    String sTestArff = "C:\\Local\\Eval\\EXTERN_ANGELO\\repr\\nGrams\\arff\\test." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + ".arff";
                    String sTmp = "C:\\Local\\Eval\\EXTERN_ANGELO\\repr\\nGrams\\tmp\\tmp." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + ".txt";

                    new EMO_Angelo_Full(n, t, ntype, sTraining, sTmp, sTrainingArff, sTest, sTestArff, 
                            EMO_Angelo_Full.GetLabels(), SET.BOTH, 
                            new PreprocessingOptions(true, false, false, false, 0, new String[0])).Run();
                }
            }
        }
    }
    
    private static void Irony_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int t:new Integer[] {1000, 100, 500, 2000, 5000, 10000}) {
            for (NGRAMTYPE ntype:NGRAMTYPE.values()) {
                for (int n=1;n<11;n++) {
                    System.out.println("-->Processing " + ntype.toString() + ".n-" + n + ".t-" + t);
                    String sTraining = "/mnt/data/Irony/data/Irony - Train.csv";
                    String sTrainingArff = "/mnt/data/Irony/repr/nGrams/arff/training." + ntype.toString() + ".n-" + n + ".t-" + t + ".arff";
                    String sTest = "/mnt/data/Irony/data/Irony - Test.csv";
                    String sTestArff = "/mnt/data/Irony/repr/nGrams/arff/test." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + ".arff";
                    String sTmp = "/mnt/data/Irony/repr/nGrams/tmp/tmp." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + ".txt";

                    new Irony(n, t, ntype, sTraining, sTmp, sTrainingArff, sTest, sTestArff, 
                            Irony.GetLabels(), SET.BOTH, 
                            new PreprocessingOptions(false, false, false, false, 0, new String[0])).Run();
                }
            }
        }
    }
    
    private static void QatarDeception_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int t:new Integer[] {1000, 100, 500, 2000, 5000, 10000}) {
            for (NGRAMTYPE ntype:NGRAMTYPE.values()) {
                for (int n=1;n<11;n++) {
                    for (int fold=0;fold<5;fold++) {
                        System.out.println("-->Processing " + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + fold);
                        String sTraining = "/mnt/data/Qatar-Deceptive/data/mix/training-" + fold;
                        String sTrainingArff = "/mnt/data/Qatar-Deceptive/repr/nGrams/arff/training." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + fold + ".arff";
                        String sTest = "/mnt/data/Qatar-Deceptive/data/mix/test-" + fold;
                        String sTestArff = "/mnt/data/Qatar-Deceptive/repr/nGrams/arff/test." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + fold + ".arff";
                        String sTmp = "/mnt/data/Qatar-Deceptive/repr/nGrams/tmp/tmp." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + fold + ".txt";

                        new QatarDeception(n, t, ntype, sTraining, sTmp, sTrainingArff, sTest, sTestArff, 
                                QatarDeception.GetLabels(), SET.BOTH, 
                                new PreprocessingOptions(false, false, false, false, 0, new String[0])).Run();
                    }
                }
            }
        }
    }
    
    private static void SevenTruthSevenLies_Demo(String ds) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int t:new Integer[] {1000, 500 /*100, 500, 2000, 5000, 10000*/}) {
            for (NGRAMTYPE ntype:NGRAMTYPE.values()) {
                for (int n=1;n<4;n++) {
                    for (int fold=0;fold<10;fold++) {
                        System.out.println("-->Processing " + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + fold + " on " + ds);
                        String sTraining = "/mnt/data/SevenTruthSevenLies/data/" + ds + "/mix/training-" + fold;
                        String sTrainingArff = "/mnt/data/SevenTruthSevenLies/repr/nGrams/" + ds + "/arff/training." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + fold + ".arff";
                        String sTest = "/mnt/data/SevenTruthSevenLies/data/" + ds + "/mix/test-" + fold;
                        String sTestArff = "/mnt/data/SevenTruthSevenLies/repr/nGrams/" + ds + "/arff/test." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + fold + ".arff";
                        String sTmp = "/mnt/data/SevenTruthSevenLies/repr/nGrams/" + ds + "/tmp/tmp." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + fold + ".txt";

                        new SevenTruthSevenLies(n, t, ntype, sTraining, sTmp, sTrainingArff, sTest, sTestArff, 
                                SevenTruthSevenLies.GetLabels(), SET.BOTH, 
                                new PreprocessingOptions(false, false, false, false, 0, new String[0])).Run();
                    }
                }
            }
        }
    }
    
    
    private static void Credibility_Demo(String set, NGRAMTYPE ntype, int n, int t) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/Credibility/";
        String sReprDir = sBaseDir + "/repr/nGrams/";
        String sArffDir = sReprDir + "/arff/";
        String sTmpDir = sReprDir + "/tmp/";
        
        String sTrainingCorpus = sBaseDir;
        String sTestCorpus = sBaseDir;
        
        if (set.equalsIgnoreCase("gov")) {
            sTrainingCorpus += "/data/SyrianGov/mix/training-%fold%";
            sTestCorpus += "/data/SyrianGov/mix/test-%fold%";
        } else if (set.equalsIgnoreCase("rev")) {
            sTrainingCorpus += "/data/SyrianRev/mix/training-%fold%";
            sTestCorpus += "/data/SyrianRev/mix/test-%fold%";
        } else {
            sTrainingCorpus += "/data/SyrianCred/training-%fold%";
            sTestCorpus += "/data/SyrianCred/test-%fold%";
        }
        
        for (int fold=0;fold<10;fold++) {
            System.out.println("-->Processing " + set + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + fold);
            
            String sTrainingArff = sArffDir + "/Credibility.training." + set + "." + ntype.toString() + ".n" + n + ".t" + t + ".f" + fold + ".arff";
            String sTestArff = sArffDir + "/Credibility.test." + set + "." + ntype.toString() + ".n" + n + ".t" + t + ".f" + fold + ".arff";

            String sTmp = sTmpDir + "/Credibility.tmp." + set + "." + ntype.toString() + ".n-" + n + ".t-" + t + ".f-" + fold + ".txt";

            new Credibility(n, t, ntype, sTrainingCorpus.replace("%fold%", String.valueOf(fold)), sTmp, sTrainingArff, 
                    sTestCorpus.replace("%fold%", String.valueOf(fold)), sTestArff, 
                    Credibility.GetLabels(), SET.BOTH, 
                    new PreprocessingOptions(false, false, false, false, 0, new String[0])).Run();
        }
    }
    
    private static void op_spam_v14_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int n=3;n<5;n++) {
            for (int l=0;l<5;l++) {
                new op_spam_v14(n, 1000, l, NGRAMTYPE.CHAR, 
                        "/home/kicorangel/Nextcloud/Personales/Kico Rangel/Working/I+D/ProFake/src/Deception/op_spam_v14/alltogether",
                        "/home/kicorangel/Nextcloud/Personales/Kico Rangel/Working/I+D/ProFake/src/Deception/data/Kico/tmp/op_spam_v14.n-grams.WORD.n-" + n + ".s-1000.l-" + l + ".txt",
                        "/home/kicorangel/Nextcloud/Personales/Kico Rangel/Working/I+D/ProFake/src/Deception/data/Kico/op_spam_v14.n-grams.WORD.n-" + n + ".s-1000.l-" + l + ".arff",
                        "",
                        "",                        
                        op_spam_v14.GetLabels(),
                        SET.TRAINING,
                        new PreprocessingOptions(true, true, true, true, l, new String[0])
                ).Run();
            }
        }
    }
    
    private static void NNN1vsall_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (String lang:new String[]{/*"urdu", "french", "english", "somali",*/ "chinese"}) {
            for (int n=1;n<11;n++) {
                for (int total:new Integer[]{/*100, 500,*/ 1000/*, 2000, 5000, 10000*/}) {
                    System.out.println(lang + " " + total + " " + n + "-grams");
                    for (int fold=0;fold<5;fold++) {
                        new NNN1vsall(n, total, NGRAMTYPE.WORD, 
                                "/mnt/data/Native-Non-Native Corpus/5folds/mix/" + fold + "/training",
                                "/mnt/data/Native-Non-Native Corpus/5folds/nGrams-1vsall/tmp/tmp.training." + lang + ".word-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".txt",
                                "/mnt/data/Native-Non-Native Corpus/5folds/nGrams-1vsall/NNN.training." + lang + ".word-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".arff",
                                "/mnt/data/Native-Non-Native Corpus/5folds/mix/" + fold + "/test",
                                "/mnt/data/Native-Non-Native Corpus/5folds/nGrams-1vsall/NNN.test." + lang + ".word-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".arff",
                                NNN1vsall.GetLabels(lang), lang, SET.BOTH, 
                                new PreprocessingOptions(false, false, false, false, 0, new String[0])).Run();

                        new NNN1vsall(n, total, NGRAMTYPE.CHAR, 
                                "/mnt/data/Native-Non-Native Corpus/5folds/mix/" + fold + "/training",
                                "/mnt/data/Native-Non-Native Corpus/5folds/nGrams-1vsall/tmp/tmp.training." + lang + ".char-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".txt",
                                "/mnt/data/Native-Non-Native Corpus/5folds/nGrams-1vsall/NNN.training." + lang + ".char-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".arff",
                                "/mnt/data/Native-Non-Native Corpus/5folds/mix/" + fold + "/test",
                                "/mnt/data/Native-Non-Native Corpus/5folds/nGrams-1vsall/NNN.test." + lang + ".char-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".arff",
                                NNN1vsall.GetLabels(lang), lang, SET.BOTH, 
                                new PreprocessingOptions(false, false, false, false, 0, new String[0])).Run();

                    }
                }
            }
        }
    }
    
    
    private static void CMUQ100_variety_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        int n = 1;
        int total = 5000;
        String BASE = "/mnt/data/PAN/datasets/pan18/";
        for (String lang:new String[]{"ar","en","es"}) {
            new PAN18_gender(n, total, NGRAMTYPE.WORD,
                    BASE + "pan18-author-profiling-training-2018-02-27/" + lang,
                    "/mnt/data/PAN/ngrams/tmp/" + lang + ".txt",
                    "/mnt/data/PAN/ngrams/training." + lang + ".word-ngram.n-1.t-5000.arff",
                    BASE + "pan18-author-profiling-test-2018-03-20/" + lang,
                    "/mnt/data/PAN/ngrams/test." + lang + ".word-ngram.n-1.t-5000.arff",
                    PAN18_gender.GetLabels(), SET.BOTH, new PreprocessingOptions(true, false, true, false, 1, new String[0])
            ).Run();
        }
    }

    private static void IroSvA19_Demo(NGRAMTYPE type, String variety, int n, int t) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sCorpusPath = "/mnt/data/IberEval/2019/IroSvA/dataset/IberLEF19-IroSvA-training-20190331/irosva." + variety +".training.csv";
        String sTmpPath = "/mnt/data/IberEval/2019/IroSvA/repr/ngrams/tmp/" + variety + "." + type.toString() + ".n" + n + ".t" + t + ".txt";
        String sTrainingArff = "/mnt/data/IberEval/2019/IroSvA/repr/ngrams/arff/training." + variety + "." + type.toString() + ".n" + n + ".t" + t + ".arff";
        String sTestPath = "/mnt/data/IberEval/2019/IroSvA/dataset/IberLEF19-IroSvA-test-20190420/irosva." + variety + ".test.csv";
        String sTestArff = "/mnt/data/IberEval/2019/IroSvA/repr/ngrams/arff/test." + variety + "." + type.toString() + ".n" + n + ".t" + t + ".arff";
        
        new IroSvA19(n, t, type, sCorpusPath, sTmpPath, sTrainingArff, sTestPath, sTestArff, IroSvA19.GetLabels(), SET.BOTH, new PreprocessingOptions(true, false, true, false, 1, new String[0]))
                .Run();
    }
    
    private static void PAN_AP_19_Demo(String task, NGRAMTYPE type, String lang, int n, int t) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sCorpusPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-training-2019-02-18/" + lang + "/";
        String sTmpPath = "/mnt/data/PAN/ngrams/pan19/tmp/" + task + "." + lang + "." + type.toString() + ".n" + n + ".t" + t + ".txt";
        String sTrainingArff = "/mnt/data/PAN/ngrams/pan19/arff/training." + task + "." + lang + "." + type.toString() + ".n" + n + ".t" + t + ".arff";
        String sTestPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-test-2019-04-29/" + lang + "/";
        String sTestArff = "/mnt/data/PAN/ngrams/pan19/arff/test." + task + "." + lang + "." + type.toString() + ".n" + n + ".t" + t + ".arff";
        
        if (task.equalsIgnoreCase("bots")) {
            new PAN19_bots(n, t, type, sCorpusPath, sTmpPath, sTrainingArff, sTestPath, sTestArff, PAN19_bots.GetLabels(), SET.BOTH, new PreprocessingOptions(true, false, true, false, 1, new String[0]))
                    .Run();
        } else {
            new PAN19_gender(n, t, type, sCorpusPath, sTmpPath, sTrainingArff, sTestPath, sTestArff, PAN19_gender.GetLabels(), SET.BOTH, new PreprocessingOptions(true, false, true, false, 1, new String[0]))
                    .Run();
        }
    }
    
    private static void PAN18_gender_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        int n = 1;
        int total = 5000;
        String BASE = "/mnt/data/PAN/datasets/pan18/";
        for (String lang:new String[]{"ar","en","es"}) {
            new PAN18_gender(n, total, NGRAMTYPE.WORD,
                    BASE + "pan18-author-profiling-training-2018-02-27/" + lang,
                    "/mnt/data/PAN/ngrams/tmp/" + lang + ".txt",
                    "/mnt/data/PAN/ngrams/training." + lang + ".word-ngram.n-1.t-5000.arff",
                    BASE + "pan18-author-profiling-test-2018-03-20/" + lang,
                    "/mnt/data/PAN/ngrams/test." + lang + ".word-ngram.n-1.t-5000.arff",
                    PAN18_gender.GetLabels(), SET.BOTH, new PreprocessingOptions(true, false, true, false, 1, new String[0])
            ).Run();
        }
    }
    
    private static void NNN_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int n=9;n<11;n++) {
            for (int total:new Integer[]{1000, 500, 100, 2000, 5000, 10000}) {
                for (int fold=0;fold<5;fold++) {
                    
                    System.out.println("Processing " + total + " " + n + "-grams for fold " + fold);
                    new NNN(n, total, NGRAMTYPE.WORD, 
                            "/mnt/data/Native-Non-Native Corpus/5folds/mix/" + fold + "/training",
                            "/mnt/data/Native-Non-Native Corpus/5folds/nGrams/tmp/tmp.training.word-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".txt",
                            "/mnt/data/Native-Non-Native Corpus/5folds/nGrams/NNN.training.word-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".arff",
                            "/mnt/data/Native-Non-Native Corpus/5folds/mix/" + fold + "/test",
                            "/mnt/data/Native-Non-Native Corpus/5folds/nGrams/NNN.test.word-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".arff",
                            NNN.GetLabels(), SET.BOTH, new PreprocessingOptions(false, false, false, false, 0, new String[0])).Run();
                    
                    new NNN(n, total, NGRAMTYPE.CHAR, 
                            "/mnt/data/Native-Non-Native Corpus/5folds/mix/" + fold + "/training",
                            "/mnt/data/Native-Non-Native Corpus/5folds/nGrams/tmp/tmp.training.char-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".txt",
                            "/mnt/data/Native-Non-Native Corpus/5folds/nGrams/NNN.training.char-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".arff",
                            "/mnt/data/Native-Non-Native Corpus/5folds/mix/" + fold + "/test",
                            "/mnt/data/Native-Non-Native Corpus/5folds/nGrams/NNN.test.char-ngram.n-" + n + ".t-" + total + ".f-" + fold + ".arff",
                            NNN.GetLabels(), SET.BOTH, new PreprocessingOptions(false, false, false, false, 0, new String[0])).Run();
                }
            }
        }
        
    }
    
    
    private static ArrayList<String> ICLE_TOEFL_GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
        oLabels.add("chinese"); 
        oLabels.add("french"); 
        oLabels.add("german"); 
        oLabels.add("italian");
        oLabels.add("japanese");
        oLabels.add("spanish"); 
        oLabels.add("turkish"); 
        
        return oLabels;
    }
    private static void ICLE_TOEFL() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
        for (int total: new int[]{100, 500, 1000, 5000, 10000}) {
            for (int n=1;n<=10;n++) {
                System.out.println("\n\nPROCESSING " + total + " " + n + "-grams");
                String sTrainingCorpus = NLIPATH + "/NLI datasets/ICLE/icle-clean-ascii";
                String sTestCorpus = NLIPATH + "/NLI datasets/TOEFL/NLI_2013_Training_Data";
                String sTmp = NLIPATH + "/nGrams/char/tf.icle-toefl." + n + "." + total + ".txt";
                String sTrainingArff = NLIPATH + "/nGrams/char/training.icle-toefl." + n + "." + total + ".arff";
                String sTestArff = NLIPATH + "/nGrams/char/test.icle-toefl." + n + "." + total + ".arff";
                PreprocessingOptions prepOpt = new PreprocessingOptions(false, false, false, false, 0, null);

                ArrayList<String> labels = ICLE_TOEFL_GetLabels();

                new ICLE(n, total, NGRAMTYPE.CHAR, sTrainingCorpus, sTmp, sTrainingArff, null, null, labels, SET.TRAINING, prepOpt).Run();
                new TOEFL(n, total, NGRAMTYPE.CHAR, sTrainingCorpus, sTmp, null, sTestCorpus, sTestArff, labels, SET.TEST, prepOpt).Run();
            }
        }
        
        for (int total: new int[]{100, 500, 1000, 5000, 10000}) {
            for (int n=1;n<=10;n++) {
                System.out.println("\n\nPROCESSING " + total + " " + n + "-grams");
                String sTrainingCorpus = NLIPATH + "/NLI datasets/ICLE/icle-clean-ascii";
                String sTestCorpus = NLIPATH + "/NLI datasets/TOEFL/NLI_2013_Training_Data";
                String sTmp = NLIPATH + "/nGrams/word/tf.icle-toefl." + n + "." + total + ".txt";
                String sTrainingArff = NLIPATH + "/nGrams/word/training.icle-toefl." + n + "." + total + ".arff";
                String sTestArff = NLIPATH + "/nGrams/word/test.icle-toefl." + n + "." + total + ".arff";
                PreprocessingOptions prepOpt = new PreprocessingOptions(false, false, false, false, 0, null);

                ArrayList<String> labels = ICLE_TOEFL_GetLabels();

                new ICLE(n, total, NGRAMTYPE.WORD, sTrainingCorpus, sTmp, sTrainingArff, null, null, labels, SET.TRAINING, prepOpt).Run();
                new TOEFL(n, total, NGRAMTYPE.WORD, sTrainingCorpus, sTmp, null, sTestCorpus, sTestArff, labels, SET.TEST, prepOpt).Run();
            }
        }
    }
    
    private static ArrayList<String> TOEFL_ICLE_GetLabels() {
        ArrayList<String> oLabels = new ArrayList<String>();
        
        oLabels.add("chinese"); 
        oLabels.add("french"); 
        oLabels.add("german"); 
        oLabels.add("italian");
        oLabels.add("japanese");
        oLabels.add("spanish"); 
        oLabels.add("turkish"); 
        
        return oLabels;
    }
    
    private static void VERSUS() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException, Exception {
        
        LangDetector oLangDetect = new LangDetector("profiles");
        
        for (int total: new int[]{/*100, 500,*/ 1000/*, 5000, 10000*/}) {
            for (int n=3;n<=5;n++) {
                for (String lang:new String[]{/*"ar", "ch",*/ "id"/*, "ch-sin"*/}) {
                    for (String evalDataset:new String[]{"ICLE", "TOEFL", "lang8", "ICNALE", "SWTP"}) {
                        for (NGRAMTYPE type:NGRAMTYPE.values()) {
                            ArrayList<String> labels = new ArrayList<String>();
                            labels.add(lang.toUpperCase()); labels.add("OT");

                            String sCorpus = NLIPATH + "/NLI datasets/VERSUS/" + lang;
                            String sTmp = NLIPATH + "/NLI datasets/VERSUS/tmp";
                            String sArff = NLIPATH + "/NLI datasets/VERSUS/" + lang;
                            PreprocessingOptions preOpt = new PreprocessingOptions(false, false, false, false, 0, null);

                            System.out.println(type.toString() + "." + total + "." + n + "." + lang + "." + evalDataset + "...");
                            new NLI_VERSUS(n, total, type, sCorpus, sTmp, sArff, evalDataset, lang, labels, preOpt, oLangDetect).Run();
                        }
                    }
                }
            }
        }
    }
    
    
    private static void TOEFL_ICLE() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        for (int total: new int[]{100, 500, 1000, 5000, 10000}) {
            for (int n=1;n<=10;n++) {
                System.out.println("\n\nPROCESSING " + total + " " + n + "-grams");
                String sTrainingCorpus = NLIPATH + "/NLI datasets/TOEFL/NLI_2013_Training_Data";
                String sTmp = NLIPATH + "/nGrams/char/tf.toefl-icle." + n + "." + total + ".txt";
                String sTrainingArff = NLIPATH + "/nGrams/char/training.toefl-icle." + n + "." + total + ".arff";
                String sTestCorpus = NLIPATH + "/NLI datasets/ICLE/icle-clean-ascii";
                String sTestArff = NLIPATH + "/nGrams/char/test.toefl-icle." + n + "." + total + ".arff";
                PreprocessingOptions prepOpt = new PreprocessingOptions(false, false, false, false, 0, null);

                ArrayList<String> labels = TOEFL_ICLE_GetLabels();

                new TOEFL(n, total, NGRAMTYPE.CHAR, sTrainingCorpus, sTmp, sTrainingArff, null, null, labels, SET.TRAINING, prepOpt).Run();
                new ICLE(n, total, NGRAMTYPE.CHAR, sTrainingCorpus, sTmp, null, sTestCorpus, sTestArff, labels, SET.TEST, prepOpt).Run();
            }
        }
        
        for (int total: new int[]{100, 500, 1000, 5000, 10000}) {
            for (int n=1;n<=10;n++) {
                System.out.println("\n\nPROCESSING " + total + " " + n + "-grams");
                String sTrainingCorpus = NLIPATH + "/NLI datasets/TOEFL/NLI_2013_Training_Data";
                String sTmp = NLIPATH + "/nGrams/word/tf.toefl-icle." + n + "." + total + ".txt";
                String sTrainingArff = NLIPATH + "/nGrams/word/training.toefl-icle." + n + "." + total + ".arff";
                String sTestCorpus = NLIPATH + "/NLI datasets/ICLE/icle-clean-ascii";
                String sTestArff = NLIPATH + "/nGrams/word/test.toefl-icle." + n + "." + total + ".arff";
                PreprocessingOptions prepOpt = new PreprocessingOptions(false, false, false, false, 0, null);

                ArrayList<String> labels = TOEFL_ICLE_GetLabels();

                new TOEFL(n, total, NGRAMTYPE.WORD, sTrainingCorpus, sTmp, sTrainingArff, null, null, labels, SET.TRAINING, prepOpt).Run();
                new ICLE(n, total, NGRAMTYPE.WORD, sTrainingCorpus, sTmp, null, sTestCorpus, sTestArff, labels, SET.TEST, prepOpt).Run();
            }
        }
    }
    
}
