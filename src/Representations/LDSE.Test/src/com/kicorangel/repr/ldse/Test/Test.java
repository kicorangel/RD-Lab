package com.kicorangel.repr.ldse.Test;

import com.kicorangel.repr.ldse.Datasets.Replab14;
import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.ldse.Datasets.CMUQ100_Arap_age;
import com.kicorangel.repr.ldse.Datasets.CMUQ100_Arap_gender;
import com.kicorangel.repr.ldse.Datasets.CMUQ100_Arap_variety;
import com.kicorangel.repr.ldse.Datasets.CMUQ198_4classes_Arap_variety;
import com.kicorangel.repr.ldse.Datasets.CMUQ198_Arap_age;
import com.kicorangel.repr.ldse.Datasets.CMUQ198_Arap_gender;
import com.kicorangel.repr.ldse.Datasets.CMUQ198_Arap_variety;
import com.kicorangel.repr.ldse.Datasets.CMUQ_Arap_gender;
import com.kicorangel.repr.ldse.Datasets.Credibility;
import com.kicorangel.repr.ldse.Datasets.FakeRealNewsMergeQatar;
import com.kicorangel.repr.ldse.Datasets.IroSvA19;
import com.kicorangel.repr.ldse.Datasets.Irony;
import com.kicorangel.repr.ldse.Datasets.MADAR_Subtask1;
import com.kicorangel.repr.ldse.Datasets.NLI_VERSUS;
import com.kicorangel.repr.ldse.Datasets.NNN;
import com.kicorangel.repr.ldse.Datasets.NNN1vsall;
import com.kicorangel.repr.ldse.Datasets.PAN_AP_14_age_CMUQ;
import com.kicorangel.repr.ldse.Datasets.PAN_AP_15_personality_discrete;
import com.kicorangel.repr.ldse.Datasets.PAN_AP_18_gender;
import com.kicorangel.repr.ldse.Datasets.PAN_AP_19_bots;
import com.kicorangel.repr.ldse.Datasets.PAN_AP_19_gender;
import com.kicorangel.repr.ldse.Datasets.PAN_AP_20;
import com.kicorangel.repr.ldse.Datasets.QatarDeception;
import com.kicorangel.repr.ldse.Datasets.TASS_Sentiment;
import com.kicorangel.repr.ldse.Datasets.TOEFL;
import com.kicorangel.repr.ldse.Datasets.UNIFY_Ekman_Emotions;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Francisco Rangel (@kicorangel)
 */
public class Test {

    public static void main(String[] args) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
        /* IroSvA19 */
//        if (args.length<4) {
//            System.out.println("USE: java -jar LDR.jar variety minFreq minSize ldrVersion");
//            return;
//        }
//        IroSvA19_Demo(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
        
        
        /* PAN19 bots vs. human */
        /*
        if (args.length<5) {
            System.out.println("USE: java -jar LDR.jar [bots|gender] lang minFreq minSize ldrVersion");
            return;
        }
//        PAN_AP_19_bots_Demo(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
        PAN_AP_19_Demo(args[0], args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]));
        */
        
//        PAN_AP_15_personality_discrete_Demo(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
        
        for (int minFreq : new int[]{10, 5, 3, 1}) {
            for (int minSize : new int[]{1, 2, 3}) {
                for (int LDRVer : new int[]{1, 2}) {
//                    UNIFY_Ekman_Emotions_Demo(minFreq, minSize, LDRVer);
                        TASS_Sentiment_Demo(minFreq, minSize, LDRVer);
                }
            }
        }
        
//        if (args.length<4) {
//            System.out.println("USE: java -jar LDR.jar lang minFreq minSize ldrVersion");
//            return;
//        } else {
////            PAN_AP_20_Demo(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
////            UNIFY_Ekman_Emotions_Demo(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
//            
//        }

//        for (String lang:new String[]{"en","es"}) {
//            for (int freq:new Integer[]{1, 2, 3, 5, 10}) {
//                for (int size:new Integer[]{1, 2, 3}) {
//                    for (int v:new Integer[]{1,2}) {
//                        PAN_AP_20_Demo(lang, freq, size, v);
//                    }
//                }
//            }
//        }
        
        /* MADAR Subtask 1 */
//        if (args.length<3) {
//            System.out.println("USE: java -jar LDR.jar minFreq minSize ldrVersion");
//            return;
//        }    
//        MADAR_Subtask1_Demo(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
        
        /* CREDIBILITY */
//        Credibility_Demo("full2");
//        Credibility_Demo("gov");
//        Credibility_Demo("rev");
        
//        if (args.length<4) {
//            System.out.println("USE: java -jar LDR.jar set minFreq minSize ldrVersion");
//            return;
//        }
//        Credibility_Demo(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]), Integer.valueOf(args[3]));
        /* End Credibility*/
        
        /* Fake Real News Merge Qatar */
//        if (args.length<3) {
//            System.out.println("USE: java -jar LDR.jar minFreq minSize ldrVersion");
//            return;
//        }
//        FakeRealNewsMergeQatar_Demo(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
        /* End Fake Real News Merge Qatar */
        
