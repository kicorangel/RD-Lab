/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.autoritas.repr.ldr.datasets;

import com.autoritas.repr.ldr.GenerateArff;
import com.autoritas.repr.ldr.Prepare;
import com.autoritas.repr.ldr.Tools;
import com.autoritas.repr.ldr.iLoadDocsxClass;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Francisco Rangel (@kicorangel), Autoritas Consulting S.A. 2017
 */
public class HCE_HIL implements iLoadDocsxClass  {
    private int mMinFreq;
    private int mMinSize;
    private String msCorpusPath;
    private String msLDRPath;
    private String msTrainingArff;
    private String msTestPath;
    private String msTestArff;
    private String mServicio;
    
    public HCE_HIL(int minFreq, int minSize, String corpusPath, String LDRPath, String trainingArff, String testPath, String testArff, String servicio) {
        mMinFreq = minFreq;
        mMinSize = minSize;
        msCorpusPath = corpusPath;
        msLDRPath = LDRPath;
        msTrainingArff = trainingArff;
        msTestPath = testPath;
        msTestArff = testArff;
        mServicio = servicio;
    }
    
    
    public void Run() throws IOException, FileNotFoundException, ParserConfigurationException, SAXException {
        ArrayList<String> Labels = GetLabels(mServicio);
        // Step 1: Prepare LDR weights and probabilities
        {
            Prepare oPrepare = new Prepare(mMinFreq, mMinSize, Labels, this, msCorpusPath, msLDRPath, true);
            oPrepare.Process();
        }   
        
        // Step 2: Generate training ARFF
        {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, Labels,  this, msCorpusPath, msLDRPath, "", msTrainingArff, true);
            oGenerate.Process();
        }
        
