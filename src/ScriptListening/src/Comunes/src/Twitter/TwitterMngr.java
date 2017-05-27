package Twitter;

import comunes.Tools;
import java.io.FileReader;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TwitterMngr {
    public static TwitterKeys GetKeys(String project) {
        TwitterKeys oKeys = new TwitterKeys();

        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            oKeys.KEY = xPath.compile("/PROYECTO/TWITTER/KEY").evaluate(doc);
            oKeys.SECRET = xPath.compile("/PROYECTO/TWITTER/SECRET").evaluate(doc);
            oKeys.ACCESSTOKEN = xPath.compile("/PROYECTO/TWITTER/ACCESSTOKEN").evaluate(doc);
            oKeys.ACCESSSECRET = xPath.compile("/PROYECTO/TWITTER/ACCESSSECRET").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return oKeys;
    }
    
    /*
     * TWITTER-BUSQUEDA
     */
    
    public static ArrayList<TwitterQuery> GetQueries(String project) {
        ArrayList<TwitterQuery> oQueries = new ArrayList<TwitterQuery>();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/TWITTER-BUSQUEDA/BUSQUEDAS/BUSQUEDA").evaluate(doc, XPathConstants.NODESET);
            for (int i=0;i<nodeList.getLength();i++) {
                Element element = (Element)nodeList.item(i);
                
                TwitterQuery oQuery = new TwitterQuery();
                oQuery.Query = element.getAttribute("clave");
                oQuery.Latitude = Tools.GetDouble(element.getAttribute("latitud"));
                oQuery.Longitude = Tools.GetDouble(element.getAttribute("longitud"));
                oQuery.Radius = Tools.GetDouble(element.getAttribute("radio"));
                oQuery.NumResults = Tools.GetInt(element.getAttribute("resultados"));
                oQueries.add(oQuery);
            }
        } catch (Exception ex) {
            
        }
        
        return oQueries;
    }
    
    public static String GetQueriesPath(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/TWITTER-BUSQUEDA/RUTA").evaluate(doc);
        } catch (Exception ex) {
            System.out.println();
        } finally {

        }
        
        return sPath;
    }
    
    /*
     * TWITTER-TIMELINES
     */
    public static String GetTimelinesPath(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/TWITTER-TIMELINE/RUTA").evaluate(doc);
        } catch (Exception ex) {
            System.out.println();
        } finally {

        }
        
        return sPath;
    }
    
    public static ArrayList<TwitterTimeline> GetTimelines(String project) {
        ArrayList<TwitterTimeline> oTimelines = new ArrayList<TwitterTimeline>();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/TWITTER-TIMELINE/TIMELINES/TIMELINE").evaluate(doc, XPathConstants.NODESET);
            for (int i=0;i<nodeList.getLength();i++) {
                Element element = (Element)nodeList.item(i);
                
                TwitterTimeline oTimeline = new TwitterTimeline();
                oTimeline.UserName = element.getAttribute("usuario");
                oTimeline.NumResults = Tools.GetInt(element.getAttribute("resultados"));
                oTimelines.add(oTimeline);
            }
        } catch (Exception ex) {
            
        }
        
        return oTimelines;
    }
}