        /* Qatar Deception */
//        if (args.length<3) {
//            System.out.println("USE: java -jar LDR.jar minFreq minSize ldrVersion");
//            return;
//        }
//        QatarDeception_Demo(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
        
//        if (args.length<5) {
//            System.out.println("USE: java -jar LDR.jar source target minFreq minSize ldrVersion");
//            return;
//        }
//        CrossGenre_Deception_Demo(args[0], args[1], Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]));
        /* End Qatar Deception */
        
        
//        NNNStats();
        
        
//        NNNCorpus();
//        NNN1vsallCorpus();
//        NLI_VERSUS();
        
//        new SemEval_18_irony(10, 1, 
//                "/mnt/data2/semeval/SemEval2018-T4-train-taskA.txt", 
//                "/mnt/data2/semeval/ldr/",
//                "/mnt/data2/semeval/training.arff", 
//                null, null).Run();
        
//        new PAN_AP_17_gender(10, 1, 
//                "/mnt/data2/pan17-author-profiling-training-dataset-2017-03-10/ar/", 
//                "/mnt/data2/ldr/pan17-gender-ar/", 
//                "/mnt/data2/ldr/pan17-gender-ar/pan17.gender.ar.training.arff", 
//                "/mnt/data2/pan17-author-profiling-test-2017-03-16/ar/", 
//                "/mnt/data2/ldr/pan17-gender-ar/pan17.gender.ar.test.arff").Run();


//        FreqMngr.Test();
        
//        for (int iFold=0;iFold<1;iFold++) {
//            System.out.println("Processing LDR for ICLE fold " + iFold);
//            int f = 3;
//            int s = 3;
//            new ICLE(f, s,  
//                   "/mnt/data/NLI/NLI datasets/icle-10folds/mix/" + iFold + "/training",
//                   "/mnt/data/NLI/LDR/ICLE-10fold/ICLE." + f + "." + s + "/f" + iFold + "/",
//                   "/mnt/data/NLI/LDR/ICLE-10fold/ICLE." + f + "." + s + ".f" + iFold + ".training.arff/",
//                   "/mnt/data/NLI/NLI datasets/icle-10folds/mix/" + iFold + "/test",
//                   "/mnt/data/NLI/LDR/ICLE-10fold/ICLE." + f + "." + s + ".f" + iFold + ".test.arff/"
//           ).Run();
//            
//            System.gc();
//        }
        
        
        /* AUTORITAS SENTIMENT ANALYSIS EXAMPLE */
//        new AutoritasSentimentAnalysis(5, 1, 
//                    "/mnt/data/SentimentAnalysis/tass2016.general.csv",
//                "/mnt/data/SentimentAnalysis/ldr.tass2016.2/",
//                "/mnt/data/SentimentAnalysis/ldr.tass2016.2/tass2016.arff",
//                "/mnt/data/SentimentAnalysis/tass2016.general.test.csv",
//                "/mnt/data/SentimentAnalysis/ldr.tass2016.2/test.tass2016.arff"
//        ).Run();
        /**/   
        
        
//        new TOEFL(2, 1, 
//               "/mnt/data/NLI/NLI datasets/swt/training",
//               "/mnt/data/NLI/LDR/SWT.CHIARAOTH.2.1/",
//               "/mnt/data/NLI/LDR/SWT.CHIARAOTH.2.1.training.arff/",
//               "/mnt/data/NLI/NLI datasets/swt/test",
//               "/mnt/data/NLI/LDR/SWT.CHIARAOTH.2.1.test.arff/"
//       ).Run();
        
//       new ICLE(5, 1, 
//               "/mnt/data/NLI/NLI datasets/icle-clean-ascii",
//               "/mnt/data/NLI/LDR/ICLE.borrar/",
//               "/mnt/data/NLI/LDR/ICLE.borrar.training.arff/",
//               "/mnt/data/NLI/LDR/ICLE.borrar/",
//               "/mnt/data/NLI/LDR/ICLE.borrar.test.arff/"
//       ).Run();
//               

        
//        boolean onlyTest = true;
//        int minFreq = 2;
//        int minSize = 1;
//        int testSet = 5;
//        new RUSProfiling(minFreq, minSize,
//                "/mnt/data/PAN/datasets/PANRus/rusprofiling.training.20170629",
//                "/mnt/data/PAN/ldr/rusprofiling/rusprofiling." + minFreq + "." + minSize,
//                "/mnt/data/PAN/ldr/rusprofiling/rusprofiling.training." + minFreq + "." + minSize + ".arff",
//                "/mnt/data/PAN/datasets/PANRus/rusprofiling.test." + testSet + ".20170829",
//                "/mnt/data/PAN/ldr/rusprofiling/rusprofiling.test" + testSet + "." + minFreq + "." + minSize + ".arff",
//                onlyTest
//        ).Run();
        
        
        
        
        
        
//        ICLE2TOEFL();
//        RepLab();
//        CMUQ();
//        CMUQ100();
        
//        TOEFL();
        
        
        
//        new PAN_AP_17_gender(10, 1, 
//                "/mnt/data/PAN/datasets/pan17/pan17-author-profiling-training-2017-03-10/es", 
//                "/mnt/data/PAN/ldr/pan17/gender.es.10.1", 
//                "/mnt/data/PAN/ldr/pan17/gender.es.10.1/pan17.gender.es.10.1.training.arff", 
//                "/mnt/data/PAN/datasets/pan17/pan17-author-profiling-test-2017-03-16/es", 
//                "/mnt/data/PAN/ldr/pan17/gender.es.10.1/pan17.gender.es.10.1.test.arff", 1).Run();
        
//        new PAN_AP_17_gender(10, 1, 
//                "/mnt/data/PAN/datasets/pan17/pan17-author-profiling-training-2017-03-10/en", 
//                "/mnt/data/PAN/ldr/pan17/gender.en.10.1", 
//                "/mnt/data/PAN/ldr/pan17/gender.en.10.1/pan17.gender.en.10.1.training.arff", 
//                "/mnt/data/PAN/datasets/pan17/pan17-author-profiling-test-2017-03-16/en", 
//                "/mnt/data/PAN/ldr/pan17/gender.en.10.1/pan17.gender.en.10.1.test.arff", 1).Run();
        
        
//        PAN18();
        
//        PAN14CMUQ_Demo();
        
        
        
        /* Para demo Arap */
//        NNNFullTraining();
//        NNN1vsallFullTraining();
        
