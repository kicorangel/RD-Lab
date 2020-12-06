/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ScriptListening.Tagger;

import com.kicorangel.repr.ldse.GenerateVectorSpaceModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import ScriptListening.Tagger.Datasets.Variety;
import com.kicorangel.repr.common.Prediction;
import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 *
 * @author @kicorangel
 */
public class LVIPredictor {
    private ArrayList<String> mLabels;
    private Classifier mClassifier;
    private GenerateVectorSpaceModel mVSM;
    private Hashtable<String, String> mProperNouns;
    
    private String BASEPATH = "/mnt/data/vodafone-models/variety.es.5.1.v1";
    private String LDRPath = BASEPATH;
    private String ModelPath = BASEPATH + "/es.variety.pan17.model";
    
    public LVIPredictor() throws IOException, Exception {
        Variety varietyPredictor = new Variety(5, 1, "", LDRPath, "", "", "", "es", 1);
        mLabels = varietyPredictor.GetLabels("es");
        mClassifier = LoadClassifier(ModelPath);
        mVSM = new GenerateVectorSpaceModel(mLabels, LDRPath, 1, 1);
    }
    
    public Prediction Predict(String text) {
        Prediction prediction = new Prediction();
        
        try {
            mVSM.Process(text);
            Instance inst = mVSM.GetInstance();
            prediction.PredictedClass = mClassifier.classifyInstance(inst); 
            prediction.ConfidenceValues = mClassifier.distributionForInstance(inst);
            prediction.Confidence = prediction.ConfidenceValues[(int)prediction.PredictedClass];
            prediction.sPredictedClass = GetLVI(prediction.PredictedClass);
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
    
    private String GetLVI(double lvi) {
        String sLVI = "";
        
        if (lvi==0) {
            sLVI = "argentina"; 
        } else if (lvi==1) {    
            sLVI = "spain";
        } else if (lvi==2) {
            sLVI = "peru";
        } else if (lvi==3) {
            sLVI = "chile";
        } else if (lvi==4) {
            sLVI = "mexico";
        } else if (lvi==5) {
            sLVI = "colombia";
        } else if (lvi==6) {
            sLVI = "venezuela";
        } 
        
        return sLVI;
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
}
