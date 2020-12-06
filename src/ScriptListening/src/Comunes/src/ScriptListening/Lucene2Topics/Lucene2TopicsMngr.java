package ScriptListening.Lucene2Topics;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.lucene.search.BooleanClause;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author @kicorangel
 */
public class Lucene2TopicsMngr {
    public static String GetINDEXPath(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/LUCENE2TOPICS/INDEX").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sPath;
    }
       
    public static String GetTemplate(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/LUCENE2TOPICS/TEMPLATE").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sPath;
    }
    
    public static String GetCHART(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/LUCENE2TOPICS/CHART").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sPath;
    }
    
    public static ArrayList<Lucene2TopicsFilters> GetFilters(String project) {
        ArrayList<Lucene2TopicsFilters> oFilters = new ArrayList<Lucene2TopicsFilters>();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/LUCENE2TOPICS/FILTER/QUERY").evaluate(doc, XPathConstants.NODESET);
            for (int i=0;i<nodeList.getLength();i++) {
                Element element = (Element)nodeList.item(i);
                
                Lucene2TopicsFilters oFilter = new Lucene2TopicsFilters();
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
    
    public static Lucene2TopicsInfo GetChartInfo(String project) {
        Lucene2TopicsInfo oInfo = new Lucene2TopicsInfo();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            oInfo.Title = xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/TITLE").evaluate(doc);
            oInfo.SubTitle = xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/SUBTITLE").evaluate(doc);
            oInfo.Field = xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/FIELD").evaluate(doc);
            oInfo.Ini = Integer.valueOf(xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/INI").evaluate(doc));
            oInfo.End = Integer.valueOf(xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/END").evaluate(doc));
            oInfo.NTopics = Integer.valueOf(xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/NTOPICS").evaluate(doc));
            oInfo.NTerms = Integer.valueOf(xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/NTERMS").evaluate(doc));
            oInfo.MinLength = Integer.valueOf(xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/MINLENGH").evaluate(doc));
            oInfo.StopWords = xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/STOPWORDS").evaluate(doc);   
            oInfo.Vocabulary = xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/VOCABULARY").evaluate(doc);   
            oInfo.WorkingFolder = xPath.compile("/PROYECTO/LUCENE2TOPICS/INFO/WORKINGFOLDER").evaluate(doc);   
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            
        }
        
        return oInfo;
    }
}