        /* PAPER SPECIAL ISSUE LVI */
        /*
        int minFreq = 10;
        String lang = "en";
        int v = 2;
        new PAN_AP_17_variety(minFreq, 1, 
                "/mnt/data/PAN/datasets/pan17/pan17-author-profiling-training-2017-03-10/" + lang+ "/", 
                "/mnt/data/PAN/ldr/pan17/variety." + lang+ "." + minFreq + ".1.v" + v + "/", 
                "/mnt/data/PAN/ldr/pan17/variety." + lang+ "." + minFreq + ".1.v" + v + "/pan17.variety." + lang+ "." + minFreq + ".1.v" + v+ ".training.arff", 
                "/mnt/data/PAN/datasets/pan17/pan17-author-profiling-test-2017-03-16/" + lang+ "/", 
                "/mnt/data/PAN/ldr/pan17/variety." + lang+ "." + minFreq + ".1.v" + v + "/pan17.variety." + lang+ "." + minFreq + ".1.v" + v+ ".test.arff", 
                lang, v).Run();
        */
        
//        CMUQ100("variety");
        
//        Credibility_Demo();
//        Credibility_Prototype();
//        Irony_Demo();
//        Irony_Prototype();
//        QatarDeception_Demo();
//        for (String dataset:new String[]{"SevenTruthSevenLies_SEP21", "SevenTruthSevenLies_Unhighlighted", "SevenTruthSevenLies_Yellow", "SevenTruthSevenLies_Orange", "SevenTruthSevenLies_Red"}) {
//            SevenTruthSevenLies_Demo(dataset);
//        }
        
//        CMUQ198("age");
//        CMUQ198("gender");
//        CMUQ198("variety");

        
        
//        if (args.length<1) {
//            System.out.println("java -jar LowDimensionalityRepresentation.jar task minFreq minSize ldrVersion direction");
//            return;
//        }
//        String task = args[0];
//        int minFreq = Integer.valueOf(args[1]);
//        int minSize = Integer.valueOf(args[2]);
//        int ldrVersion = Integer.valueOf(args[3]);
//        int dir = Integer.valueOf(args[4]);
//        
////        task = "variety";
////        minFreq = 1;
////        minSize = 10;
////        ldrVersion = 2;
////        dir = 1;
////        CMUQ198_4classes(task, minFreq, minSize, ldrVersion);
//        CMUQ198_4classes_PAN(task, minFreq, minSize, ldrVersion, dir);
////        dir = 2;
////        CMUQ198_4classes_PAN(task, minFreq, minSize, ldrVersion, dir);
        
    }
    
    private static void Irony_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/Irony/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        String sTrainingCorpus = sBaseDir + "/data/Irony - Train.csv";
        String sTestCorpus = sBaseDir + "/data/Irony - Test.csv";
        
        for (int minFreq:new Integer[]{10,5,1}) {
            for (int minSize:new Integer[]{1,2,3}) {
                for (int LDRVersion:new Integer[]{2,1}) {           
                        String sTrainingArff = sArffDir + "/Irony.training.mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                        String sTestArff = sArffDir + "/Irony.test.mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                        String sLDRPath = sLDRData + "/LDR.v" + LDRVersion + ".mf" + minFreq + ".ms" + minSize;

                        if (!new File(sLDRPath).exists()) {
                            new File(sLDRPath).mkdir();
                        }

                        new Irony(minFreq, minSize, sTrainingCorpus, 
                                sLDRPath, sTrainingArff, sTestCorpus, 
                                sTestArff, LDRVersion).Run();
                    
                }
            }
        }
    }
    
    private static void Irony_Prototype() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/Irony/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        String sTrainingCorpus = sBaseDir + "/data/Irony - Full.csv";
        String sTestCorpus = sBaseDir + "/data/Irony - Full.csv";
        String sTrainingArff = sArffDir + "/Irony.training.full.mf5.ms1.v1.arff";
        String sTestArff = sArffDir + "/Irony.test.full.mf5.ms1.v1.arff";
        String sLDRPath = sLDRData + "/LDR.v1.mf5.ms1.full";
        
        if (!new File(sLDRPath).exists()) {
                            new File(sLDRPath).mkdir();
                        }
        
        new Irony(5, 1, sTrainingCorpus, 
                                sLDRPath, sTrainingArff, sTestCorpus, 
                                sTestArff, 1).Run();
    }
    
    private static void Credibility_Prototype() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/Credibility/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        String sTrainingCorpus = sBaseDir + "/data/Credibility Annotated Arabic Tweet Corpus.Populated.txt";
        String sTestCorpus = sBaseDir + "/data/Credibility Annotated Arabic Tweet Corpus.Populated.txt";
        String sTrainingArff = sArffDir + "/Credibility.training.full.mf1.ms3.v1.arff";
        String sTestArff = sArffDir + "/Credibility.test.full.mf1.ms3.v1.arff";
        String sLDRPath = sLDRData + "/LDR.v1.mf1.ms3.full";
        
        if (!new File(sLDRPath).exists()) {
                            new File(sLDRPath).mkdir();
                        }
        
        new Credibility(1, 3, sTrainingCorpus, 
                                sLDRPath, sTrainingArff, sTestCorpus, 
                                sTestArff, 1).Run();
    }
    
    
    private static void CrossGenre_Deception_Demo(String source, String target, int minFreq, int minSize, int LDRVersion) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        if (source.equalsIgnoreCase(target)) {
            return;
        }
        
        String sSourceDir = "";
        if (source.equalsIgnoreCase("credibility")) {
            sSourceDir = "/mnt/data/Credibility/";
        } else if (source.equalsIgnoreCase("tweets")) {
            sSourceDir = "/mnt/data/Qatar-Deceptive/";
        } else if (source.equalsIgnoreCase("news")) {
            sSourceDir = "/mnt/data/Fake_Real_News_Merge_Qatar_Corpus/";
        }
        
        String sTrainingCorpus = "";
        if (source.equalsIgnoreCase("credibility")) {
            sTrainingCorpus = sSourceDir + "/data/Credibility Annotated Arabic Tweet Corpus.Populated.txt";
        } else if (source.equalsIgnoreCase("tweets")) {
            sTrainingCorpus = sSourceDir + "/data/Qatar Blockade - Blockade + Worldcup.csv";
        } else if (source.equalsIgnoreCase("news")) {
            sTrainingCorpus = sSourceDir + "/data/Fake_Real_News_Merge_Qatar_Corpus.csv";
        }
        
        String sTargetDir = "";
        if (target.equalsIgnoreCase("credibility")) {
            sTargetDir = "/mnt/data/Credibility/";
        } else if (target.equalsIgnoreCase("tweets")) {
            sTargetDir = "/mnt/data/Qatar-Deceptive/";
        } else if (target.equalsIgnoreCase("news")) {
            sTargetDir = "/mnt/data/Fake_Real_News_Merge_Qatar_Corpus/";
        }
        
        String sTestCorpus = "";
        if (target.equalsIgnoreCase("credibility")) {
            sTestCorpus = sTargetDir + "/data/Credibility Annotated Arabic Tweet Corpus.Populated.txt";
        } else if (target.equalsIgnoreCase("tweets")) {
            sTestCorpus = sTargetDir + "/data/Qatar Blockade - Blockade + Worldcup.csv";
        } else if (target.equalsIgnoreCase("news")) {
            sTestCorpus = sTargetDir + "/data/Fake_Real_News_Merge_Qatar_Corpus.csv";
        }
                
        String sBaseDir = "/mnt/data/Deception-Crossgenre/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        
        String sTrainingArff = sArffDir + "/" + source + ".training.mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
        String sTestArff = sArffDir + "/" + target + ".test.mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
        
        String sLDRPath = sLDRData + "/LDR." + source + ".v" + LDRVersion + ".mf" + minFreq + ".ms" + minSize;

        if (!new File(sLDRPath).exists()) {
            new File(sLDRPath).mkdir();
        }
        
        if (source.equalsIgnoreCase("Credibility")) {
            new Credibility(minFreq, minSize, sTrainingCorpus, 
                sLDRPath, sTrainingArff, "", 
                "", LDRVersion).Run();
        } else if (source.equalsIgnoreCase("News")) {
            new FakeRealNewsMergeQatar(minFreq, minSize, sTrainingCorpus, 
                sLDRPath, sTrainingArff, "", 
                "", LDRVersion).Run();
        } else if (source.equalsIgnoreCase("Tweets")) {
            new QatarDeception(minFreq, minSize, sTrainingCorpus, 
                sLDRPath, sTrainingArff, "", 
                "", LDRVersion).Run();
        }
        
        if (target.equalsIgnoreCase("Credibility")) {
            new Credibility(minFreq, minSize, "", 
                sLDRPath, "", sTestCorpus, 
                sTestArff, LDRVersion).Run();
        } else if (target.equalsIgnoreCase("News")) {
            new FakeRealNewsMergeQatar(minFreq, minSize, "", 
                sLDRPath, "", sTestCorpus, 
                sTestArff, LDRVersion).Run();
        } else if (target.equalsIgnoreCase("Tweets")) {
            new QatarDeception(minFreq, minSize, "", 
                sLDRPath, "", sTestCorpus, 
                sTestArff, LDRVersion).Run();
        }
        
        
    }
    
    private static void QatarDeception_Demo(int minFreq, int minSize, int LDRVersion) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/Qatar-Deceptive/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        String sTrainingCorpus = sBaseDir + "/data/Qatar Blockade - Blockade + Worldcup.csv";
//        String sTestCorpus = sBaseDir + "/data/mix/test-%fold%";
        
        String sTrainingArff = sArffDir + "/Qatar-Deceptive.training.mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
//        String sTestArff = sArffDir + "/Qatar-Deceptive.test.f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
        String sLDRPath = sLDRData + "/LDR.v" + LDRVersion + ".mf" + minFreq + ".ms" + minSize;

        if (!new File(sLDRPath).exists()) {
            new File(sLDRPath).mkdir();
        }

        new QatarDeception(minFreq, minSize, sTrainingCorpus, 
                sLDRPath, sTrainingArff, "", 
                "", LDRVersion).Run();
    }
    
    
    private static void QatarDeception_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/Qatar-Deceptive/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        String sTrainingCorpus = sBaseDir + "/data/mix/training-%fold%";
        String sTestCorpus = sBaseDir + "/data/mix/test-%fold%";
        
        for (int minFreq:new Integer[]{10,5,1}) {
            for (int minSize:new Integer[]{1,2,3}) {
                for (int LDRVersion:new Integer[]{2,1}) {           
                    for (int fold=0;fold<5;fold++) {
                        String sTrainingArff = sArffDir + "/Qatar-Deceptive.training.f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                        String sTestArff = sArffDir + "/Qatar-Deceptive.test.f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                        String sLDRPath = sLDRData + "/LDR.v" + LDRVersion + ".mf" + minFreq + ".ms" + minSize + ".f" + fold;

                        if (!new File(sLDRPath).exists()) {
                            new File(sLDRPath).mkdir();
                        }

                        new QatarDeception(minFreq, minSize, sTrainingCorpus.replace("%fold%", String.valueOf(fold)), 
                                sLDRPath, sTrainingArff, sTestCorpus.replace("%fold%", String.valueOf(fold)), 
                                sTestArff, LDRVersion).Run();
                    }
                }
            }
        }
    }
    
    private static void MADAR_Subtask1_Demo(int minFreq, int minSize, int LDRVersion) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/MADAR/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff";
        String sLDRData = sLDRDir + "/data/";
        String sTrainingCorpus = sBaseDir + "/dataset/MADAR-SHARED-TASK-fourth-release-18Mar2019/MADAR-Shared-Task-Subtask-1/MADAR-Corpus-26-train - MADAR-Corpus-26-train.csv"; 
        String sTestCorpus = sBaseDir + "/dataset/MADAR-SHARED-TASK-fourth-release-18Mar2019/MADAR-Shared-Task-Subtask-1/MADAR-Corpus-26-dev - MADAR-Corpus-26-dev.csv";
        