        // Step 3: Generate test ARFF
        {
            GenerateArff oGenerate = new GenerateArff(mMinFreq, mMinSize, Labels, this, msCorpusPath, msLDRPath, msTestPath, msTestArff, true);
            oGenerate.Process();
        }
    }
    
    

    public ArrayList<String> GetLabels(String sData) throws FileNotFoundException, IOException {
        ArrayList<String> Labels = new ArrayList<String>();
        Hashtable<String, String> oLabels = new Hashtable<String, String>();
        BufferedReader br = new BufferedReader(new FileReader(sData));
        String line = "";
        while ((line = br.readLine()) != null) {
            if (line.startsWith("2015")) {
                String []data = line.split(";");
                if (data[2].equalsIgnoreCase(mServicio) &&
                        !line.contains("Cierre por caducidad administrativa") &&
                        !data[3].equalsIgnoreCase("999.9") &&
                        !data[3].isEmpty()) {
                    oLabels.put(data[3], data[3]);
                }
            }
        }
        br.close();
        
        Enumeration labels = oLabels.keys();
        while (labels.hasMoreElements()) {
            String sLabel = (String)labels.nextElement();
            Labels.add(sLabel);
        }
        
        return Labels;
    }
    
    public Hashtable<String, Hashtable<String, Integer>> LoadDocsxClass(String sData, String label, int minSize, boolean verbose) {
        Hashtable<String, Hashtable<String, Integer>> oDocs = new Hashtable<String, Hashtable<String, Integer>>();
        Hashtable<String, String> Informes = new Hashtable<String, String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(sData));
            String sText = "";
            String sState = "", sLastState = "";
            String FechaAlta = "", Servicio = "", CodDiagnostico = "", Cierre = "";
            String Motivos = "", Antecedentes = "", Enfermedad = "", Exploracion = "", Examenes = "", Impresion = "", Tratamiento = "";
            
            int iInforme = 0;
            while ((sText = br.readLine()) != null) {
                
                sState = GetStatus(sText, sState);

                if (sState.equalsIgnoreCase("INITIALISE")) {
                    String[] info = sText.split(";");

                    if (!FechaAlta.isEmpty()) {
                       // aquí tengo el informe
                        if (!Cierre.contains("caducidad") &&
                                !CodDiagnostico.isEmpty() &&
                                !CodDiagnostico.equalsIgnoreCase("999.9") &&
                                Servicio.equalsIgnoreCase(mServicio) &&
                                label.equalsIgnoreCase(CodDiagnostico)) {
                            Informes.put("i" + iInforme, Motivos);                            
                        }
                    }

                    // 
                    FechaAlta = info[0];
                    Servicio = info[2];
                    CodDiagnostico = info[3];
                    Motivos = "";
                    Antecedentes = "";
                    Enfermedad = "";
                    Exploracion = "";
                    Examenes = "";
                    Impresion = "";
                    Tratamiento = "";
                    Cierre = "";
                    iInforme++;
                    
                    if (info.length >= 9) {
                        if (info[8].equalsIgnoreCase("Cierre por caducidad administrativa")) {
                            Cierre = info[8];
                        }
                    } else {
                        String s = "";
                    }

                    sState = "MOTIVOS";
                } else if (sState.equalsIgnoreCase("MOTIVOS")) {
                    Motivos += sText + "\n";
                } else if (sState.equalsIgnoreCase("ANTECEDENTES")) {
                    if (sState.equalsIgnoreCase(sLastState)) {
                        Antecedentes += sText + "\n";
                    }
                } else if (sState.equalsIgnoreCase("ENFERMEDAD")) {
                    if (sState.equalsIgnoreCase(sLastState)) {
                        Enfermedad += sText + "\n";
                    }
                } else if (sState.equalsIgnoreCase("EXPLORACION")) {
                    if (sState.equalsIgnoreCase(sLastState)) {
                        Exploracion += sText + "\n";
                    }
                } else if (sState.equalsIgnoreCase("EXAMENES")) {
                    if (sState.equalsIgnoreCase(sLastState)) {
                        Examenes += sText + "\n";
                    }
                } else if (sState.equalsIgnoreCase("IMPRESION")) {
                    if (sState.equalsIgnoreCase(sLastState)) {
                        Impresion += sText + "\n";
                    }
                } else if (sState.equalsIgnoreCase("TRATAMIENTO")) {
                    if (sState.equalsIgnoreCase(sLastState)) {
                        Tratamiento += sText + "\n";
                    }
                } else if (sState.equalsIgnoreCase("CIERRE_CADUCIDAD_ADMINISTRATIVA")) {
                    if (sState.equalsIgnoreCase(sLastState)) {
                        Cierre += sText + "\n";
                    }
                }

                sLastState = sState;
                
            }
        } catch (Exception ex) {
            
        } finally {
            if (br!=null) { try {br.close();} catch (Exception ex) {} }
        }
        
        
        Enumeration informe = Informes.keys();
        while (informe.hasMoreElements()) {
            String sId = (String)informe.nextElement();
            String sText = Informes.get(sId);
            sText = Tools.Prepare(sText);
            String[] tokens = sText.split(" ");
            for (String t : tokens) {
                String sLemma = t.toLowerCase().trim();

                if (sLemma.length() < minSize) {
                    continue;
                }
                Hashtable<String, Integer> oFreq = new Hashtable<String, Integer>();
                if (oDocs.containsKey(sId)) {
                    oFreq = oDocs.get(sId);
                }
                int iFreq = 0;
                if (oFreq.containsKey(sLemma)) {
                    iFreq = oFreq.get(sLemma);
                }
                oFreq.put(sLemma, ++iFreq);
                oDocs.put(sId, oFreq);
            }
        }
        
        return oDocs;
    }
    
    private static String GetStatus(String text, String status) {
        if (StartsWithDate(text)) {
            status = "INITIALISE";
        } else if (text.startsWith("ANTECEDENTES PERSONALES:")) {
            status = "ANTECEDENTES";
        } else if (text.startsWith("ENFERMEDAD ACTUAL:")) {
            status = "ENFERMEDAD";
        } else if (text.startsWith("EXPLORACIÓN FÍSICA:")) {
            status = "EXPLORACION";
        } else if (text.startsWith("EXÁMENES COMPLEMENTARIOS:")) {
            status = "EXAMENES";
        } else if (text.startsWith("IMPRESIÓN DIAGNÓSTICA:")) {
            status = "IMPRESION";
        } else if (text.startsWith("TRATAMIENTO Y RECOMENDACIONES:")) {
            status = "TRATAMIENTO";
        }

        return status;
    }
    
    private static boolean StartsWithDate(String text) {
        boolean bStartsWithDate = false;

        String[] data = text.split(";");

        if (data.length > 1) {
            if (data[0].matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}$")) { // http://stackoverflow.com/questions/4463867/java-regular-expression-match
                bStartsWithDate = true;
            }
        }

        return bStartsWithDate;
    }
}
