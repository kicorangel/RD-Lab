package com.kicorangel.repr.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Francisco Manuel Rangel Pardo (@kicorangel) / Corex, Building Knowledge Solutions 2006
 */
public class LVI {
    public static Hashtable<String, String> LoadDocuments(String path) throws IOException {
        Hashtable<String, String> oDocs = new Hashtable<String, String>();
        
        FileReader fr = new FileReader(path + "/tf.txt");
        BufferedReader bf = new BufferedReader(fr);
        
        String sLine = "";
        while ((sLine = bf.readLine()) != null) {
            String []info = sLine.split(",");
            String sName = info[0];
            
            oDocs.put(sName, sLine);
        }
        
        return oDocs;
    }
    
    public static Hashtable<String, Integer> LoadCorporaFromData(String sData) throws FileNotFoundException, IOException {
        Hashtable<String, Integer> oCorpora = new Hashtable<String, Integer>();
        
        File directory = new File(sData);
        String [] files = directory.list();
        for (int iFile=0;iFile<files.length;iFile++) {
            String sFileName = files[iFile];
            String []fileInfo = sFileName.split("_");
            String sAuthor = fileInfo[0];
            String sLang = fileInfo[1]; sLang = sLang.replace(".xml", ""); sLang = sLang.toUpperCase();
            System.out.println("-->" + (iFile+1) + "/" + files.length);
            if (sLang.equalsIgnoreCase("PA")) {
                continue;
            }
            
            FileReader fr = new FileReader(sData + "/" + sFileName);
            BufferedReader bf = new BufferedReader(fr);
            String sLine = "";

            while ((sLine = bf.readLine())!=null){
                String []info = sLine.split(" ");
                if (info.length>3) {
                    String sLemma = info[1].toLowerCase();
                    String sPoS = String.valueOf(info[2].charAt(0));
                    
                    if (sPoS.equalsIgnoreCase("n") || sPoS.equalsIgnoreCase("v") ||
                            sPoS.equalsIgnoreCase("a") || sPoS.equalsIgnoreCase("R")) {
                        
                        int iFreq = 0;
                        if (oCorpora.containsKey(sLemma)) {
                            iFreq = oCorpora.get(sLemma);
                        }
                        oCorpora.put(sLemma, ++iFreq);
                    }
                }
            }
            
            bf.close();
            fr.close();
        }
        
        return oCorpora;
    }
    
    public static Hashtable<String, Integer> LoadCorporaFromClusters(String sClusters, boolean triplets) throws FileNotFoundException, IOException {
        Hashtable<String, Integer> oCorpora = new Hashtable<String, Integer>();
        
        for (String country : new String[]{"AR", "CL", "ES", "MX", /*"PA", */"PE"}) {
            
            String sPathToClusterInfo = sClusters + "/Hispablogs" + country + "/gen.csv";
            FileReader fr = new FileReader(sPathToClusterInfo);
            BufferedReader bf = new BufferedReader(fr);
            String sLine = "";

            while ((sLine = bf.readLine())!=null){
                Triplet oTriplet = new Triplet(sLine, triplets);
                oCorpora.put(oTriplet.Lemma1, 0);
                if (triplets) {
                    oCorpora.put(oTriplet.Lemma2, 0);
                }
            }
            
            bf.close();
            fr.close();
        }
        
        return oCorpora;
    }
    
    public static ArrayList<String> SerializeTerms(Hashtable<String, Integer> oTerms, int minFreq ){
        ArrayList<String> oListOfTerms = new ArrayList<String>(oTerms.keySet());
        Collections.sort(oListOfTerms);
        
//        Enumeration terms = oTerms.keys();
//        while (terms.hasMoreElements()) {
//            String sTerm = (String)terms.nextElement();
//            oListOfTerms.add(sTerm);
//        }
        
        for (int i=oListOfTerms.size()-1;i>=0;i--) {
            String sTerm = oListOfTerms.get(i);
            int iFreq = oTerms.get(sTerm);
            if (iFreq<minFreq) {
                oListOfTerms.remove(i);
            }
        }
        
        return oListOfTerms;
    }
    