//        for (int minFreq:new Integer[]{10, 5, 1}) {
//            for (int minSize:new Integer[]{1, 2, 3}) {
//                for (int LDRVersion:new Integer[]{2, 1}) {
                    String sTrainingArff = sArffDir + "/MADAR.Subtask1.training.mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                    String sTestArff = sArffDir + "/MADAR.Subtask1.test.mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                    String sLDRPath = sLDRData + "/LDR.v" + LDRVersion + ".mf" + minFreq + ".ms" + minSize;
                    
                    if (!new File(sLDRPath).exists()) {
                            new File(sLDRPath).mkdir();
                        }

                        new MADAR_Subtask1(minFreq, minSize, sTrainingCorpus, 
                                sLDRPath, sTrainingArff, sTestCorpus, 
                                sTestArff, LDRVersion).Run();
//                }
//            }
//        }
    }
    
    private static void SevenTruthSevenLies_Demo(String ds) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/SevenTruthSevenLies/";
        String sLDRDir = sBaseDir + "/repr/ldr/" + ds + "/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        String sTrainingCorpus = sBaseDir + "/data/" + ds + "/mix/training-%fold%";
        String sTestCorpus = sBaseDir + "/data/" + ds + "/mix/test-%fold%";
        
        for (int minFreq:new Integer[]{10,5}) {
            for (int minSize:new Integer[]{1,2,3}) {
                for (int LDRVersion:new Integer[]{2, 1}) {   
                    for (int fold=0;fold<10;fold++) {
                        String sTrainingArff = sArffDir + "/SevenTruthSevenLies.training.f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                        String sTestArff = sArffDir + "/SevenTruthSevenLies.test.f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                        String sLDRPath = sLDRData + "/LDR.v" + LDRVersion + ".mf" + minFreq + ".ms" + minSize + ".f" + fold;

                        if (!new File(sLDRPath).exists()) {
                            new File(sLDRPath).mkdir();
                        }

                        new QatarDeception(minFreq, minSize, sTrainingCorpus.replace("%fold%", String.valueOf(fold)), 
                                sLDRPath, sTrainingArff, sTestCorpus.replace("%fold%", String.valueOf(fold)), 
                                sTestArff, LDRVersion).Run();
                    }
                }
            }
        }
    }
    
    private static void FakeRealNewsMergeQatar_Demo(int minFreq, int minSize, int ldrVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/Fake_Real_News_Merge_Qatar_Corpus/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        
        String sTrainingCorpus = sBaseDir + "/data/Fake_Real_News_Merge_Qatar_Corpus.csv";

        String sTrainingArff = sArffDir + "/FakeRealNewsMergeQatar.training.mf" + minFreq + ".ms" + minSize + ".v" + ldrVer + ".arff";
        String sLDRPath = sLDRData + "/LDR.v" + ldrVer + ".mf" + minFreq + ".ms" + minSize;

        if (!new File(sLDRPath).exists()) {
            new File(sLDRPath).mkdir();
        }

        new FakeRealNewsMergeQatar(minFreq, minSize, sTrainingCorpus, 
                sLDRPath, sTrainingArff, "", 
                "", ldrVer).Run();
    }
    
    private static void FakeRealNewsMergeQatar10Folds_Demo(int minFreq, int minSize, int ldrVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/Fake_Real_News_Merge_Qatar_Corpus/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        
        String sTrainingCorpus = sBaseDir + "/data/mix/training-%fold%";
        String sTestCorpus = sBaseDir + "/data/mix/test-%fold%";
        
        for (int fold=0;fold<10;fold++) {
            String sTrainingArff = sArffDir + "/FakeRealNewsMergeQatar.training.f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + ldrVer + ".arff";
            String sTestArff = sArffDir + "/FakeRealNewsMergeQatar.test.f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + ldrVer + ".arff";
            String sLDRPath = sLDRData + "/LDR.v" + ldrVer + ".mf" + minFreq + ".ms" + minSize + ".f" + fold;

            if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }

            new FakeRealNewsMergeQatar(minFreq, minSize, sTrainingCorpus.replace("%fold%", String.valueOf(fold)), 
                    sLDRPath, sTrainingArff, sTestCorpus.replace("%fold%", String.valueOf(fold)), 
                    sTestArff, ldrVer).Run();
        }
        
    }
    
    
    private static void Credibility_Demo(String set, int minFreq, int minSize, int ldrVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/Credibility/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        
        String sTrainingCorpus = sBaseDir; 
        String sTestCorpus = sBaseDir;
        
        if (set.equalsIgnoreCase("gov")) {
            sTrainingCorpus += "/data/SyrianGov/mix/training-%fold%";
            sTestCorpus += "/data/SyrianGov/mix/test-%fold%";
        } else if (set.equalsIgnoreCase("rev")) {
            sTrainingCorpus += "/data/SyrianRev/mix/training-%fold%";
            sTestCorpus += "/data/SyrianRev/mix/test-%fold%";
        } else {
            sTrainingCorpus += "/data/Credibility Annotated Arabic Tweet Corpus.Populated.txt";
            sTestCorpus += "/data/SyrianCred/test-%fold%";
        }
        

        String sTrainingArff = sArffDir + "/Credibility.training." + set + ".mf" + minFreq + ".ms" + minSize + ".v" + ldrVer + ".arff";
        String sTestArff = sArffDir + "/Credibility.test." + set + ".mf" + minFreq + ".ms" + minSize + ".v" + ldrVer + ".arff";
        String sLDRPath = sLDRData + "/LDR." + set + ".v" + ldrVer + ".mf" + minFreq + ".ms" + minSize;

        if (!new File(sLDRPath).exists()) {
            new File(sLDRPath).mkdir();
        }

        new Credibility(minFreq, minSize, sTrainingCorpus, 
                sLDRPath, sTrainingArff, "", 
                "", ldrVer).Run();
    }
    
    
    private static void Credibility_Demo10Folds(String set, int minFreq, int minSize, int ldrVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sBaseDir = "/mnt/data/Credibility/";
        String sLDRDir = sBaseDir + "/repr/ldr/";
        String sArffDir = sLDRDir + "/arff/";
        String sLDRData = sLDRDir + "/data/";
        
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
        
//        for (int minFreq:new Integer[]{10,5,2,1}) {
//            for (int minSize:new Integer[]{1,2,3}) {
//                for (int LDRVersion:new Integer[]{2,1}) {           
                    for (int fold=0;fold<10;fold++) {
                        String sTrainingArff = sArffDir + "/Credibility.training." + set + ".f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + ldrVer + ".arff";
                        String sTestArff = sArffDir + "/Credibility.test." + set + ".f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + ldrVer + ".arff";
                        String sLDRPath = sLDRData + "/LDR." + set + ".v" + ldrVer + ".mf" + minFreq + ".ms" + minSize + ".f" + fold;

                        if (!new File(sLDRPath).exists()) {
                            new File(sLDRPath).mkdir();
                        }

                        new Credibility(minFreq, minSize, sTrainingCorpus.replace("%fold%", String.valueOf(fold)), 
                                sLDRPath, sTrainingArff, sTestCorpus.replace("%fold%", String.valueOf(fold)), 
                                sTestArff, ldrVer).Run();
                    }
//                }
//            }
//        }
    }
    
    private static void PAN14CMUQ_Demo() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
