package org.datapool.controllers;

import org.datapool.ApplicationConfiguration;
import org.datapool.dto.ClusterStatus;
import org.datapool.dto.GlobalToken;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.model.Status;
import org.datapool.repository.SettingsRepository;
import org.datapool.services.AccountServiceImpl;
import org.datapool.services.CacheManagerService;
import org.datapool.services.DatapoolService;
import org.datapool.victoria.ConsumersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/health")
public class HealthClusterController {
    @Autowired
    private CacheManagerService cacheManagerService;
    @Autowired
    private DatapoolService datapoolService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private GlobalTokenService globalTokenService;
    @Autowired
    private ConsumersService consumersService;
    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private SettingsRepository settingsRepository;

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/")
    public ResponseEntity<InternalApiRequest> getStatus(
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        List statusList = new ArrayList<>();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkGlobalAdminStatus(globalToken.getUserId())){
                try {
                    Status status = cacheManagerService.getHealth();
                    ClusterStatus clusterStatus = new ClusterStatus();
                    clusterStatus.setStatus(status.getStatus());
                    clusterStatus.setName("cache-manager-app");
                    clusterStatus.setPath(settingsRepository.findByName(configuration.getCacheManagerApp()).getValue());
                    statusList.add(clusterStatus);
                } catch (Exception e){
                    e.printStackTrace();
                    ClusterStatus clusterStatus = new ClusterStatus();
                    clusterStatus.setStatus("DOWN");
                    clusterStatus.setName("cache-manager-app");
                    clusterStatus.setPath(settingsRepository.findByName(configuration.getCacheManagerApp()).getValue());
                    statusList.add(clusterStatus);
                }
                try {
                    org.datapool.api.Status status = datapoolService.getHealth();
                    ClusterStatus clusterStatus = new ClusterStatus();
                    clusterStatus.setStatus(status.getStatus());
                    clusterStatus.setName("datapool-app");
                    clusterStatus.setPath(settingsRepository.findByName(configuration.getDatapoolApp()).getValue());
                    statusList.add(clusterStatus);
                } catch (Exception e){
                    e.printStackTrace();
                    ClusterStatus clusterStatus = new ClusterStatus();
                    clusterStatus.setStatus("DOWN");
                    clusterStatus.setName("datapool-app");
                    clusterStatus.setPath(settingsRepository.findByName(configuration.getDatapoolApp()).getValue());
                    statusList.add(clusterStatus);
                }
                try {
                    boolean status = consumersService.getHealth();
                    ClusterStatus clusterStatus = new ClusterStatus();
                    if (status){
                        clusterStatus.setStatus("UP");
                    } else {
                        clusterStatus.setStatus("DOWN");
                    }
                    clusterStatus.setName("victoria-metrics");
                    clusterStatus.setPath(settingsRepository.findByName(configuration.getMetricsPusher()).getValue());
                    statusList.add(clusterStatus);
                } catch (Exception e){
                    e.printStackTrace();
                    ClusterStatus clusterStatus = new ClusterStatus();
                    clusterStatus.setStatus("DOWN");
                    clusterStatus.setName("victoria-metrics");
                    clusterStatus.setPath(settingsRepository.findByName(configuration.getMetricsPusher()).getValue());
                    statusList.add(clusterStatus);
                }
                result.setResult(statusList);
                result.setCode(200);
                result.setSuccess(true);
                result.setMessage("OK");
            } else {
                result.setSuccess(false);
                result.setMessage("You are not ADMIN! Go away!");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }
}
