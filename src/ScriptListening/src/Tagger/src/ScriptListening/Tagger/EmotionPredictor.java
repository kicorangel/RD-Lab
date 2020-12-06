/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ScriptListening.Tagger;

import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import com.kicorangel.repr.enumerations.SET;
import com.kicorangel.repr.nGrams.GenerateVectorSpaceModel;
import ScriptListening.Tagger.Datasets.Emotions;
import java.io.IOException;
import java.util.ArrayList;
import com.kicorangel.repr.common.Prediction;
import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 *
 * @author @kicorangel
 */
public class EmotionPredictor {
    private ArrayList<String> mLabels;
    private Classifier mClassifier;
    private GenerateVectorSpaceModel mVSM;
    
    private String BASEPATH = "/mnt/data/vodafone-models/emotion.char.3.1000/";
    private String NGRAMSPATH = BASEPATH + "tmp.CHAR.n-3.t-1000.txt";
    private String ModelPath = BASEPATH + "/emo.es.c.3.1000.model";
    
    public EmotionPredictor() throws IOException, Exception {
        Emotions emotionPredictor = new Emotions(3, 1000, NGRAMTYPE.CHAR, "", BASEPATH, "", "", "", Emotions.GetLabels(), SET.BOTH, 
                            new PreprocessingOptions(true, false, false, false, 0, new String[0]));
        mLabels = emotionPredictor.GetLabels();
        mClassifier = LoadClassifier(ModelPath);
        mVSM = new GenerateVectorSpaceModel(mLabels, NGRAMSPATH, 3, 1000, 1, NGRAMTYPE.CHAR, new PreprocessingOptions(true, false, false, false, 0, new String[0]));
    }
    
    public Prediction Predict(String text) {
        Prediction prediction = new Prediction();
        
        try {
            mVSM.Process(text);
            Instance inst = mVSM.GetInstance();
            prediction.PredictedClass = mClassifier.classifyInstance(inst); 
            prediction.ConfidenceValues = mClassifier.distributionForInstance(inst);
            prediction.Confidence = prediction.ConfidenceValues[(int)prediction.PredictedClass];
            prediction.sPredictedClass = GetEmotionGroup(prediction.PredictedClass);
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
    
    private String GetEmotionGroup(double prediction) {
        String sEmotionGroup = mLabels.get((int)Math.round(prediction));
        
        return sEmotionGroup;
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
}
