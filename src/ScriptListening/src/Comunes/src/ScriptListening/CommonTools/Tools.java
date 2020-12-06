package ScriptListening.CommonTools;

import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

/**
 *
 * @author @kicorangel
 */


public class Tools {
    
    public static void OrderList (String lista_palabras[]){
        boolean ordenado=false;
        int cuentaIntercambios=0;
        //Usamos un bucle anidado, saldra cuando este ordenado el array
        while(!ordenado){
            for(int i=0;i<lista_palabras.length-1;i++){
                if (lista_palabras[i].compareToIgnoreCase(lista_palabras[i+1])>0){
                    //Intercambiamos valores
                    String aux=lista_palabras[i];
                    lista_palabras[i]=lista_palabras[i+1];
                    lista_palabras[i+1]=aux;
                    //indicamos que hay un cambio
                    cuentaIntercambios++;
                }
            }
            //Si no hay intercambios, es que esta ordenado.
            if (cuentaIntercambios==0){
                ordenado=true;
            }
            //Inicializamos la variable de nuevo para que empiece a contar de nuevo
            cuentaIntercambios=0;
        }
 
    }
    
    public static String GetNOMBRE(String project) {
        String sPath = "";
        try {
            String sFileName = "../proyectos/" + project + ".xml";
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(sFileName);
            XPath xPath =  XPathFactory.newInstance().newXPath();

            sPath = xPath.compile("/PROYECTO/NOMBRE").evaluate(doc);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {

        }
        
        return sPath;
    }
    
    public static boolean IsValidDate(String sDate) {
        boolean bValid = true;
        Date oDt = null;
        int day = Integer.valueOf(sDate.substring(6, 8));
        int month = Integer.valueOf(sDate.substring(4, 6)) - 1;
        int year = Integer.valueOf(sDate.substring(0, 4));
        try {
            oDt = new Date(year, 
                    month, 
                    day);
        } catch (Exception ex) {
            bValid = false;
        }
        
        if (year!=oDt.getYear() || month!=oDt.getMonth() || day!=oDt.getDate()) {
            bValid = false;
        }
        
        return bValid;
    }
    
    public static String FormatDate(String date2Format) {
        String sYear = date2Format.substring(0, 4);
        String sMonth = date2Format.substring(4, 6);
        String sDay = date2Format.substring(6, 8);
        
        return sDay + "-" + sMonth + "-" + sYear;
    }
}
