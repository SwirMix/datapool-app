package org.datapool.repository;

import com.google.gson.Gson;
import org.datapool.dto.db.CacheLogEntity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CacheLogDataConverter implements AttributeConverter<CacheLogEntity, String> {
    private final static Gson GSON = new Gson();

    @Override
    public String convertToDatabaseColumn(CacheLogEntity entity) {
        return GSON.toJson(entity);
    }

    @Override
    public CacheLogEntity convertToEntityAttribute(String dbData) {
        return GSON.fromJson(dbData, CacheLogEntity.class);
    }
}
