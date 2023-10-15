package org.datapool.services;

import org.datapool.ApplicationConfiguration;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.commons.dto.metadata.Message;
import org.datapool.dto.db.Setting;
import org.datapool.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingsService {
    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private ApplicationConfiguration configuration;

    public InternalApiRequest saveAllSettings(List<Setting> settings){
        InternalApiRequest result = new InternalApiRequest();
        try {

            result.setResult(settingsRepository.saveAll(settings));
            result.setCode(200);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(new Message("JDBS error."));
            result.setCode(200);
            result.setSuccess(false);
            result.setMessage("OK");
        }
        return result;
    }

    public InternalApiRequest<List<Setting>> getAllSettings(){
        InternalApiRequest<List<Setting>> result = new InternalApiRequest();
        result.setResult(new ArrayList<>());
        try {
            List<Setting> data = settingsRepository.findAll();
            for (Setting setting : data){
                if (!(setting.getName().equals(configuration.getJwtSecret())| setting.getName().equals(configuration.getPasswordSecret()))){
                    result.getResult().add(setting);
                }
            }
            result.setCode(200);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e){
            e.printStackTrace();
            result.setCode(200);
            result.setSuccess(false);
            result.setMessage("JDBS error.");
        }
        return result;
    }
}
