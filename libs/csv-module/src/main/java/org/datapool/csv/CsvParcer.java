package org.datapool.csv;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CsvParcer {
    protected Path filePath;
    protected String[] columns;
    protected long rownum;
    protected CSVReader reader;

    public Map<String,String> getMapColumns(){
        Map<String,String> columns = new HashMap<>();
        for (String column : this.columns){
            columns.put(column, "text");
        }
        return columns;
    }

    public void reiniteReader() throws FileNotFoundException {
        this.reader = new CSVReader(new FileReader(filePath.toFile()));
        try {
            columns = reader.readNext();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> next(){
        Map<String, String> data = new HashMap<>();
        try {
            String[] row = reader.readNext();
            if (row!=null){
                for (int i = 0; i < columns.length; i++){
                    data.put(columns[i], row[i]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public Path getFilePath() {
        return filePath;
    }

    public CsvParcer setFilePath(Path filePath) {
        this.filePath = filePath;
        return this;
    }

    public String[] getColumns() {
        return columns;
    }

    public CsvParcer setColumns(String[] columns) {
        this.columns = columns;
        return this;
    }

    public long getRownum() {
        return rownum;
    }

    public CsvParcer setRownum(long rownum) {
        this.rownum = rownum;
        return this;
    }

    public CsvParcer(Path filePath) throws Exception {
        this.filePath = filePath;
        reiniteReader();
        Map<String,String> line = next();
        if (!line.isEmpty()){
            rownum = lines();
            reader.close();
        }
    }

    public void close() throws IOException {
        reader.close();
    }

    public long lines(){
        long count = -1;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                count++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return count;
    }
}