//        PAN_AP_14_age_CMUQ.GenerateTrainingTruth("/mnt/data/PAN/datasets/pan14-cmuq/training");
        for (int LDR=1;LDR<=2;LDR++) {
            for (String lang:new String[]{"es","en"}) {
                new PAN_AP_14_age_CMUQ(1, 1, 
                        "/mnt/data/PAN/datasets/pan14-cmuq/training/" + lang, 
                        "/mnt/data/PAN/ldr/pan14-cmuq/age." + lang + ".1.1.v" + LDR,
                        "/mnt/data/PAN/ldr/pan14-cmuq/age.training." + lang + ".1.1.v" + LDR + ".arff",
                        "/mnt/data/PAN/datasets/pan14-cmuq/test/" + lang,
                        "/mnt/data/PAN/ldr/pan14-cmuq/age.test." + lang + ".1.1.v" + LDR + ".arff",
                    LDR)
                        .Run();
            }
        }
        
    }
    
    private static void IroSvA19_Demo(String variety, int minFreq, int minSize, int LDRVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sCorpusPath = "/mnt/data/IberEval/2019/IroSvA/dataset/IberLEF19-IroSvA-training-20190331/irosva." + variety +".training.csv";
        String sLDRPath = "/mnt/data/IberEval/2019/IroSvA/repr/ldr/data/irosva19." + variety + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVer;
        String sTrainingArff = "/mnt/data/IberEval/2019/IroSvA/repr/ldr/arff/irosva19.training." + variety + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        String sTestPath = "/mnt/data/IberEval/2019/IroSvA/dataset/IberLEF19-IroSvA-test-20190420/irosva." + variety + ".test.csv";
        String sTestArff = "/mnt/data/IberEval/2019/IroSvA/repr/ldr/arff/irosva19.test." + variety + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        
        if (!new File(sLDRPath).exists()) {
            new File(sLDRPath).mkdir();
        }
        
        new IroSvA19(minFreq, minSize, sCorpusPath, sLDRPath, sTrainingArff, sTestPath, sTestArff, LDRVer).Run();
    }
    
    private static void PAN_AP_20_Demo(String lang, int minFreq, int minSize, int LDRVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sCorpusPath = "/mnt/data/PAN/datasets/pan20/4.dataset/pan20-author-profiling-training-2020-02-23/" + lang;
        String sLDRPath = "/mnt/data/PAN/ldr/pan20/data/" + lang + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVer;
        String sTrainingArff = "/mnt/data/PAN/ldr/pan20/arff/" + lang + ".training.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        String sTestPath = "/mnt/data/PAN/datasets/pan20/4.dataset/pan20-author-profiling-test-2020-02-23/" + lang;
        String sTestArff = "/mnt/data/PAN/ldr/pan20/arff/" + lang + ".test.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        
         if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }
         
        new PAN_AP_20(minFreq, minSize, sCorpusPath, sLDRPath, sTrainingArff, sTestPath, sTestArff, LDRVer).Run();
    }
    
    private static void TASS_Sentiment_Demo(int minFreq, int minSize, int LDRVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sCorpusPath = "/mnt/data/TASS/dataset/general-train-tagged-3l.tsv";
        String sLDRPath = "/mnt/data/TASS/ldr/data/emo.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer;
        String sTrainingArff = "/mnt/data/TASS/ldr/arff/training.emo.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        String sTestPath = "/mnt/data/TASS/dataset/general-test-tagged-3l.tsv";
        String sTestArff = "/mnt/data/TASS/ldr/arff/test.emo.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        
        if (!new File(sLDRPath).exists()) {
            new File(sLDRPath).mkdir();
        }
        
        new TASS_Sentiment(minFreq, minSize, sCorpusPath, sLDRPath, sTrainingArff, sTestPath, sTestArff, LDRVer).Run();
    }
    
    private static void UNIFY_Ekman_Emotions_Demo(int minFreq, int minSize, int LDRVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sCorpusPath = "/mnt/data/UNIFY/dataset/train.tsv";
        String sLDRPath = "/mnt/data/UNIFY/ldr/data/emo.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer;
        String sTrainingArff = "/mnt/data/UNIFY/ldr/arff/training.emo.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        String sTestPath = "/mnt/data/UNIFY/dataset/test.tsv";
        String sTestArff = "/mnt/data/UNIFY/ldr/arff/test.emo.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        
        if (!new File(sLDRPath).exists()) {
            new File(sLDRPath).mkdir();
        }
        
        new UNIFY_Ekman_Emotions(minFreq, minSize, sCorpusPath, sLDRPath, sTrainingArff, sTestPath, sTestArff, LDRVer).Run();
    }
    
    private static void PAN_AP_15_personality_discrete_Demo(int minFreq, int minSize, int LDRVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
        for (String task:new String[]{"O", "C", "E", "A", "N"}) {
            String sCorpusPath = "/mnt/data/PAN/datasets/pan15/pan15-author-profiling-training-dataset-2015-04-23/pan15-author-profiling-training-dataset-spanish-2015-04-23/";
            String sLDRPath = "/mnt/data/PAN/ldr/pan15/data/" + task + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVer;
            String sTrainingArff = "/mnt/data/PAN/ldr/pan15/arff/" + task + ".training.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
            String sTestPath = "/mnt/data/PAN/datasets/pan15/pan15-author-profiling-test-dataset-2015-04-23/pan15-author-profiling-test-dataset2-spanish-2015-04-23/";
            String sTestArff = "/mnt/data/PAN/ldr/pan15/arff/" + task + ".test.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";

            if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }

            new PAN_AP_15_personality_discrete(task, minFreq, minSize, sCorpusPath, sLDRPath, sTrainingArff, sTestPath, sTestArff, LDRVer).Run();
        }
    }
    
    private static void PAN_AP_19_Demo(String task, String lang, int minFreq, int minSize, int LDRVer) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sCorpusPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-training-2019-02-18/" + lang;
        String sLDRPath = "/mnt/data/PAN/ldr/pan19/data/" + task + "." + lang + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVer;
        String sTrainingArff = "/mnt/data/PAN/ldr/pan19/arff/" + task + "." + lang + ".training.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        String sTestPath = "/mnt/data/PAN/datasets/pan19/dataset/pan19-author-profiling-test-2019-04-29/" + lang;
        String sTestArff = "/mnt/data/PAN/ldr/pan19/arff/" + task + "." + lang + ".test.mf" + minFreq + ".ms" + minSize + ".v" + LDRVer + ".arff";
        
         if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }
         
        if (task.equalsIgnoreCase("bots")) {
            new PAN_AP_19_bots(minFreq, minSize, sCorpusPath, sLDRPath, sTrainingArff, sTestPath, sTestArff, LDRVer).Run();
        } else {
            new PAN_AP_19_gender(minFreq, minSize, sCorpusPath, sLDRPath, sTrainingArff, sTestPath, sTestArff, LDRVer).Run();
        }
    }
    
    private static void PAN18() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        int LDR = 2;
        String lang = "es";
//        for (int LDR=1;LDR<=2;LDR++) {
//            for (String lang:new String[]{"ar","en","es"}) {
                new PAN_AP_18_gender(5, 1, 
                       "/mnt/data/PAN/datasets/pan18/pan18-author-profiling-training-2018-02-27/" + lang, 
                       "/mnt/data/PAN/ldr/pan18/gender." + lang + ".5.1.v" + LDR, 
                       "/mnt/data/PAN/ldr/pan18/gender." + lang + ".5.1.v" + LDR + "/pan18.gender." + lang + ".5.1.v" + LDR + ".training.arff", 
                       "/mnt/data/PAN/datasets/pan18/pan18-author-profiling-test-2018-03-20/" + lang, 
                       "/mnt/data/PAN/ldr/pan18/gender." + lang + ".5.1.v" + LDR + "/pan18.gender." + lang + ".5.1.v" + LDR + ".test.arff", LDR).Run();
