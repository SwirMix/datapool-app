package org.datapool.controllers;

import org.datapool.dto.GlobalToken;
import org.datapool.dto.api.internal.*;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.services.AccountServiceImpl;
import org.datapool.services.ProjectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
public class AccountRestController {
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private GlobalTokenService globalTokenService;

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/auth")
    public ResponseEntity<InternalApiRequest<AuthRs>> auth(
            @RequestBody AuthRq request
    ){
        InternalApiRequest<AuthRs> response = accountService.authAuth(request);
        if (response.isSuccess()){
            InternalApiRequest<List<UserProject>> projects = projectService.getProjects(response.getResult().getUserId());
            if (projects.isSuccess()){
                response.getResult().setProjects(projects.getResult());
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/create")
    public ResponseEntity<InternalApiRequest<CreateAccountRs>> registration(
            @RequestBody CreateAccountRq body
    ){
        InternalApiRequest<CreateAccountRs> result = accountService.createAccount(body);
        if (result.isSuccess()){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

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
    @PostMapping("/update/pass")
    public ResponseEntity<InternalApiRequest> updateUsers(
            @RequestHeader String token,
            @RequestBody UpdatePass body
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if(accountService.checkGlobalAdminStatus(globalToken.getUserId())){
                result = accountService.updatePass(body, globalToken.getUserId());
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

}
