package ScriptListening.Lucene2Charts;

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
public class Lucene2ChartsMngr {
    public static String GetINDEXPath(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/LUCENE2CHARTS/INDEX").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sPath;
    }
    
    public static ArrayList<Lucene2ChartsFilters> GetFilters(String project) {
        ArrayList<Lucene2ChartsFilters> oFilters = new ArrayList<Lucene2ChartsFilters>();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/LUCENE2CHARTS/FILTER/QUERY").evaluate(doc, XPathConstants.NODESET);
            for (int i=0;i<nodeList.getLength();i++) {
                Element element = (Element)nodeList.item(i);
                
                Lucene2ChartsFilters oFilter = new Lucene2ChartsFilters();
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
    
//    public static int GetRange(String project, String side) {
//        int range = 0;
//        try {
//            String sFileName = "../proyectos/" + project + ".xml";
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(sFileName);
//            XPath xPath =  XPathFactory.newInstance().newXPath();
//
//            range = Integer.valueOf(xPath.compile("/PROYECTO/LUCENE2CHARTS/RANGE/" + side.toUpperCase()).evaluate(doc));
//        } catch (Exception ex) {
//            System.out.println(ex.toString());
//        } finally {
//
//        }
//        
//        return range;
//    }
    
    public static Lucene2ChartsInfo GetChartInfo(String project) {
        Lucene2ChartsInfo oInfo = new Lucene2ChartsInfo();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            oInfo.Type = xPath.compile("/PROYECTO/LUCENE2CHARTS/INFO/TYPE").evaluate(doc);
            oInfo.Title = xPath.compile("/PROYECTO/LUCENE2CHARTS/INFO/TITLE").evaluate(doc);
            oInfo.SubTitle = xPath.compile("/PROYECTO/LUCENE2CHARTS/INFO/SUBTITLE").evaluate(doc);
            oInfo.GroupField = xPath.compile("/PROYECTO/LUCENE2CHARTS/INFO/DISTRIBUTION/GROUPFIELD").evaluate(doc);
            oInfo.NGroups = Integer.valueOf(xPath.compile("/PROYECTO/LUCENE2CHARTS/INFO/DISTRIBUTION/NGROUPS").evaluate(doc));
            oInfo.Ini = Integer.valueOf(xPath.compile("/PROYECTO/LUCENE2CHARTS/INFO/EVOLUTION/INI").evaluate(doc));
            oInfo.End = Integer.valueOf(xPath.compile("/PROYECTO/LUCENE2CHARTS/INFO/EVOLUTION/END").evaluate(doc));
            oInfo.XAxis = xPath.compile("/PROYECTO/LUCENE2CHARTS/INFO/EVOLUTION/XAXIS").evaluate(doc);
            oInfo.YAxis = xPath.compile("/PROYECTO/LUCENE2CHARTS/INFO/EVOLUTION/YAXIS").evaluate(doc);
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

            sTemplate = xPath.compile("/PROYECTO/LUCENE2CHARTS/TEMPLATE").evaluate(doc);
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

            sChart = xPath.compile("/PROYECTO/LUCENE2CHARTS/CHART").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sChart;
    }
}
