package com.kicorangel.repr.nGrams;

import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.base.Tools;
import static com.kicorangel.repr.base.Tools.LoadNGrams;
import static com.kicorangel.repr.base.Tools.Preprocess;
import static com.kicorangel.repr.base.Tools.getTokens;
import com.kicorangel.repr.enumerations.PreprocessingOptions;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.gui.explorer.PreprocessPanel;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public class GenerateVectorSpaceModel {
    private ArrayList<String> mLabels;
    
    private String mNGramsPath;
    
    private ArrayList<String> mListOfTerms;
    
    private ArrayList<String> mMetaAttributes;
    private ArrayList<String> mMetaValues;
    
    private int mN;
    private int mTotal;
    private int mLength;
    private NGRAMTYPE mType;
    private PreprocessingOptions mPrepOpt;
    
    private Hashtable<String, Double> mDoc;
    
    private String mText;
    private Instance mInstance;
    
    public GenerateVectorSpaceModel(ArrayList<String> labels, String nGramsPath, int n, int total, int length, NGRAMTYPE type,  PreprocessingOptions prepOpt) throws IOException {
        new GenerateVectorSpaceModel(labels, nGramsPath, n, total, length, type, prepOpt, new ArrayList<String>());
    }
    
    public GenerateVectorSpaceModel(ArrayList<String> labels, String nGramsPath, int n, int total, int length, NGRAMTYPE type,  PreprocessingOptions prepOpt, ArrayList<String> metaAttributes) throws IOException {
        mLabels = labels;
        mN = n;
        mTotal = total;
        mLength = length;
        mPrepOpt = prepOpt;
        mNGramsPath = nGramsPath;
        mType = type;
        mMetaAttributes = metaAttributes;
        LoadListOfTerms();
    }
    
    public Instance GetInstance() {
        return mInstance;
    }
    
    public ArrayList<String> GetListOfTerms() {
        return mListOfTerms;
    }
    
    public void Clear() {
        mInstance = null;
    }
    
    public void Process(String text) throws IOException {
        Process(text, new ArrayList<String>());
    }
    
    public void Process(String text, ArrayList<String> metaValues) throws FileNotFoundException, IOException {
        mText = text;
        mMetaValues = metaValues;
        Clear();
        ProcessText();
        CreateInstance();
    }
    
    private void ProcessText() throws IOException {
        mDoc = new Hashtable<String, Double>();
        
        mText = Preprocess(mText, mPrepOpt);
        Hashtable<String, Integer> oMyNGrams = LoadNGrams(mText, mN, mLength, mType);
        
        Enumeration keys = oMyNGrams.keys();
        int iTokens = 0;
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            int iVal = oMyNGrams.get(key);
            iTokens += iVal;

            mDoc.put(key, (double)iVal);
        }
        
        keys = mDoc.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            double iVal = mDoc.get(key);
//            mDoc.put(key, iVal/iTokens);
            mDoc.put(key, (double)((double)iVal/(double)oMyNGrams.size()));
        }
    }
    
    private void LoadListOfTerms() throws FileNotFoundException, IOException {
        mListOfTerms = new ArrayList<String>();
       
        FileReader fr = null;
        try {
            fr = new FileReader(mNGramsPath);
            BufferedReader bf = new BufferedReader(fr);
            String sCadena = "";
            while ((sCadena = bf.readLine())!=null) {
                String []info = sCadena.split("\t");
                if (info.length==2) {
                    String nGram = info[0];
                    mListOfTerms.add(nGram);
                }
            }
        } catch (Exception ex) {
            
        } finally {
            if (fr!=null) { try { fr.close(); } catch (Exception ex) {} }
        }
    }
    
    
    private void CreateInstance() {
        int nAttrib = mListOfTerms.size() + mMetaAttributes.size() + 1;
        int nClasses = mLabels.size();
        int iClass = nAttrib - 1;
        try
        {
            FastVector attInfo = new FastVector(nAttrib);
            FastVector oFv = new FastVector(nClasses);
            for (int i=0;i<mListOfTerms.size();i++) {
                String sTerm = mListOfTerms.get(i);
                
                attInfo.addElement(new Attribute(sTerm));
            }
            
            for (int i=0;i<mMetaAttributes.size();i++) {
                String sMetaAttribute = mMetaAttributes.get(i);
                
                attInfo.addElement(new Attribute(sMetaAttribute));
            }
            
            for (int i=0;i<mLabels.size();i++) {
                String sLabel = mLabels.get(i);
                
                oFv.addElement(sLabel);
            }
            
            attInfo.addElement(new Attribute("class", oFv));

            Instances instances = new Instances("nGramsAttributes", attInfo, nAttrib);
            instances.setClassIndex(iClass);

            Instance inst = new DenseInstance(nAttrib);
            
            int iAttrib = 0;
            for (int i=0;i<mListOfTerms.size();i++) {
                String sTerm = mListOfTerms.get(i);
                double val = 0;
                if (mDoc.containsKey(sTerm)) {
                    val = mDoc.get(sTerm);
                }
                inst.setValue(iAttrib++, val);
            }

            for (int i=0;i<mMetaValues.size();i++) {
                String sMetaValue = mMetaValues.get(i);
                double metaVal = getDouble(sMetaValue);
                inst.setValue(iAttrib++, metaVal);
            }
//            inst.setValue(iClass, 0);
            instances.add(inst);

            mInstance = instances.instance(0);
        }
        catch (Exception ex) {
        }
    }
    
    private static double getDouble(String toDouble) {
        try {
            return Double.valueOf(toDouble);
        } catch (Exception ex) {
            return 0;
        }
    }
}
