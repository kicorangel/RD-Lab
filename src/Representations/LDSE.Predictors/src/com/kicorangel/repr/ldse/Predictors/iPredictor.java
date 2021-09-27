/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.ldse.Predictors;

import com.kicorangel.repr.common.Prediction;
import com.kicorangel.repr.ldse.Predictors.Eval.Info;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Hashtable;

/**
 *
 * @author frangel
 */
public interface iPredictor {
    public void InstantiatePredictor(String ldsePath, String modelPath, int minFreq, int minSize, int version) throws IOException;
    public Prediction Predict(String text);
    public Hashtable<String, Info> LoadTruth(String truthPath, String lang) throws FileNotFoundException, IOException, URISyntaxException;
    
}
