package org.datapool.controllers;

import org.datapool.dto.GlobalToken;
import org.datapool.dto.api.internal.CreateDataSourceRq;
import org.datapool.dto.api.internal.CreateProjectRs;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.db.DataSource;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.services.CacheManagerService;
import org.datapool.services.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/datasource")
public class DataSourceRestController {

    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private GlobalTokenService globalTokenService;
    @Autowired
    private CacheManagerService cacheManagerService;

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/update")
    public ResponseEntity<InternalApiRequest<CreateProjectRs>> updateProject(
            @RequestBody DataSource request,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            try {
                result = dataSourceService.updateDataSource(request, globalToken.getUserId());
                if (!result.isSuccess()){
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("internal error. Go to logs!");
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/create")
    public ResponseEntity<InternalApiRequest<CreateProjectRs>> createProject(
            @RequestBody CreateDataSourceRq request,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            try {
                result = dataSourceService.createDataSource(globalToken.getUserId(), request);
                if (!result.isSuccess()){
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("internal error. Go to logs!");
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteDataSource(
            @PathVariable String id,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            try {
                result = dataSourceService.deleteDataSource(globalToken.getUserId(), id);
                if (!result.isSuccess()){
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("internal error. Go to logs!");
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/{projectId}")
    public ResponseEntity<InternalApiRequest> getDataSources(
            @PathVariable String projectId,
            @RequestHeader String token,
            @RequestParam(required = false) String userId
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            try {
                result = dataSourceService.getDataSources(projectId, globalToken.getUserId());
                if (!result.isSuccess()){
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("internal error. Go to logs!");
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/details/{id}")
    public ResponseEntity<InternalApiRequest> getDataSourceData(
            @PathVariable String id,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            try {
                result = dataSourceService.getDataSourceById(id, globalToken.getUserId());
                if (!result.isSuccess()){
                    switch (result.getCode()){
                        case 400:
                            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                        case 404:
                            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("internal error. Go to logs!");
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/browse/{id}")
    public ResponseEntity<InternalApiRequest> browseDatasource(
            @PathVariable String id,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            try {
                result = dataSourceService.getDataSourceById(id, globalToken.getUserId());
                if (!result.isSuccess()){
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
                DataSource dataSource = (DataSource) result.getResult();
                switch (dataSource.getProperties().getType()){
                    case POSTGRESQL:
                        result = cacheManagerService.getJdbcTables(dataSource, "org.postgresql.Driver");
                        return new ResponseEntity<>(result, HttpStatus.OK);
                }
            } catch (Exception e){
                e.printStackTrace();
                result.setSuccess(false);
                result.setMessage("internal error. Go to logs!");
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
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