//            }
//        }
    }
    
    private static void NNNStats() throws IOException {
        System.out.println("GLOBAL");
        NNN.PrintStats("/mnt/data/Native-Non-Native Corpus/TXT_with_English_header");
        String sTrainingCorpus = "/mnt/data/Native-Non-Native Corpus/5folds/mix/%fold%/training/";
        String sTestCorpus = "/mnt/data/Native-Non-Native Corpus/5folds/mix/%fold%/test/";
        
        
        for (int fold=0;fold<5;fold++) {
            System.out.println("TRAINING");
            NNN.PrintStats(sTrainingCorpus.replace("%fold%", String.valueOf(fold)));
            System.out.println("TEST");
            NNN.PrintStats(sTestCorpus.replace("%fold%", String.valueOf(fold)));
        }
    }
    
    private static void NNN1vsallFullTraining() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sTrainingCorpus = "/mnt/data/Native-Non-Native Corpus/TXT_with_English_header/";
        String sOutputPath = "/mnt/data/Native-Non-Native Corpus/FullTraining/";
        int minFreq = 5;
        int minSize = 1;
        int ldrVersion = 1;
        
        for (String lang:new String[]{"chinese", "urdu", "french", "english", "somali"}) {
            String sTrainingArff = sOutputPath + "/NNN.training." + lang + ".mf" + minFreq + ".ms" + minSize + ".v" + ldrVersion + ".arff";
            String sLDRPath = sOutputPath + "/LDR.v" + ldrVersion + "." + lang + ".mf" + minFreq + ".ms" + minSize;

            if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }

            new NNN1vsall(minFreq, minSize, sTrainingCorpus, 
                    sLDRPath, sTrainingArff, null, 
                    null, NNN1vsall.GetLabels(lang), lang, SET.TRAINING, ldrVersion).Run();
        }

    }
    
    private static void NNN1vsallCorpus() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sTrainingCorpus = "/mnt/data/Native-Non-Native Corpus/5folds/mix/%fold%/training/";
        String sTestCorpus = "/mnt/data/Native-Non-Native Corpus/5folds/mix/%fold%/test/";
        String sArffBase = "/mnt/data/Native-Non-Native Corpus/5folds/LDR-1vsall/";
        String sLDRBase = "/mnt/data/Native-Non-Native Corpus/5folds/LDR-1vsall/";
        
        for (int minFreq:new int[]{5,2,1}) {
            for (int minSize:new int[]{1,2,3}) {
                for (String lang:new String[]{"chinese"/*,"urdu", "french", "english", "somali"*/}) {
                    for (int LDRVersion=1;LDRVersion<=2;LDRVersion++) {
                        for (int fold=0;fold<5;fold++) {
                            String sTrainingArff = sArffBase + "/NNN.training." + lang + ".f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                            String sTestArff = sArffBase + "/NNN.test." + lang + ".f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                            String sLDRPath = sLDRBase + "/LDR.v" + LDRVersion + "." + lang + ".mf" + minFreq + ".ms" + minSize + ".f" + fold;

                            if (!new File(sLDRPath).exists()) {
                                new File(sLDRPath).mkdir();
                            }

                            new NNN1vsall(minFreq, minSize, sTrainingCorpus.replace("%fold%", String.valueOf(fold)), 
                                    sLDRPath, sTrainingArff, sTestCorpus.replace("%fold%", String.valueOf(fold)), 
                                    sTestArff, NNN1vsall.GetLabels(lang), lang, SET.BOTH, LDRVersion).Run();
                        }
                    }
                }
            }
        }
    }
    
    private static void NNNFullTraining() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        int minFreq = 1;
        int minSize = 1;
        int ldrVersion = 1;
        String sTrainingCorpus = "/mnt/data/Native-Non-Native Corpus/TXT_with_English_header/";
        String sOutputPath = "/mnt/data/Native-Non-Native Corpus/FullTraining/";
                
        if (!new File(sOutputPath).exists()) {
            new File(sOutputPath).mkdir();
        }
                
        new NNN(minFreq, minSize, sTrainingCorpus, 
                sOutputPath + "/LDR.v" + ldrVersion + ".mf" + minFreq + ".ms" + minSize, 
                sOutputPath + "/NNN.full-training.mf" + minFreq + ".ms" + minSize + ".v" + ldrVersion + ".arff", 
                null, null, null, SET.TRAINING, ldrVersion).Run();
    }
    
    
    private static void NNNCorpus() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
        
        int minFreq = 5;
        int minSize = 2;
        String sTrainingCorpus = "/mnt/data/Native-Non-Native Corpus/5folds/mix/%fold%/training/";
        String sTestCorpus = "/mnt/data/Native-Non-Native Corpus/5folds/mix/%fold%/test/";
        String sArffBase = "/mnt/data/Native-Non-Native Corpus/5folds/LDR/";
        String sLDRBase = "/mnt/data/Native-Non-Native Corpus/5folds/LDR/";
        
        
        
        
        for (int LDRVersion=1;LDRVersion<=2;LDRVersion++) {
            for (int fold=0;fold<5;fold++) {
                String sTrainingArff = sArffBase + "/NNN.training.f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                String sTestArff = sArffBase + "/NNN.test.f" + fold + ".mf" + minFreq + ".ms" + minSize + ".v" + LDRVersion + ".arff";
                String sLDRPath = sLDRBase + "/LDR.v" + LDRVersion + ".mf" + minFreq + ".ms" + minSize + ".f" + fold;
                
                if (!new File(sLDRPath).exists()) {
                    new File(sLDRPath).mkdir();
                }
                
                new NNN(minFreq, minSize, sTrainingCorpus.replace("%fold%", String.valueOf(fold)), 
                        sLDRPath, sTrainingArff, sTestCorpus.replace("%fold%", String.valueOf(fold)), 
                        sTestArff, null, SET.BOTH, LDRVersion).Run();
            }
        }
    }
    
    private static void TOEFL() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        int minFreq = 10;
        int minSize = 1;
        String sTrainingCorpus = "/mnt/data/NLI/NLI datasets/TOEFL/VERSUS/training/";
        String sTestCorpus = "/mnt/data/NLI/NLI datasets/TOEFL/VERSUS/test/";
        String sArffBase = "/mnt/data/NLI/NLI datasets/TOEFL/VERSUS/arff/";
        String sLDRBase = "/mnt/data/NLI/NLI datasets/TOEFL/VERSUS/LDR/";
        
        for (int LDRVersion = 1;LDRVersion<=2;LDRVersion++) {
            String sTrainingArff = sArffBase + "/TOEFL.training.all-lang." + minFreq + "." + minSize + ".v" + LDRVersion + ".arff";
            String sTestArff = sArffBase + "/TOEFL.test.all-lang." + minFreq + "." + minSize + ".v" + LDRVersion + ".arff";
            String sLDRPath = sLDRBase + "/LDRv" + LDRVersion + "." + minFreq + "." + minSize + ".all-lang/";

            new TOEFL(minFreq, minSize, sTrainingCorpus, sLDRPath, sTrainingArff, sTestCorpus, sTestArff, null, "", SET.BOTH, LDRVersion).Run();
        }
        
//        for (String lang:new String[]{"ARA", "CHI"}) {
//            for (int LDRVersion = 1;LDRVersion<=2;LDRVersion++) {
//                String sTrainingArff = sArffBase + "/TOEFL.training." + lang.toLowerCase() + "." + minFreq + "." + minSize + ".v" + LDRVersion + ".arff";
//                String sTestArff = sArffBase + "/TOEFL.test." + lang.toLowerCase() + "." + minFreq + "." + minSize + ".v" + LDRVersion + ".arff";
//                String sLDRPath = sLDRBase + "/LDRv" + LDRVersion + "." + minFreq + "." + minSize + "." + lang.toLowerCase() + "/";
//
//                new TOEFL(minFreq, minSize, sTrainingCorpus, sLDRPath, sTrainingArff, sTestCorpus, sTestArff, null, lang, SET.BOTH, LDRVersion).Run();
//            }
//        }

    }
       
    private static void NLI_VERSUS() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
        
                
//        for (String lang:new String[]{"ar", "ch", "id"}) {
//            for (String evalDataset:new String[]{"ICLE", "TOEFL", "lang8", "ICNALE", "SWTP"}) {
//                new NLI_VERSUS().PrepareDataset(lang, evalDataset, "/mnt/data/NLI/NLI datasets/VERSUS", "/mnt/data/NLI/NLI datasets/VERSUS/LDR");
//            }
//        }
                
