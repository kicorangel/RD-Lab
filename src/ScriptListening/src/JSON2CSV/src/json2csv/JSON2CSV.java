package json2csv;

import JSON2CSV.JSON2CSVFields;
import JSON2CSV.JSON2CSVMngr;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author @kicorangel
 */

public class JSON2CSV {

    public static void main(String[] args) throws IOException {
        if (args.length<1) {
            System.out.println("Debe indicar el nombre del proyecto como parámetro. Este nombre debe coincidir con uno de los ficheros .xml de su carpeta proyectos, sin la extensión xml. Por ejemplo: ejemplo");
            return;
        }
        
        String sProyecto = args[0];
        String sJSONPath = JSON2CSVMngr.GetJSONPath(sProyecto);
        String sCSVPath = JSON2CSVMngr.GetCSVPath(sProyecto);
        ArrayList<JSON2CSVFields> oFields = JSON2CSVMngr.GetFields(sProyecto);
        
        FileWriter fw = new FileWriter(sCSVPath);
        
        StringBuilder sb = new StringBuilder();
        for (int iField=0;iField<oFields.size();iField++) {
            JSON2CSVFields oField = oFields.get(iField);
            if (oField.Process) {
                sb.append("\"" + oField.Field + "\",");
            }
        }
        String sLine = sb.toString();
        if (sLine.endsWith(",")) {
            sLine = sLine.substring(0, sLine.length() - 1);
        }
        fw.write(sLine + "\n");
        
        File directory = new File(sJSONPath);
        String [] folders = directory.list();
        for (int iFolder = 0; iFolder < folders.length; iFolder++) {
            String sFolderName = folders[iFolder];
            String sFolder = sJSONPath + "/" + sFolderName;
            if (new File(sFolder).isDirectory()) {
                File subdir = new File(sFolder);
                String []files = subdir.list();
                for (int iFile=0;iFile<files.length;iFile++) {
                    String sFileName = sFolder + "/" + files[iFile];

                    try {
                        byte[] encoded = Files.readAllBytes(Paths.get(sFileName));
                        String sJSON = new String(encoded);

                        JSONObject json = new JSONObject(sJSON);

                        sb = new StringBuilder();
                        for (int iField=0;iField<oFields.size();iField++) {
                            JSON2CSVFields oField = oFields.get(iField);
                            if (oField.Process) {
                                String sFieldValue = GetFieldValue(oField.Field, json, "");
                                sb.append("\"" + sFieldValue + "\",");
                            }
                        }
                        sLine = sb.toString();
                        if (sLine.endsWith(",")) {
                            sLine = sLine.substring(0, sLine.length() - 1);
                        }
                        fw.write(sLine + "\n");
                        
                        
                    } catch (Exception ex) {
                        
                    }
                }
            }
        }               
        
        fw.flush();
        fw.close();
    }
    
    private static String GetFieldValue(String fieldName, JSONObject json, String fieldValue) throws JSONException {
        if (!fieldName.contains(":")) {
            return json.optString(fieldName, fieldValue);
        } else {
            String []data = fieldName.split(":");
            JSONObject myJson = json.getJSONObject(data[0]);
            String myFieldName = fieldName.replaceFirst(data[0] + ":", "");
            return GetFieldValue(myFieldName, myJson, fieldValue);
        }
    }
}
