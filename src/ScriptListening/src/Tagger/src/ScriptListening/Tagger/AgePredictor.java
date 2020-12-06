/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ScriptListening.Tagger;

import com.kicorangel.repr.ldse.GenerateVectorSpaceModel;
import ScriptListening.Tagger.Datasets.Age;
import com.kicorangel.repr.common.Prediction;
import java.io.IOException;
import java.util.ArrayList;
import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 *
 * @author @kicorangel
 */
public class AgePredictor {
    private ArrayList<String> mLabels;
    private Classifier mClassifier;
    private GenerateVectorSpaceModel mVSM;
   
    private String BASEPATH = "/mnt/data/vodafone-models/age.es.5.1.v1";
    private String LDRPath = BASEPATH;
    private String ModelPath = BASEPATH + "/es.age.pan14.model";
    
    public AgePredictor() throws IOException, Exception {
//        PAN_AP_14_age_3groups agePredictor = new PAN_AP_14_age_3groups(5, 1, "", LDRPath, "", "", "", 1);
        Age agePredictor = new Age(5, 1, "", LDRPath, "", "", "", 1);
        mLabels = agePredictor.GetLabels();
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
            prediction.sPredictedClass = GetAgeGroup(prediction.PredictedClass);
//            prediction.sConfidence = String.format("%.0f", (double)((double)Math.round(10000*prediction.Confidence)/(double)100)) + "%";
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
    
    
    private String GetAgeGroup(double age) {
        String sAgeGroup = "";
        
        if (age==0) {
            sAgeGroup = "25-"; 
        } else if (age==1) {    
            sAgeGroup = "25-35";
        } else if (age==2) {
            sAgeGroup = "35+";
        }
        
        return sAgeGroup;
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