//        for (String lang:new String[]{/*"ar", "ch"/*, */"id"}) {
//            for (String evalDataset:new String[]{/*"ICLE", "TOEFL", "lang8", "ICNALE",*/ "SWTP"}) {
//                new NLI_VERSUS().RefineDataset(lang, evalDataset, "/mnt/data/NLI/NLI datasets/VERSUS/LDR", 10000);
//            }
//        }
        
        int minFreq = 10;
        int minSize = 1;
        int LDRVersion = 2;
        
        for (String lang:new String[]{/*"ar",*/ "ch"/*, "id"*/}) {
            for (String evalDataset:new String[]{"ICLE"/*, "TOEFL", "lang8", "ICNALE", "SWTP"*/}) {
                
                String sTrainingDocs = "/mnt/data/NLI/NLI datasets/VERSUS/LDR/" + lang + "/" + evalDataset + "/training/";
                if (new File(sTrainingDocs).exists()) {
                    String sTestDocs = "/mnt/data/NLI/NLI datasets/VERSUS/LDR/" + lang + "/" + evalDataset + "/test/";
                    String sTrainingArff = "/mnt/data/NLI/NLI datasets/VERSUS/LDR/" + lang + "/" + evalDataset + "/LDRv" + LDRVersion + ".versus.training." + minFreq + "." + minSize + ".arff";
                    String sTestArff = "/mnt/data/NLI/NLI datasets/VERSUS/LDR/" + lang + "/" + evalDataset + "/LDRv" + LDRVersion + ".versus.test." + minFreq + "." + minSize + ".arff";
                    String sLDRPath = "/mnt/data/NLI/NLI datasets/VERSUS/LDR/" + lang + "/" + evalDataset + "/LDRv" + LDRVersion + "/";

                    ArrayList<String> oLabels = new ArrayList<String>();
                    oLabels.add(lang); oLabels.add("ot");
                    new NLI_VERSUS(minFreq, minSize, sTrainingDocs, sLDRPath, sTrainingArff, sTestDocs, sTestArff, oLabels, SET.BOTH, LDRVersion).Run();
                }
            }
        }
        
    }
    
    private static void CMUQ198_4classes_PAN(String task, int minFreq, int minSize, int ldrVersion, int direction) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sTrainingDocs = "";
        String sTestDocs = "";
        
        if (direction==0) {
            sTrainingDocs = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/dataset-4classes-PAN/training.cmuq198_4classes/";
            sTestDocs = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/dataset-4classes-PAN/test.pan17/";
        } else {
            sTrainingDocs = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/dataset-4classes-PAN/training.pan17/";
            sTestDocs = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/dataset-4classes-PAN/test.cmuq198_4classes/";
        }
        
        if (task.equalsIgnoreCase("variety")) {
            int nTweets = 100;
            String sTrainingArff, sTestArff, sLDRPath;
            
            if (direction==0) {
                sTrainingArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198-4classes.training.cmuq198-4classes.t-" + nTweets + ".variety.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
                sTestArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198-4classes.test.pan.t-" + nTweets + ".variety.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
                sLDRPath = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/ldr/cmuq198-4classes.ldr.cmuq198-4classes.pan.variety.t-" + nTweets + ".f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion;
            } else {
                sTrainingArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198-4classes.training.pan.t-" + nTweets + ".variety.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
                sTestArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198-4classes.test.cmuq198-4classes.t-" + nTweets + ".variety.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
                sLDRPath = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/ldr/cmuq198-4classes.ldr.pan.cmuq198-4classes.variety.t-" + nTweets + ".f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion;
            }

            if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }
            
            new CMUQ198_4classes_Arap_variety(minFreq, minSize, nTweets, sTrainingDocs, sLDRPath, sTrainingArff, sTestDocs, sTestArff, ldrVersion).Run();
        }
    }
    
    private static void CMUQ198_4classes(String task, int minFreq, int minSize, int ldrVersion) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        String sTrainingDocs = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/dataset-4classes/training/";
        String sTestDocs = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/dataset-4classes/test/";
        
        if (task.equalsIgnoreCase("variety")) {
            int nTweets = 100;
            
            String sTrainingArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198-4classes.training.t-" + nTweets + ".variety.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
            String sTestArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198-4classes.test.t-" + nTweets + ".variety.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
            String sLDRPath = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/ldr/cmuq198-4classes.ldr.variety.t-" + nTweets + ".f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion;

            if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }
            
            new CMUQ198_4classes_Arap_variety(minFreq, minSize, nTweets, sTrainingDocs, sLDRPath, sTrainingArff, sTestDocs, sTestArff, ldrVersion).Run();
        }
    }
    
    private static void CMUQ198(String task, int minFreq, int minSize, int ldrVersion) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
//        String sTrainingDocs = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-FULL/dataset/";
//        String sTestDocs = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-FULL/dataset/";
        
        String sTrainingDocs = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/dataset/training/";
        String sTestDocs = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/dataset/test/";
        
        if (task.equalsIgnoreCase("variety")) {
//            int minFreq = 10;
//            int minSize = 1;
//            int ldrVersion = 2;
            int nTweets = 100;
            
            
            String sTrainingArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198.training.t-" + nTweets + ".variety.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
            String sTestArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198.test.t-" + nTweets + ".variety.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
            String sLDRPath = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/ldr/ldr.variety.t-" + nTweets + ".f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion;

            if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }
            
            new CMUQ198_Arap_variety(minFreq, minSize, nTweets, sTrainingDocs, sLDRPath, sTrainingArff, sTestDocs, sTestArff, ldrVersion).Run();
        }
        if (task.equalsIgnoreCase("gender")) {
//            int minFreq = 5;
//            int minSize = 1;
//            int ldrVersion = 1;
            int nTweets = 100;
            
            String sTrainingArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198.training.t-" + nTweets + ".gender.f-" + minFreq + ".s-" + minSize  + ".v-" + ldrVersion +  ".arff";
            String sTestArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198.test.t-" + nTweets + ".gender.f-" + minFreq + ".s-" + minSize  + ".v-" + ldrVersion +  ".arff";
            String sLDRPath = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/ldr/ldr.gender.t-" + nTweets + ".f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion;
            
            if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }
            
            new CMUQ198_Arap_gender(minFreq, minSize, nTweets, sTrainingDocs, sLDRPath, sTrainingArff, sTestDocs, sTestArff, ldrVersion).Run();
        }
        if (task.equalsIgnoreCase("age")) {
//            int minFreq = 5;
//            int minSize = 1;
//            int ldrVersion = 1;
            int nTweets = 100;
            
            String sTrainingArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198.training.t-" + nTweets + ".age.f-" + minFreq + ".s-" + minSize  + ".v-" + ldrVersion +  ".arff";
            String sTestArff = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/arff/cmuq198.test.t-" + nTweets + ".age.f-" + minFreq + ".s-" + minSize  + ".v-" + ldrVersion +  ".arff";
            String sLDRPath = "/mnt/data/CMUQ-Arap_Corpus/SETOF198-SPLIT/ldr/ldr.age.t-" + nTweets + ".f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion;
            
            if (!new File(sLDRPath).exists()) {
                new File(sLDRPath).mkdir();
            }
            
            new CMUQ198_Arap_age(minFreq, minSize, nTweets, sTrainingDocs, sLDRPath, sTrainingArff, sTestDocs, sTestArff, ldrVersion).Run();
        }
    }
    
    private static void CMUQ100(String task) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
        
        if (task.equalsIgnoreCase("gender")) {
            int minFreq = 1;
            int minSize = 1;
            for (int ldrVersion=1;ldrVersion<3;ldrVersion++) {
                for (int nTweets:new int[]{/*200,*/500}) {
                    String sTrainingDocs = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/training";
                    String sTestDocs = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/test";
                    String sTrainingArff = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/arff/cmuq100.training.t-" + nTweets + ".gender.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
                    String sTestArff = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/arff/cmuq100.test.t-" + nTweets + ".gender.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
                    String sLDRPath = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/ldr/ldr.gender.t-" + nTweets + ".f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion;

                    new CMUQ100_Arap_gender(minFreq, minSize, nTweets, sTrainingDocs, sLDRPath, sTrainingArff, sTestDocs, sTestArff, ldrVersion).Run();
                }
            }
        }
        
        if (task.equalsIgnoreCase("age")) {
             int minFreq = 1;
            int minSize = 1;
            for (int ldrVersion=1;ldrVersion<2;ldrVersion++) {
                for (int nTweets:new int[]{100}) {  // 100, 200, 500
                    String sTrainingDocs = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/training";
                    String sTestDocs = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/test";
                    String sTrainingArff = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/arff/cmuq100.training.t-" + nTweets + ".age.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
                    String sTestArff = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/arff/cmuq100.test.t-" + nTweets + ".age.f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion + ".arff";
                    String sLDRPath = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/ldr/ldr.age.t-" + nTweets + ".f-" + minFreq + ".s-" + minSize + ".v-" + ldrVersion;

                    new CMUQ100_Arap_age(minFreq, minSize, nTweets, sTrainingDocs, sLDRPath, sTrainingArff, sTestDocs, sTestArff, ldrVersion).Run();
                }
            }
        }
        
        if (task.equalsIgnoreCase("variety")) {
            int minSize = 1;
            for (int nTweets:new int[]{10, 20, 30, 40, 50, 60, 70, 80, 90}) {
                for (int minFreq:new Integer[]{10/*, 5*/}) {
                    for (int v:new Integer[]{/*1,*/2}) {
                        String sTrainingDocs = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/training";
                        String sTestDocs = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/test";
                        String sTrainingArff = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/arff/cmuq100.training.t-" + nTweets + "." + task + ".f-" + minFreq + ".s-" + minSize + ".v-" + v + ".arff";
                        String sTestArff = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/arff/cmuq100.test.t-" + nTweets + "." + task + ".f-" + minFreq + ".s-" + minSize + ".v-" + v + ".arff";
                        String sLDRPath = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/Setof100/ldr/ldr." + task + ".t-" + nTweets + ".f-" + minFreq + ".s-" + minSize + ".v-" + v;

                        new CMUQ100_Arap_variety(minFreq, minSize, nTweets, sTrainingDocs, sLDRPath, sTrainingArff, sTestDocs, sTestArff, v).Run();
                    }
                }
            }
        }
    }
    
    private static void CMUQ() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
