package org.datapool.repository;

import com.google.gson.Gson;
import org.datapool.dto.db.DataSourceData;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class DataSourceDataConverter implements AttributeConverter<DataSourceData, String> {
    private final static Gson GSON = new Gson();

    @Override
    public String convertToDatabaseColumn(DataSourceData entity) {
        return GSON.toJson(entity);
    }

    @Override
    public DataSourceData convertToEntityAttribute(String dbData) {
        return GSON.fromJson(dbData, DataSourceData.class);
    }
}