    public static Hashtable<String, Double> LoadIDF(String path) throws IOException {
        Hashtable<String, Double> oIDF = new Hashtable<String, Double>();
        
        FileReader fr = new FileReader(path + "/idf.txt");
        BufferedReader bf = new BufferedReader(fr);
        
        String sLine = "";
        while ((sLine = bf.readLine()) != null) {
            String []info = sLine.split(":::");
            String sTerm = info[0];
            double sFreq = Double.valueOf(info[1]);
            
            oIDF.put(sTerm, sFreq);
        }
        
        return oIDF;
    }
    
    public static Hashtable<String, String> LoadWeights(String path) throws IOException {
        Hashtable<String, String> oDocs = new Hashtable<String, String>();
        
        FileReader fr = new FileReader(path + "/weights.txt");
        BufferedReader bf = new BufferedReader(fr);
        
        String sLine = "";
        while ((sLine = bf.readLine()) != null) {
            String []info = sLine.split(",");
            String sName = info[0];
            
            oDocs.put(sName, sLine);
        }
        
        return oDocs;
    }
    
    public static Hashtable<String, Hashtable<String, Double>>LoadTxCat(String path) throws FileNotFoundException, IOException {
        Hashtable<String, Hashtable<String, Double>> oTxCat = new Hashtable<String, Hashtable<String, Double>>();
        for (String country : new String[]{"AR", "CL", "ES", "MX", /*"PA", */"PE"}) {
            Hashtable<String, Double> oTx = new Hashtable<String, Double>();
            String sPathToClusterInfo = path + "/tx_" + country.toLowerCase() + ".txt";
            FileReader fr = new FileReader(sPathToClusterInfo);
            BufferedReader bf = new BufferedReader(fr);
            String sLine = "";

            while ((sLine = bf.readLine())!=null){
                String []info = sLine.split(":::");
                oTx.put(info[0], Double.valueOf(info[1]));
            }
            oTxCat.put(country, oTx);
        }
        return oTxCat;
    }
    
    
    public static Hashtable<String, Hashtable<String, Double>>LoadProbabilities(String path) throws FileNotFoundException, IOException {
        Hashtable<String, Hashtable<String, Double>> oProbCat = new Hashtable<String, Hashtable<String, Double>>();
        for (String country : new String[]{"AR", "CL", "ES", "MX", /*"PA", */"PE"}) {
            Hashtable<String, Double> oProb = new Hashtable<String, Double>();
            String sPathToClusterInfo = path + "/prob_" + country.toLowerCase() + ".txt";
            FileReader fr = new FileReader(sPathToClusterInfo);
            BufferedReader bf = new BufferedReader(fr);
            String sLine = "";

            while ((sLine = bf.readLine())!=null){
                String []info = sLine.split(":::");
                oProb.put(info[0], Double.valueOf(info[1]));
            }
            oProbCat.put(country, oProb);
        }
        return oProbCat;
    }
    
