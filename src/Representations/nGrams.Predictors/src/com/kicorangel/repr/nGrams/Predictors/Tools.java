/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kicorangel.repr.nGrams.Predictors;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author @kicorangel
 */
public class Tools {
    public static String Normalize(String data)
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
    
    public static String GetDocumentText(String authorFile) {
        String sText = "";
        try {
            File fXmlFile = new File(authorFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            NodeList documents = doc.getDocumentElement().getElementsByTagName("document");
            for (int i = 0; i < documents.getLength(); i++) {
                try {
                    Element element = (Element) documents.item(i);
                    String sHtmlContent = element.getTextContent();
                    String sContent = com.kicorangel.repr.base.Tools.GetText(sHtmlContent);
                    sText += com.kicorangel.repr.base.Tools.Prepare(sContent) + " ";
                } catch (Exception ex) {

                }
            }
        } catch (Exception ex) {

        }
        
        return sText;
    }
}
