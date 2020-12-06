package ScriptListening.comunes;

import java.util.Date;

/**
 *
 * @author @kicorangel
 */
public class Tools {
    public static int GetInt(String intValue) {
        return GetInt(intValue, 0);
    }
    
    public static int GetInt(String intValue, int defaultValue) {
        int retValue = defaultValue;
        try {
            retValue = Integer.valueOf(intValue);
        } catch (Exception ex) {
            
        }
        
        return retValue;
    }
    
    public static double GetDouble(String doubleValue) {
        return GetDouble(doubleValue, 0);
    }
    
    public static double GetDouble(String doubleValue, double defaultValue) {
        double retValue = defaultValue;
        try {
            retValue = Double.valueOf(doubleValue);
        } catch (Exception ex) {
            
        }
        return retValue;
    }
    
    public static String ToCTIMESTAMP(String toConvert) {
        String ctimestamp = "";
        
        Date oDt = new Date(toConvert);
        
        ctimestamp = (1900+oDt.getYear()) + "" +
                PaddingLeft(String.valueOf(1+oDt.getMonth()), 2, "0") +
                PaddingLeft(String.valueOf(oDt.getDate()), 2, "0") +
                PaddingLeft(String.valueOf(oDt.getHours()), 2, "0") +
                PaddingLeft(String.valueOf(oDt.getMinutes()), 2, "0") +
                PaddingLeft(String.valueOf(oDt.getSeconds()), 2, "0") + "0";
        
        return ctimestamp;
    }
    
    public static String PaddingLeft(String val, int n, String fill) {
        String padded = "";
        
        padded = val;
        
        for (int i=val.length();i<n;i++) {
            padded = fill + padded;
        }
        
        return padded;
    }
    
    public static int IndexOf(String content, int ini)
    {
        int iPos = ini;
        if (iPos>=0 && iPos<content.length())
        {
            for (iPos=ini;iPos<content.length();iPos++)
            {
                if (!Character.isDigit(content.charAt(iPos)) &&
                        !Character.isLetter(content.charAt(iPos)) &&
                        content.charAt(iPos) != '_')
                    break;
            }
        }
        return iPos;
    }
}
