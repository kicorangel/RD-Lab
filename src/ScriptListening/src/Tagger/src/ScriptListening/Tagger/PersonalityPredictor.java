/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ScriptListening.Tagger;

import com.kicorangel.repr.ldse.GenerateVectorSpaceModel;
import ScriptListening.Tagger.Datasets.Personality;
import java.io.IOException;
import java.util.ArrayList;
import com.kicorangel.repr.common.Prediction;
import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 *
 * @author @kicorangel
 */
public class PersonalityPredictor {
    private String mTask;
    private ArrayList<String> mLabels;
    private Classifier mClassifier;
    private GenerateVectorSpaceModel mVSM;
    
    private String BASEPATH = "/mnt/data/vodafone-models/personality.es.TASK.1.1.vLDR";
    private String LDRPath = BASEPATH;
    private String ModelPath = BASEPATH + "/personality.TASK.1.1.vLDR.model";
    
    public PersonalityPredictor(String task) throws IOException, Exception {
        mTask = task;
        BASEPATH = BASEPATH.replaceAll("LDR", GetLDRVersion(mTask)).replaceAll("TASK", mTask.toUpperCase());
        LDRPath = BASEPATH;
        ModelPath = ModelPath.replaceAll("LDR", GetLDRVersion(mTask)).replaceAll("TASK", mTask.toUpperCase());
        Personality personalityProfiler = new Personality(task, 1, 1, "", LDRPath, "", "", "", Integer.valueOf(GetLDRVersion(mTask)));
        mLabels = personalityProfiler.GetLabels();
        mClassifier = LoadClassifier(ModelPath);
        mVSM = new GenerateVectorSpaceModel(mLabels, LDRPath, 1, Integer.valueOf(GetLDRVersion(mTask)));
    }
    
    public Prediction Predict(String text) {
        Prediction prediction = new Prediction();
        
        try {
            mVSM.Process(text);
            Instance inst = mVSM.GetInstance();
            prediction.PredictedClass = mClassifier.classifyInstance(inst); 
            prediction.ConfidenceValues = mClassifier.distributionForInstance(inst);
            prediction.Confidence = prediction.ConfidenceValues[(int)prediction.PredictedClass];
            prediction.sPredictedClass = GetPersonalityGroup(mTask, prediction.PredictedClass);
            prediction.sConfidence = String.format("%.0f", (double)Math.round(100*prediction.Confidence)) + "%";
        } catch (Exception ex) {
//            System.out.println(ex.toString());
            prediction.PredictedClass = -1;
            prediction.ConfidenceValues = new double[0];
            prediction.Confidence = 0;
            prediction.sPredictedClass = "";
            prediction.sConfidence = "";
        }
        
        return prediction;
    }
    
    private String GetPersonalityGroup(String task, double prediction) {
        String sPersonalityGroup = "";
        
        if (task.equalsIgnoreCase("O")) { //V8
            if (prediction==0) {
                return "open";
            } else {
                return "closed";
            }
        } else if (task.equalsIgnoreCase("C")) {   // V7
            if (prediction==0) {
                return "conscientious";
            } else {
                return "careless";
            }
        } else if (task.equalsIgnoreCase("E")) { //V4
            if (prediction==0) {
                return "introverted";
            } else {
                return "extroverted";
            } 
        } else if (task.equalsIgnoreCase("A")) {   // V6
            if (prediction==0) {
                return "agreeable";
            } else {
                return "disagreeable";
            }
        } else if (task.equalsIgnoreCase("N")) {   // V5
            if (prediction==0) {
                return "neurotic";
            } else {
                return "stable";
            }
        }
        
        return sPersonalityGroup;
    }
    
    private static Classifier LoadClassifier(String pathName)
    {
        Classifier oClassifier = null;
        try
        {
            java.io.ObjectInput oi = new java.io.ObjectInputStream(new java.io.FileInputStream(pathName));
            Object o = oi.readObject();
            oClassifier = (weka.classifiers.Classifier)o;
            oi.close();
        }
        catch (IOException ex)
        {
            String sDescription = ex.toString();
            String s = sDescription;
        }
        catch (ClassNotFoundException ex)
        {
            String sDesc = ex.toString();
            String s = sDesc;
        }
        catch (Error e)
        {
            String sDescription = e.getMessage();
            String s = sDescription;
        }
        catch (Exception ex)
        {
            String sDescription = ex.toString();
            String s = sDescription;
        }
        
        return oClassifier;
    }
    
    private String Normalize(String data)
    {
        data = data.toLowerCase();
        data = data.trim();
        
        data = data.replace("á", "a");
        data = data.replace("à", "a");
        data = data.replace("ä", "a");
        data = data.replace("â", "a");
        data = data.replace("é", "e");
        data = data.replace("è", "e");
        data = data.replace("ë", "e");
        data = data.replace("ê", "e");
        data = data.replace("í", "i");
        data = data.replace("ì", "i");
        data = data.replace("ï", "i");
        data = data.replace("î", "i");
        data = data.replace("ó", "o");
        data = data.replace("ò", "o");
        data = data.replace("ö", "o");
        data = data.replace("ô", "o");
        data = data.replace("ú", "u");
        data = data.replace("ù", "u");
        data = data.replace("ü", "u");
        data = data.replace("û", "u");

        return data;
    }
    
    private String GetLDRVersion(String task) {
        if (task.equalsIgnoreCase("C")) {
            return "1";
        } else {
            return "2";
        }
    }
}
