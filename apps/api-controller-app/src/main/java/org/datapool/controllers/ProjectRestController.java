package org.datapool.controllers;

import org.datapool.dto.CreateTokenRq;
import org.datapool.dto.GlobalToken;
import org.datapool.dto.api.internal.CreateDataSourceRq;
import org.datapool.dto.api.internal.CreateProjectRq;
import org.datapool.dto.api.internal.CreateProjectRs;
import org.datapool.dto.api.internal.TeamMemberRequest;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.db.DataSource;
import org.datapool.dto.db.Token;
import org.datapool.dto.db.UserProjectPublicData;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.repository.UsersJpaRepository;
import org.datapool.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectRestController {

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private GlobalTokenService globalTokenService;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private PermissionValidator permissionValidator;

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/create")
    public ResponseEntity<InternalApiRequest<CreateProjectRs>> createProject(
            @RequestBody CreateProjectRq request,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            try {
                result = projectService.createProject(request, globalToken.getUserId());
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
    @GetMapping("/")
    public ResponseEntity<InternalApiRequest<List<UserProjectPublicData>>> getProjects(
            @RequestHeader String token,
            @RequestParam(required = false) String userId
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (userId==null){
                userId = globalToken.getUserId();
            }
            try {
                result = projectService.getProjects(userId);
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
    @GetMapping("/{project}")
    public ResponseEntity<InternalApiRequest<List<UserProjectPublicData>>> getProjectPermission(
            @RequestHeader String token,
            @PathVariable String project
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            try {
                if (permissionValidator.checkProjectPermission(globalToken.getUserId(), project)){
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
    @DeleteMapping("/member")
    public ResponseEntity<InternalApiRequest> deleteUserFromProject(
            @RequestHeader String token,
            @RequestBody TeamMemberRequest request
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkUserExist(request.getUserId()) && projectService.checkProjectExist(request.getProjectId())){
                try {
                    result = projectService.deleteMember(request, globalToken.getUserId());
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
            } else {
                result.setSuccess(false);
                result.setMessage("User or project not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/member")
    public ResponseEntity<InternalApiRequest> addUserToProject(
            @RequestBody TeamMemberRequest request,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkUserExist(request.getUserId()) && projectService.checkProjectExist(request.getProjectId())){
                try {
                    result = projectService.addMember(request, globalToken.getUserId());
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
            } else {
                result.setSuccess(false);
                result.setMessage("User or project not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/team/{projectId}")
    public ResponseEntity<InternalApiRequest> getTeam(
            @RequestHeader String token,
            @PathVariable String projectId
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkUserExist(globalToken.getUserId()) && projectService.checkProjectExist(projectId)){
                try {
                    result = projectService.getTeam(projectId);
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
            } else {
                result.setSuccess(false);
                result.setMessage("User or project not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/tokens/{projectId}")
    public ResponseEntity<InternalApiRequest> updateTokenInCluster(
            @RequestHeader String token,
            @PathVariable String projectId
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkUserExist(globalToken.getUserId()) && projectService.checkProjectExist(projectId)){
                try {
                    result = projectService.getProjectTokens(projectId, globalToken.getUserId());
                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("internal error. Go to logs!");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                result = projectService.updateTokensInCache((List<Token>) result.getResult());
                if (!result.isSuccess()){
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setSuccess(false);
                result.setMessage("User or project not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/tokens/{projectId}")
    public ResponseEntity<InternalApiRequest> getTokens(
            @RequestHeader String token,
            @PathVariable String projectId
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkUserExist(globalToken.getUserId()) && projectService.checkProjectExist(projectId)){
                try {
                    result = projectService.getProjectTokens(projectId, globalToken.getUserId());
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
            } else {
                result.setSuccess(false);
                result.setMessage("User or project not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/tokens/{projectId}/{id}")
    public ResponseEntity<InternalApiRequest> getToken(
            @RequestHeader String token,
            @PathVariable String projectId,
            @PathVariable String id
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkUserExist(globalToken.getUserId()) && projectService.checkProjectExist(projectId)){
                try {
                    result = projectService.getToken(projectId, globalToken.getUserId(), id);
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
            } else {
                result.setSuccess(false);
                result.setMessage("User or project not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/tokens")
    public ResponseEntity<InternalApiRequest> createToken(
            @RequestBody CreateTokenRq request,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkUserExist(globalToken.getUserId()) && projectService.checkProjectExist(request.getProjectId())){
                try {
                    result = projectService.createToken(request, globalToken.getUserId(), globalToken.getEmail());
                    if (!result.isSuccess()){
                        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    result.setSuccess(false);
                    result.setMessage("internal error. Go to logs!");
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<>(projectService.getProjectTokens(request.getProjectId(), globalToken.getUserId()), HttpStatus.OK);
            } else {
                result.setSuccess(false);
                result.setMessage("User or project not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @DeleteMapping("/tokens/{id}")
    public ResponseEntity<InternalApiRequest> deleteToken(
            @PathVariable String id,
            @RequestHeader String token
    ){
        InternalApiRequest result = new InternalApiRequest();
        try {
            GlobalToken globalToken = globalTokenService.decryptGlobalToken(token);
            if (accountService.checkUserExist(globalToken.getUserId())){
                try {
                    result = projectService.deleteToken(id, globalToken.getUserId());
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
            } else {
                result.setSuccess(false);
                result.setMessage("User or project not exist.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("invalid token");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }


}
