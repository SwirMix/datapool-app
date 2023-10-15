package org.datapool;

import com.google.gson.internal.LinkedTreeMap;
import com.opencsv.CSVWriter;
import org.datapool.dto.DataPoolItem;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.metadata.Message;
import org.datapool.model.InternalApiRequestObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import retrofit2.Response;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Character.FORMAT;

public class Utils {
    public static boolean checkNull(String value){
        return Optional.of(value).isPresent();
    }

    public static boolean checkEmpty(String value){
        if (value.equals("")) {
            return true;
        } else return false;
    }

    public static ResponseEntity resultFilter(InternalApiRequest result){
        switch (result.getCode()){
            case 404:
                return new ResponseEntity(result, HttpStatus.NOT_FOUND);
            case 400:
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            case 500:
                return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                return new ResponseEntity(result, HttpStatus.OK);
        }
    }

    public static InternalApiRequest cacheManagerResponseConverter(InternalApiRequest object){
        InternalApiRequest result = new InternalApiRequest();
        result.setMessage(object.getMessage());
        result.setSuccess(object.isSuccess());
        result.setResult(object.getResult());
        return result;
    }

    public static File writeCsvFile(List<LinkedTreeMap> items, File file) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(file.getPath()))) {
            writer.writeNext((String[]) items.get(0).keySet().toArray(new String[0]));
            for (LinkedTreeMap map : items){
                List<String> line = new ArrayList<>();
                for (Object field : map.values()){
                    if (field instanceof String){
                        line.add((String) field);
                    }
                    if (field instanceof Integer){
                        line.add(Integer.toString((Integer) field));
                    }
                    if (field instanceof Double){
                        line.add(Double.toString((Double) field));
                    }
                    if (field instanceof Float){
                        line.add(Double.toString((Float) field));
                    }
                }
                writer.writeNext(line.toArray(new String[0]));
            }
        }
        return file;
    }
}
