/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ScriptListening.Tagger;

import com.kicorangel.repr.ldse.GenerateVectorSpaceModel;
import ScriptListening.Tagger.Datasets.Gender;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import com.kicorangel.repr.common.Prediction;
import weka.classifiers.Classifier;
import weka.core.Instance;

/**
 *
 * @author @kicorangel
 */
public class GenderPredictor {
    private ArrayList<String> mLabels;
    private Classifier mClassifier;
    private GenerateVectorSpaceModel mVSM;
    private Hashtable<String, String> mProperNouns;
    
    private String BASEPATH = "/mnt/data/vodafone-models/gender.es.10.1.v1";
    private String LDRPath = BASEPATH;
    private String ModelPath = BASEPATH + "/es.gender.pan17.model";
    private String ProperNounsFile = BASEPATH + "/propernouns.csv";
    
    public GenderPredictor() throws IOException, Exception {
        Gender genderProfiler = new Gender(10, 1, "", LDRPath, "", "", "", 1);
        mLabels = genderProfiler.GetLabels();
        mClassifier = LoadClassifier(ModelPath);
        mVSM = new GenerateVectorSpaceModel(mLabels, LDRPath, 1, 1);
        mProperNouns = LoadProperNouns(ProperNounsFile);
    }
    
    public Prediction JointPrediction(String name, String text) {
        Prediction prediction = new Prediction();
        
        String sPrediction = PredictGenderByName(name);
        if (sPrediction.isEmpty()) {
            prediction = Predict(text);
        } else {
            prediction.sPredictedClass = sPrediction;
            prediction.sConfidence = "100%";
            prediction.Confidence = 1;
            prediction.ConfidenceValues = new double[]{1};
        }
        
        return prediction;
    }
    
    public Prediction Predict(String text) {
        Prediction prediction = new Prediction();
        
        try {
            mVSM.Process(text);
            Instance inst = mVSM.GetInstance();
            prediction.PredictedClass = mClassifier.classifyInstance(inst); 
            prediction.ConfidenceValues = mClassifier.distributionForInstance(inst);
            prediction.Confidence = prediction.ConfidenceValues[(int)prediction.PredictedClass];
            prediction.sPredictedClass = GetGenderGroup(prediction.PredictedClass);
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
    
    private String GetGenderGroup(double gender) {
        String sGenderGroup = "";
        
        if (gender==0) {
            sGenderGroup = "male"; 
        } else if (gender==1) {    
            sGenderGroup = "female";
        } 
        
        return sGenderGroup;
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
    
    
    public Hashtable<String, String> LoadProperNouns(String file) throws FileNotFoundException, IOException {
        Hashtable<String, String> oProperNouns = new Hashtable<String, String>();
        
        FileReader fr = new FileReader(file);
        BufferedReader bf = new BufferedReader(fr);
        String sLine = "";
        while ((sLine = bf.readLine())!=null) {
            String []data = sLine.split(",");
            if (data.length==2) {
                oProperNouns.put(data[0].toLowerCase(), data[1].toLowerCase());
            }
        }
        bf.close();
        fr.close();
        
        return oProperNouns;
        
    }
    public String PredictGenderByName(String name)
    {
        String sGender = "";
        
        if (mProperNouns!=null && !mProperNouns.isEmpty())
        {
            name = name.replaceAll("\\.", " ").replace("-", " ");
            String []Name = name.split(" ");
            if (Name.length>0)
            {
                int i=0;
                do  {
                    String sMainName = Name[i];
                    sMainName = Normalize(sMainName);

                    if (mProperNouns.containsKey(sMainName)) {
                        sGender = mProperNouns.get(sMainName);
                    }
                    i++;
                } while (sGender.isEmpty() && i<Name.length);
            }
        }
        
        return sGender;
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
