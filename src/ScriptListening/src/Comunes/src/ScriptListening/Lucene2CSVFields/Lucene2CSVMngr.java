package ScriptListening.Lucene2CSVFields;

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
public class Lucene2CSVMngr {
    public static ArrayList<Lucene2CSVFields> GetFields(String project) {
        ArrayList<Lucene2CSVFields> oFields = new ArrayList<Lucene2CSVFields>();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/LUCENE2CSV/CAMPOS/CAMPO").evaluate(doc, XPathConstants.NODESET);
            for (int i=0;i<nodeList.getLength();i++) {
                Element element = (Element)nodeList.item(i);
                
                Lucene2CSVFields oField = new Lucene2CSVFields();
                oField.Process = ((String)element.getAttribute("procesar")).equalsIgnoreCase("si");
                oField.Field = element.getAttribute("nombre");
                oFields.add(oField);
            }
        } catch (Exception ex) {
            
        }
        
        return oFields;
    }
    
    public static String GetINDEXPath(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/LUCENE2CSV/INDEX").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sPath;
    }
    
    public static String GetCSVPath(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/LUCENE2CSV/CSV").evaluate(doc);
        } catch (Exception ex) {
            System.out.println();
        } finally {

        }
        
        return sPath;
    }
    
    public static ArrayList<Lucene2CSVFilters> GetFilters(String project) {
        ArrayList<Lucene2CSVFilters> oFilters = new ArrayList<Lucene2CSVFilters>();
        
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            
            XPath xPath =  XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile("/PROYECTO/LUCENE2CSV/FILTER/QUERY").evaluate(doc, XPathConstants.NODESET);
            for (int i=0;i<nodeList.getLength();i++) {
                Element element = (Element)nodeList.item(i);
                
                Lucene2CSVFilters oFilter = new Lucene2CSVFilters();
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