    public static void GenerateTFIDF(Hashtable<String, Integer>oTerms, ArrayList<String>oListOfTerms, String sInput, String sOutput, boolean verbose) throws FileNotFoundException, IOException {
        FileWriter oFW = new FileWriter(sOutput + "/tf.txt");
        
        File directory = new File(sInput);
        String [] files = directory.list();
        for (int iFile=0;iFile<files.length;iFile++) {
            String sFileName = files[iFile];
            String []fileInfo = sFileName.split("_");
            String sAuthor = fileInfo[0];
            String sLang = fileInfo[1]; sLang = sLang.replace(".xml", ""); sLang = sLang.toUpperCase();
            
            if (sLang.equalsIgnoreCase("PA")) {
                continue;
            }
            
            if (verbose) {
                System.out.println("-->" + (iFile+1) + "/" + files.length);
            }
            
            Hashtable<String, Integer> oFreq = new Hashtable<String, Integer>();
            
            FileReader fr = new FileReader(sInput + "/" + sFileName);
            BufferedReader bf = new BufferedReader(fr);
            String sLine = "";

            while ((sLine = bf.readLine())!=null){
                String []info = sLine.split(" ");
                if (info.length>3) {
                    String sLemma = info[1].toLowerCase();
                    String sPoS = String.valueOf(info[2].charAt(0));
                    
                    if (sPoS.equalsIgnoreCase("n") || sPoS.equalsIgnoreCase("v") ||
                            sPoS.equalsIgnoreCase("a") || sPoS.equalsIgnoreCase("R")) {
                        
                        int iFreq = 0;
                        if (oFreq.containsKey(sLemma)) {
                            iFreq = oFreq.get(sLemma);
                        }
                        iFreq++;
                        oFreq.put(sLemma, iFreq);
                    }
                }
            }
            
            bf.close();
            fr.close();
            
            Hashtable<String, Integer> oDoc = new Hashtable<String, Integer>();
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                if (oFreq.containsKey(sTerm)) {
                    int iDocFreq = oFreq.get(sTerm);
                    int iCorpusFreq = oTerms.get(sTerm);
                    iCorpusFreq += iDocFreq;
                    
                    oDoc.put(sTerm, iDocFreq);
                    oTerms.put(sTerm, iCorpusFreq);
                }
            }
            
            // Write doc frequencies: list of tf
            StringBuilder Line = new StringBuilder();
            sLine = sAuthor + "," + sLang + ",";
            int length = oListOfTerms.size();
            for (int i=0;i<length;i++) {
                String sTerm = oListOfTerms.get(i);
                if (oDoc.containsKey(sTerm)) {
                    int iFreq = oDoc.get(sTerm);
                    sLine += iFreq;
                } else {
                    sLine += "0";
                }
                
//                if (oDoc.containsKey(sTerm)) {
//                    int iFreq = oDoc.get(sTerm);
//                    Line.append(iFreq);
//                } else {
//                    Line.append("0");
//                }
                
                if (i<oListOfTerms.size()-1) {
                    sLine += ",";
                } else {
                    sLine += "\n";
                }
                
//                if (i<length-1) {
//                    Line.append(",");
//                } else {
//                    Line.append("\n");
//                }
            }
//            sLine += Line.toString();
            oFW.write(sLine);
            oFW.flush();
        }
        oFW.close();
        