//        String sCorpus = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/processed";
//        String sBalanced = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/balanced/processed";
//        CMUQ_Arap_gender.Balance(sCorpus, sBalanced, 405);
        
        
        int minFreq = 5;
        int minSize = 1;
        
        int iIni = 2;
        int iEnd = iIni+1;
//        int iEnd = 5;
        
        for (int iFold=iIni;iFold<iEnd;iFold++) {
            String sTrainingDocs = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/balanced/3folds/mix/" + iFold + "/training";
            String sTestDocs = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/balanced/3folds/mix/" + iFold + "/test";
            String sTrainingArff = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/balanced/3folds/arff/cmuq.training." + iFold + "." + minFreq + "." + minSize + ".arff";
            String sTestArff = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/balanced/3folds/arff/cmuq.test." + iFold + "." + minFreq + "." + minSize + ".arff";
            String sLDRPath = "/media/kicorangel/Acer HD/mnt/data/CMUQ-Arap_Corpus_V0.55/balanced/3folds/ldr/" + iFold;
            
            new CMUQ_Arap_gender(minFreq, minSize, sTrainingDocs, sLDRPath, sTrainingArff, sTestDocs, sTestArff, 1).Run();
        }
    }
    private static void RepLab() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        
        // 1: INFLUENCER VS. NO INFLUENCER
        // 2: ALL GROUPS
        // 3: public: PUBLIC INSTITUTIOS + NGO
        //    company:  PRIVATE COMPANIES + EMPLOYEE
        //    celebrity:  CELEBRITIES & SPORTMEN & 
        //    journ-prof:  JOURNALIST & PROFESSIONALS
        //      UNDECIDABLE
        // 4: JOURNALIST VS. PROFESSIONAL
        // 5: JOURNALIST + PROFESSIONAL vs. OTHERs
        
        
        /* REPLAB CON VICTORIA */
        int minFreq = 10;
        int minSize = 1;
        int conf = 5;
        String domain = "D01";
        boolean bIncludeUndecidable = false;
        int LDRVersion = 1;
        new Replab14(minFreq, minSize, 
                   "/mnt/data/RepLab/replab2014-dataset-unofficial/author_profiling/training", 
                   "/mnt/data/RepLab/LDR/replab14." + minFreq + "." + minSize + "." + conf + "." + domain +  "/", 
                   "/mnt/data/RepLab/LDR/replab14.training." + minFreq + "." + minSize + "." + conf + "." + domain + ".arff", 
                   "/mnt/data/RepLab/replab2014-dataset-unofficial/author_profiling/test", 
                   "/mnt/data/RepLab/LDR/replab14.test." + minFreq + "." + minSize + "." + conf + "." + domain + ".arff",
                   conf, domain, bIncludeUndecidable, SET.BOTH, LDRVersion
           ).Run();
     
        domain = "D02";
        new Replab14(minFreq, minSize, 
                   "/mnt/data/RepLab/replab2014-dataset-unofficial/author_profiling/training", 
                   "/mnt/data/RepLab/LDR/replab14." + minFreq + "." + minSize + "." + conf + "." + domain +  "/", 
                   "/mnt/data/RepLab/LDR/replab14.training." + minFreq + "." + minSize + "." + conf + "." + domain + ".arff", 
                   "/mnt/data/RepLab/replab2014-dataset-unofficial/author_profiling/test", 
                   "/mnt/data/RepLab/LDR/replab14.test." + minFreq + "." + minSize + "." + conf + "." + domain + ".arff",
                   conf, domain, bIncludeUndecidable, SET.BOTH, LDRVersion
           ).Run();
        
        
        
        
     /**/
        
//    int minFreq = 10;
//    int minSize = 1;
//    String domain = "D01";
//     new Replab14_Influencers(minFreq, minSize, 
//                "/mnt/data/RepLab/replab_dataset/RL2014D01/RL2014D01_train.txt", 
//                "/mnt/data/RepLab/LDR/replab14inf." + minFreq + "." + minSize + "." + domain +  "/", 
//                "/mnt/data/RepLab/LDR/replab14inf.training." + minFreq + "." + minSize + "." + domain + ".arff", 
//                "/mnt/data/RepLab/replab_dataset/RL2014D01/RL2014D01_test.txt", 
//                "/mnt/data/RepLab/LDR/replab14inf.test." + minFreq + "." + minSize + "." + domain + ".arff",
//                domain
//        ).Run();
        
    }
    
    private static void ICLE2TOEFL() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
//        int minFreq = 5;
//        int minSize = 1;
//        String sCorpusPath = "/media/kicorangel/Acer HD/mnt/data/NLI/NLI datasets/ICLE/icle-clean-ascii";
//        String sLDRPath = "/media/kicorangel/Acer HD/mnt/data/NLI/LDR/ICLE2TOEFL/chi/5.1/";
//        String sTestPath = "/media/kicorangel/Acer HD/mnt/data/NLI/NLI datasets/TOEFL/prepared4ldr/test";
//        String sTrainArff = "/media/kicorangel/Acer HD/mnt/data/NLI/LDR/ICLE2TOEFL/icle2toefl.train.chi.5.1.arff";
//        String sTestArff = "/media/kicorangel/Acer HD/mnt/data/NLI/LDR/ICLE2TOEFL/icle2toefl.test.chi.5.1.arff";
//        ArrayList<String> Labels = new ArrayList<String>();
//        Labels.add("chinese"); Labels.add("french"); Labels.add("german"); Labels.add("italian"); Labels.add("japanese"); 
//        Labels.add("spanish"); Labels.add("turkish");
//        
//        new ICLE(minFreq, minSize, sCorpusPath, sLDRPath, sTrainArff, null, null, Labels, SET.TRAINING).Run();
//        new TOEFL(minFreq, minSize, null, sLDRPath, null, sTestPath, sTestArff, Labels, SET.TEST).Run();
        
    }
    
}


