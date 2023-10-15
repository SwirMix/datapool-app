package org.datapool.controllers;

import org.datapool.dto.GlobalToken;
import org.datapool.dto.api.internal.UpdateUser;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.db.Project;
import org.datapool.dto.db.Setting;
import org.datapool.dto.db.Users;
import org.datapool.dto.metadata.Message;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.services.AccountServiceImpl;
import org.datapool.services.ProjectServiceImpl;
import org.datapool.services.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class GlobalAdminController {
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private GlobalTokenService globalTokenService;
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private SettingsService settingsService;

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/users")
    public ResponseEntity<InternalApiRequest> getUsers(
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            try {
                result = accountService.getUsers(globalToken.getUserId());
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
    @DeleteMapping("/users/{id}")
    public ResponseEntity<InternalApiRequest> deleteUser(
            @RequestHeader String token,
            @PathVariable String id
    ){
        InternalApiRequest result = new InternalApiRequest();
        result.setResult(new Message("OK"));
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("OK");
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkGlobalAdminStatus(globalToken.getUserId())){
                try {
                    accountService.deleteUser(id);
                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("internal error. Go to logs!");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                result.setSuccess(false);
                result.setMessage("you are not admin");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/users")
    public ResponseEntity<InternalApiRequest> updateUsers(
            @RequestHeader String token,
            @RequestBody UpdateUser body
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if(accountService.checkGlobalAdminStatus(globalToken.getUserId())){

                result = accountService.updateUser(body);
                if (result.isSuccess()){
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                result.setCode(403);
                result.setSuccess(false);
                result.setMessage("you are not admin");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("Internal error.");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/projects")
    public ResponseEntity<InternalApiRequest> updateProject(
            @RequestHeader String token,
            @RequestBody Project body
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if(accountService.checkGlobalAdminStatus(globalToken.getUserId())){
                result = projectService.updateProject(body);
                if (result.isSuccess()){
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                result.setCode(403);
                result.setSuccess(false);
                result.setMessage("you are not admin");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("Internal error.");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/projects")
    public ResponseEntity<InternalApiRequest> getProjects(
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if(accountService.checkGlobalAdminStatus(globalToken.getUserId())){
                result = projectService.getAllProjects();
                if (result.isSuccess()){
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                result.setCode(403);
                result.setSuccess(false);
                result.setMessage("you are not admin");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("Internal error.");
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<InternalApiRequest> deleteProject(
            @RequestHeader String token,
            @PathVariable String id
    ){
        InternalApiRequest result = new InternalApiRequest();
        result.setResult(new Message("OK"));
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("OK");
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkGlobalAdminStatus(globalToken.getUserId())){
                try {
                    projectService.deleteProject(id);
                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("internal error. Go to logs!");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                result.setSuccess(false);
                result.setMessage("you are not admin");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/settings")
    public ResponseEntity<InternalApiRequest> getSettings(
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        result.setResult(new Message("OK"));
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("OK");
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkGlobalAdminStatus(globalToken.getUserId())){
                try {
                    result = settingsService.getAllSettings();
                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("internal error. Go to logs!");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                result.setSuccess(false);
                result.setMessage("you are not admin");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/settings")
    public ResponseEntity<InternalApiRequest> postSettings(
            @RequestHeader String token,
            @RequestBody List<Setting> settings
    ){
        InternalApiRequest result = new InternalApiRequest();
        result.setResult(new Message("OK"));
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("OK");
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkGlobalAdminStatus(globalToken.getUserId())){
                try {
                    result = settingsService.saveAllSettings(settings);
                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("internal error. Go to logs!");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                result.setSuccess(false);
                result.setMessage("you are not admin");
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