        // Write corpus frequencies: list of idf
        oFW = new FileWriter(sOutput + "/idf.txt");
        for (int i=0;i<oListOfTerms.size();i++) {
            String sTerm = oListOfTerms.get(i);
            int iFreq = oTerms.get(sTerm);
            oFW.write(sTerm + ":::" + iFreq + "\n");
        }
        oFW.close();
    }
    
    public static void GenerateWeights(Hashtable<String, String> oDocs, Hashtable<String, Integer>oTerms, ArrayList<String>oListOfTerms, String sOutput, boolean verbose) throws IOException {
        int N = oDocs.size();
        Enumeration docs = oDocs.keys();
        FileWriter oFW = new FileWriter(sOutput + "/weights.txt");
        int iFile = 0;
        while (docs.hasMoreElements()) {
            String sDoc = (String)docs.nextElement();
            String sInfo = oDocs.get(sDoc);
            String []info = sInfo.split(",");
            String sAuthor = info[0];
            String sLang = info[1];
            
            if (verbose) {
                System.out.println("-->" + (iFile+1) + "/" + N);
                iFile++;
            }
            
            StringBuilder Weight = new StringBuilder();
            String sWeight = sAuthor + "," + sLang + ",";
            int length = oListOfTerms.size();
            for (int i=0;i<length;i++) {
                String sTerm = oListOfTerms.get(i);
                double iTF = Double.valueOf(info[i+2]);
                double iIDF = oTerms.get(sTerm);
                
                double weight = Math.log(iTF+1) * Math.log(N / (1+iIDF));
                
//                sWeight += weight;
//                if (i<oTerms.size()-1) {
//                    sWeight += ",";
//                } else {
//                    sWeight += "\n";
//                }
                Weight.append(weight);
                if (i<length-1) {
                    Weight.append(",");
                } else {
                    Weight.append("\n");
                }
            }
            sWeight += Weight.toString();
            oFW.write(sWeight);
            oFW.flush();
        }
        oFW.close();
    }
    
    public static Hashtable<String, ArrayList<String>> GenerateMoments(Hashtable<String, String> oWeights, int order, boolean verbose) {
        Hashtable<String, ArrayList<String>> oMoments = new Hashtable<String, ArrayList<String>>();
        
        int iDoc = 0;
        Enumeration weights = oWeights.keys();
        while (weights.hasMoreElements()) {
            String sDoc = (String)weights.nextElement();
            String sInfo = oWeights.get(sDoc);
            String []info = sInfo.split(",");
            
            if (verbose) {
                System.out.println("-->" + (++iDoc) + "/" + oWeights.size());
            }
            
            double x_ = 0;
            double N = 0;
            for (int i=2;i<info.length;i++) {
                x_ += Double.parseDouble(info[i]);
                N++;
            }
            x_ = x_ / N;
            
            ArrayList<String>oMh = new ArrayList<String>();
            for (int h=0;h<order;h++) {
                double mh = 0;
                for (int i=2;i<info.length;i++) {
                    double xi = Double.parseDouble(info[i]);
                    
                    mh += Math.pow(xi-x_,h) / N;
                }
                oMh.add(String.format("%f", mh));
            }
            oMoments.put(sDoc, oMh);
        }
        
        return oMoments;
    }
    
