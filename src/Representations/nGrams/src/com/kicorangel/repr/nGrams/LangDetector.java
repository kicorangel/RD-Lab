/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.nGrams;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public class LangDetector {
    
    public LangDetector(String profileDirectory) throws Exception {
//    public void init(String profileDirectory) throws LangDetectException {
        DetectorFactory.loadProfile(profileDirectory);
    }
    
    public String Detect(String text) {
        try {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        return detector.detect();
        } catch (Exception ex) {
            return "UNDETECTED";
        }
    }
}
