/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.nGrams.Predictors;

import com.kicorangel.repr.common.Prediction;
import com.kicorangel.repr.enumerations.NGRAMTYPE;
import com.kicorangel.repr.nGrams.Predictors.Eval.Info;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author frangel
 */
public interface iPredictor {
    public void InstantiatePredictor(String nGramsPath, String nGramsFile, String modelPath, NGRAMTYPE nGramsType, int n, int total, int length) throws IOException;
    public Prediction Predict(String text, ArrayList<String> metaData);
    public Hashtable<String, Info> LoadTruth(String truthPath, String lang) throws FileNotFoundException, IOException, URISyntaxException;
    public Hashtable<String, String> LoadMeta(String metaPath) throws FileNotFoundException, IOException;
}
