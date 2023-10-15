package org.datapool.ant;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;

public class Utils {
    public static String[] extractData(Collection data){
        ArrayList values = new ArrayList();
        for (Object value : data){
            String finalStr = "";
            if (value instanceof String){
                finalStr = ((String) value).replace("\"\"", "\"");
            } else if (value instanceof Long){
                finalStr = Long.toString((Long) value);
            } else if (value instanceof Double){
                finalStr = Double.toString((double) value);
            } else if (value instanceof Float){
                finalStr = Float.toString((Float) value);
            }
            values.add(finalStr);
        }
        return (String[]) values.toArray(new String[0]);
    }

    public static Configuration readConfiguration(String filePath) throws FileNotFoundException {
        Gson gson = new Gson();
        File file = new File(filePath);
        if (file.exists()){
            JsonReader reader = new JsonReader(new FileReader(file));
            Configuration data = gson.fromJson(reader, Configuration.class);
            return data;
        } else throw new FileNotFoundException();
    }
}
