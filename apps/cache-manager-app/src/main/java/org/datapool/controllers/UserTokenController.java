package org.datapool.controllers;

import org.datapool.dto.DataPoolToken;
import org.datapool.dto.SaveToken;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.services.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tokens")
public class UserTokenController {
    @Autowired
    private UserTokenService tokenService;

    @PostMapping("/")
    public ResponseEntity saveRemoteToken(
            @RequestBody SaveToken token
    ){
        DataPoolToken dataPoolToken = new DataPoolToken()
                .setToken(token.getToken())
                .setProject(token.getProject());
        InternalApiRequest result = tokenService.updateOrCreate(dataPoolToken);
        if (result.isSuccess()){
            return new ResponseEntity(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{token}")
    public ResponseEntity saveRemoteToken(
            @PathVariable String token
    ){
        InternalApiRequest result = tokenService.getToken(token);
        if (result.isSuccess()){
            return new ResponseEntity(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{token}")
    public ResponseEntity deleteToken(
            @PathVariable String token
    ){
        InternalApiRequest result = tokenService.deleteToken(token);
        if (result.isSuccess()){
            return new ResponseEntity(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
