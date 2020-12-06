/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.JSON2Lucene;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author @kicorangel
 */


public class JSON2LuceneMngr {
    public static ArrayList<JSON2LuceneFields> GetFields(String project) {
        ArrayList<JSON2LuceneFields> oFields = new ArrayList<JSON2LuceneFields>();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/JSON2LUCENE/CAMPOS/CAMPO").evaluate(doc, XPathConstants.NODESET);
            for (int i=0;i<nodeList.getLength();i++) {
                Element element = (Element)nodeList.item(i);
                
                JSON2LuceneFields oField = new JSON2LuceneFields();
                oField.Process = ((String)element.getAttribute("procesar")).equalsIgnoreCase("si");
                oField.Field = element.getAttribute("nombre");
                oField.IndexField = element.getAttribute("indice");
                oField.Store = GetStore(element.getAttribute("store"));
                oField.Index = GetIndex(element.getAttribute("index"));
                oField.TermVector = GetTermVector(element.getAttribute("termvector"));
                oField.Convert = element.getAttribute("convert");
                oField.Unique = ((String)element.getAttribute("unique")).equalsIgnoreCase("yes");
                oFields.add(oField);
            }
        } catch (Exception ex) {
            
        }
        
        return oFields;
    }
    
    public static String GetJSONPath(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/JSON2LUCENE/JSON").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sPath;
    }
    
    public static String GetIndexPath(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/JSON2LUCENE/INDEX").evaluate(doc);
        } catch (Exception ex) {
            System.out.println();
        } finally {

        }
        
        return sPath;
    }
    
    private static Store GetStore(String store) {
        if (store.equalsIgnoreCase("yes")) {
            return Store.YES;
        } else {
            return Store.NO;
        }
    }
    
    private static Index GetIndex(String index) {
        if (index.equalsIgnoreCase("analyzed")) {
            return Index.ANALYZED;
        } else if (index.equalsIgnoreCase("analyzed_no_norms")) {
            return Index.ANALYZED_NO_NORMS;
        } else if (index.equalsIgnoreCase("no")) {
            return Index.NO;
        } else if (index.equalsIgnoreCase("not_analyzed")) {
            return Index.NOT_ANALYZED;
        } else {
            return Index.NOT_ANALYZED_NO_NORMS;
        }
    }
    
    private static TermVector GetTermVector(String termVector) {
        if (termVector.equalsIgnoreCase("no")) {
            return TermVector.NO;
        } else if (termVector.equalsIgnoreCase("with_offsets")) {
            return TermVector.WITH_OFFSETS;
        } else if (termVector.equalsIgnoreCase("with_positions")) {
            return TermVector.WITH_POSITIONS;
        } else if (termVector.equalsIgnoreCase("with_positions_offsets")) {
            return TermVector.WITH_POSITIONS_OFFSETS;
        } else {
            return TermVector.YES;
        }
    }
    
    public static double GetMinDate(String project) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        double minDate = 0;
        
        String sFileName = "../proyectos/" + project + ".xml";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(sFileName);

        XPath xPath =  XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/JSON2LUCENE/DATES").evaluate(doc, XPathConstants.NODESET);
        if (nodeList.getLength()>0) {
            minDate = Double.valueOf(((Element)nodeList.item(0)).getAttribute("min"));
        }
        return minDate;
    }
    
    public static double GetMaxDate(String project) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        double maxDate = 99999999;
        
        String sFileName = "../proyectos/" + project + ".xml";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(sFileName);

        XPath xPath =  XPathFactory.newInstance().newXPath();
        NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/JSON2LUCENE/DATES").evaluate(doc, XPathConstants.NODESET);
        if (nodeList.getLength()>0) {
            maxDate = Double.valueOf(((Element)nodeList.item(0)).getAttribute("max"));
        }
        return maxDate;
    }
}
