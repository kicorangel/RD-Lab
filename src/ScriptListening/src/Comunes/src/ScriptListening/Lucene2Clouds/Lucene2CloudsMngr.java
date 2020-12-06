package ScriptListening.Lucene2Clouds;

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
public class Lucene2CloudsMngr {
    public static String GetINDEXPath(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/LUCENE2CLOUDS/INDEX").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sPath;
    }
    
    public static ArrayList<Lucene2CloudsFilters> GetFilters(String project) {
        ArrayList<Lucene2CloudsFilters> oFilters = new ArrayList<Lucene2CloudsFilters>();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/LUCENE2CLOUDS/FILTER/QUERY").evaluate(doc, XPathConstants.NODESET);
            for (int i=0;i<nodeList.getLength();i++) {
                Element element = (Element)nodeList.item(i);
                
                Lucene2CloudsFilters oFilter = new Lucene2CloudsFilters();
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
    
    public static Lucene2CloudsInfo GetChartInfo(String project) {
        Lucene2CloudsInfo oInfo = new Lucene2CloudsInfo();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            oInfo.Title = xPath.compile("/PROYECTO/LUCENE2CLOUDS/INFO/TITLE").evaluate(doc);
            oInfo.Field = xPath.compile("/PROYECTO/LUCENE2CLOUDS/INFO/FIELD").evaluate(doc);
            oInfo.NWords = Integer.valueOf(xPath.compile("/PROYECTO/LUCENE2CLOUDS/INFO/NWORDS").evaluate(doc));
            oInfo.StopWords = xPath.compile("/PROYECTO/LUCENE2CLOUDS/INFO/STOPWORDS").evaluate(doc);   
            oInfo.Scale = xPath.compile("/PROYECTO/LUCENE2CLOUDS/INFO/SCALE").evaluate(doc);   
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            
        }
        
        return oInfo;
    }
    
    public static String GetTemplate(String project) {
        String sTemplate = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sTemplate = xPath.compile("/PROYECTO/LUCENE2CLOUDS/TEMPLATE").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sTemplate;
    }
    
    public static String GetChartPath(String project) {
        String sChart = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sChart = xPath.compile("/PROYECTO/LUCENE2CLOUDS/CHART").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sChart;
    }
}
