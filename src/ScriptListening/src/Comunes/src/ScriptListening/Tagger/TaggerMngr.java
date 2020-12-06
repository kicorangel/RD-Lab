/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ScriptListening.Tagger;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.lucene.search.BooleanClause;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author @kicorangel
 */
public class TaggerMngr {
    public static TaggerFields GetFields(String project) {
        TaggerFields oFields = new TaggerFields();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/TAGGER/LANG").evaluate(doc, XPathConstants.NODESET);
            if (nodeList.getLength()>0) {
                oFields.Lang = ((Element)nodeList.item(0)).getAttribute("process").equalsIgnoreCase("yes");
            }
            
            nodeList = (NodeList) xPath.compile("/PROYECTO/TAGGER/GENDER").evaluate(doc, XPathConstants.NODESET);
            if (nodeList.getLength()>0) {
                oFields.Gender = ((Element)nodeList.item(0)).getAttribute("process").equalsIgnoreCase("yes");
            }
            
            nodeList = (NodeList) xPath.compile("/PROYECTO/TAGGER/AGE").evaluate(doc, XPathConstants.NODESET);
            if (nodeList.getLength()>0) {
                oFields.Age = ((Element)nodeList.item(0)).getAttribute("process").equalsIgnoreCase("yes");
            }
            
            nodeList = (NodeList) xPath.compile("/PROYECTO/TAGGER/VARIETY").evaluate(doc, XPathConstants.NODESET);
            if (nodeList.getLength()>0) {
                oFields.Variety = ((Element)nodeList.item(0)).getAttribute("process").equalsIgnoreCase("yes");
            }
            
            nodeList = (NodeList) xPath.compile("/PROYECTO/TAGGER/PERSONALITY").evaluate(doc, XPathConstants.NODESET);
            if (nodeList.getLength()>0) {
                oFields.Personality = ((Element)nodeList.item(0)).getAttribute("process").equalsIgnoreCase("yes");
            }
            
            nodeList = (NodeList) xPath.compile("/PROYECTO/TAGGER/EMOTIONS").evaluate(doc, XPathConstants.NODESET);
            if (nodeList.getLength()>0) {
                oFields.Emotions = ((Element)nodeList.item(0)).getAttribute("process").equalsIgnoreCase("yes");
            }
            
            nodeList = (NodeList) xPath.compile("/PROYECTO/TAGGER/SENTIMENT").evaluate(doc, XPathConstants.NODESET);
            if (nodeList.getLength()>0) {
                oFields.Sentiment = ((Element)nodeList.item(0)).getAttribute("process").equalsIgnoreCase("yes");
            }
        } catch (Exception ex) {
            
        }
        
        return oFields;
    }
    
    public static boolean GetForce(String project) {
        boolean bForce = false;
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            bForce = xPath.compile("/PROYECTO/TAGGER/FORCE").evaluate(doc).equalsIgnoreCase("yes");
        } catch (Exception ex) {
            System.out.println();
        } finally {

        }
        
        return bForce;
    }
    
    public static String GetIndexPath(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/TAGGER/INDEX").evaluate(doc);
        } catch (Exception ex) {
            System.out.println();
        } finally {

        }
        
        return sPath;
    }
    
    public static ArrayList<TaggerFilters> GetFilters(String project) {
        ArrayList<TaggerFilters> oFilters = new ArrayList<TaggerFilters>();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/TAGGER/FILTER/QUERY").evaluate(doc, XPathConstants.NODESET);
            for (int i=0;i<nodeList.getLength();i++) {
                Element element = (Element)nodeList.item(i);
                
                TaggerFilters oFilter = new TaggerFilters();
                oFilter.Type = element.getAttribute("type");
                oFilter.Field = element.getAttribute("field");
                oFilter.Value = element.getAttribute("value");
                oFilter.Ini = element.getAttribute("ini");
                oFilter.IncludeIni = ((String)element.getAttribute("includeini")).equalsIgnoreCase("yes");
                oFilter.End = element.getAttribute("end");
                oFilter.IncludeEnd = ((String)element.getAttribute("includeend")).equalsIgnoreCase("yes");
                String sOccur = element.getAttribute("occur");
                if (sOccur.equalsIgnoreCase("MUST")) {
                    oFilter.Occur = BooleanClause.Occur.MUST;
                } else if (sOccur.equalsIgnoreCase("MUST_NOT")) {
                    oFilter.Occur = BooleanClause.Occur.MUST_NOT;
                } else {
                    oFilter.Occur = BooleanClause.Occur.SHOULD;
                }
                oFilters.add(oFilter);
            }
        } catch (Exception ex) {
            
        }
        
        return oFilters;
    }
}
