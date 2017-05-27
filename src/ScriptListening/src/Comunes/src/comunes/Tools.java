package comunes;

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
}