//    public static void GenerateTX(Hashtable<String, String> oDocs, ArrayList<String>oListOfTerms, Hashtable<String, String> oWeights, String sOutput, boolean verbose) throws IOException {
//        Hashtable<String, Hashtable<String, Double>> oTxCat = new Hashtable<String, Hashtable<String, Double>>();
//        Hashtable<String, 
//    
//    }
    
    public static ArrayList<AuthorWeight> GetAuthorWeightsFromDocs(Hashtable<String, String> oDocs, Hashtable<String, String>oWeights, ArrayList<String>oListOfTerms) {
        ArrayList<AuthorWeight> authorWeight = new ArrayList<AuthorWeight>();
        
        Enumeration docs = oWeights.keys();
        while (docs.hasMoreElements()) {
                String sDoc = (String)docs.nextElement();
                String sInfo = oDocs.get(sDoc);
                String []info = sInfo.split(",");
                
                AuthorWeight oWeight = new AuthorWeight();
                oWeight.Author = info[0];
                oWeight.Lang = info[1];
                
                for (int i=0;i<oListOfTerms.size();i++) {
//                    oWeight.WordWeight.put(oListOfTerms.get(i), Double.valueOf(info[i]));
                    oWeight.ListOfWeights.add(Double.valueOf(info[i+2]));
                }
                
                authorWeight.add(oWeight);
        }
        
         return authorWeight;
    }
    
    public static void GenerateTX3(Hashtable<String, String> oDocs, ArrayList<String>oListOfTerms, Hashtable<String, String> oWeights, String sOutput, boolean verbose) throws IOException {
        Hashtable<String, Hashtable<String, Double>> oTxCat = new Hashtable<String, Hashtable<String, Double>>();
        Enumeration docs = oWeights.keys();
            
        int iDoc = 1;
        while (docs.hasMoreElements()) {
            if (verbose) {
//                    System.out.println("-->" + sTerm + " " + (i+1) + "/" + oListOfTerms.size());
                System.out.println("-->" + iDoc + "/" + oWeights.size());
                iDoc++;
            }
            String sDoc = (String)docs.nextElement();
            String sInfo = oDocs.get(sDoc);
            String []info = sInfo.split(",");
            String sAuthor = info[0];
            String sLang = info[1];
//                double weight = Double.valueOf(info[i+2]);
                
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                double weight = Double.valueOf(info[i+2]);

                Hashtable<String, Double>oTx = new Hashtable<String, Double>();
                if (oTxCat.containsKey(sLang)) {
                    oTx = oTxCat.get(sLang);
                }
                double t = 0;
                if (oTx.containsKey(sTerm)) {
                    t = oTx.get(sTerm);
                }
                t += weight;
                oTx.put(sTerm, t);
                oTxCat.put(sLang, oTx);
            }
        }
        
        for (String country : new String[]{"AR", "CL", "ES", "MX", /*"PA", */"PE"}) {
            FileWriter oFW = new FileWriter(sOutput + "/tx_" + country.toLowerCase() + ".txt");
            Hashtable<String, Double>oTx = oTxCat.get(country);
            
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                double iAccWeight = 0;
                if (oTx.containsKey(sTerm)) {
                    iAccWeight = oTx.get(sTerm);
                } 
                oFW.write(sTerm + ":::" + iAccWeight + "\n");
//                oFW.flush();
            }
            oFW.close();
        }
    }
    
    public static void GenerateTX2(Hashtable<String, String> oDocs, ArrayList<String>oListOfTerms, Hashtable<String, String> oWeights, String sOutput, boolean verbose) throws IOException {
        Hashtable<String, Hashtable<String, Double>> oTxCat = new Hashtable<String, Hashtable<String, Double>>();
        ArrayList<AuthorWeight> oAuthorWeight = GetAuthorWeightsFromDocs(oDocs, oWeights, oListOfTerms);
        for (int i=0;i<oListOfTerms.size();i++) {
            String sTerm = oListOfTerms.get(i);
            if (verbose) {
                System.out.println("-->" + sTerm + " " + (i+1) + "/" + oListOfTerms.size());
            }
            int iLength = oAuthorWeight.size();
            for (int a=0;a<iLength;a++) {
                AuthorWeight oWeight = oAuthorWeight.get(a);
                String sAuthor = oWeight.Author;
                String sLang = oWeight.Lang;
//                double weight = oWeight.WordWeight.get(sTerm);
                double weight = oWeight.ListOfWeights.get(i);
                Hashtable<String, Double>oTx = new Hashtable<String, Double>();
                if (oTxCat.containsKey(sLang)) {
                    oTx = oTxCat.get(sLang);
                }
                double t = 0;
                if (oTx.containsKey(sTerm)) {
                    t = oTx.get(sTerm);
                }
                t += weight;
                oTx.put(sTerm, t);
                oTxCat.put(sLang, oTx);
            }
        }
        
        for (String country : new String[]{"AR", "CL", "ES", "MX", /*"PA", */"PE"}) {
            FileWriter oFW = new FileWriter(sOutput + "/tx_" + country.toLowerCase() + ".txt");
            Hashtable<String, Double>oTx = oTxCat.get(country);
            
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                double iAccWeight = 0;
                if (oTx.containsKey(sTerm)) {
                    iAccWeight = oTx.get(sTerm);
                } 
                oFW.write(sTerm + ":::" + iAccWeight + "\n");
//                oFW.flush();
            }
            oFW.close();
        }
    }
    
    public static void GenerateT1(Hashtable<String, String> oDocs, ArrayList<String>oListOfTerms, Hashtable<String, String> oWeights, String sOutput, boolean verbose) throws IOException {
        Hashtable<String, Hashtable<String, Double>> oTxCat = new Hashtable<String, Hashtable<String, Double>>();
        
        for (int i=0;i<oListOfTerms.size();i++) {
            String sTerm = oListOfTerms.get(i);
            Enumeration docs = oWeights.keys();
            
            if (verbose) {
                    System.out.println("-->" + sTerm + " " + (i+1) + "/" + oListOfTerms.size());
                }
            
            while (docs.hasMoreElements()) {
                String sDoc = (String)docs.nextElement();
                String sInfo = oDocs.get(sDoc);
                String []info = sInfo.split(",");
                String sAuthor = info[0];
                String sLang = info[1];
                double weight = Double.valueOf(info[i+2]);
                
                
                
                Hashtable<String, Double>oTx = new Hashtable<String, Double>();
                if (oTxCat.containsKey(sLang)) {
                    oTx = oTxCat.get(sLang);
                }
                double t = 0;
                if (oTx.containsKey(sTerm)) {
                    t = oTx.get(sTerm);
                }
                t += weight;
                oTx.put(sTerm, t);
                oTxCat.put(sLang, oTx);
            }
            
//            if (i>100) {
//                break;
//            }
        }
        
        for (String country : new String[]{"AR", "CL", "ES", "MX", /*"PA", */"PE"}) {
            FileWriter oFW = new FileWriter(sOutput + "/tx_" + country.toLowerCase() + ".txt");
            Hashtable<String, Double>oTx = oTxCat.get(country);
            
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                double iAccWeight = 0;
                if (oTx.containsKey(sTerm)) {
                    iAccWeight = oTx.get(sTerm);
                } 
                oFW.write(sTerm + ":::" + iAccWeight + "\n");
//                oFW.flush();
            }
            oFW.close();
        }
    }
    
    public static void GenerateProbabilities(ArrayList<String>oListOfTerms, Hashtable<String, Hashtable<String, Double>> oTxCat, String sOutput, boolean verbose) throws IOException {
        Hashtable<String, Hashtable<String, Double>> oProbCat = CalculateProbabilities(oListOfTerms, oTxCat);
        for (String country : new String[]{"AR", "CL", "ES", "MX", /*"PA", */"PE"}) {
            FileWriter oFW = new FileWriter(sOutput + "/prob_" + country.toLowerCase() + ".txt");
            Hashtable<String, Double>oProb = oProbCat.get(country);
            
            if (verbose) {
                System.out.println("-->" + country);
            }
            
            for (int i=0;i<oListOfTerms.size();i++) {
                String sTerm = oListOfTerms.get(i);
                double iProb = 0;
                if (oProb.containsKey(sTerm)) {
                    iProb = oProb.get(sTerm);
                } 
                oFW.write(sTerm + ":::" + iProb + "\n");
                oFW.flush();
            }
            oFW.close();
        }
    }
    
    private static Hashtable<String, Hashtable<String, Double>> CalculateProbabilities(ArrayList<String> oListOfTerms, Hashtable<String, Hashtable<String, Double>> oTxCat) {
        Hashtable <String, Hashtable<String, Double>> oProbCat = new Hashtable<String, Hashtable<String, Double>>();
        
        for (int i=0;i<oListOfTerms.size();i++) {
            String sTerm = oListOfTerms.get(i);
            for (String countryTarget : new String []{"AR", "CL", "ES", "MX", /*"PA", */"PE"}) {
                Hashtable<String, Double>oTxTarget = oTxCat.get(countryTarget);
                double ta = 0;
                double tc = 0;
                if (oTxTarget.containsKey(sTerm)) {
                    ta = oTxTarget.get(sTerm);
                }
                for (String countryOther : new String []{"AR", "CL", "ES", "MX", /*"PA", */"PE"}) {
                    if (countryTarget.equalsIgnoreCase(countryOther)) {
                        continue;
                    }
                    Hashtable<String, Double>oTxOther = oTxCat.get(countryOther);
                    
                    if (oTxOther.containsKey(sTerm)) {
                        tc += oTxOther.get(sTerm);
                    }
                }
                double prob = 0;
                if (ta+tc>0) {
                    prob = ta / (ta + tc);
                } else {
                    prob = ta / (ta + tc + 1);
                }
                Hashtable<String, Double> oProbTerm = new Hashtable<String, Double>();
                if (oProbCat.containsKey(countryTarget)) {
                    oProbTerm = oProbCat.get(countryTarget);
                }
                oProbTerm.put(sTerm, prob);
                oProbCat.put(countryTarget, oProbTerm);
            }
        }
        
        return oProbCat;
    }
}
