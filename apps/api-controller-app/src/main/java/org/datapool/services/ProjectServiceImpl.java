package org.datapool.services;

import com.google.gson.Gson;
import org.apache.catalina.User;
import org.datapool.dto.CreateTokenRq;
import org.datapool.dto.api.internal.*;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.db.*;
import org.datapool.dto.metadata.Message;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.jwt.PasswordJwtService;
import org.datapool.repository.DataSourceRepository;
import org.datapool.repository.ProjectJpaRepository;
import org.datapool.repository.ProjectPermissionRepository;
import org.datapool.repository.TokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.datapool.Utils.checkEmpty;
import static org.datapool.Utils.checkNull;

@Service
public class ProjectServiceImpl {
    @Autowired
    private ProjectJpaRepository projectJpaRepository;
    @Autowired
    private PermissionValidator permissionValidator;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private CacheManagerService cacheManagerService;
    @Autowired
    private AccountServiceImpl accountService;

    public InternalApiRequest updateProject(Project project){
        InternalApiRequest result = new InternalApiRequest();
        try {
            Optional<Project> data = projectJpaRepository.findById(project.getId());
            if (data.isPresent()){
                project.setCreated(new Date());
                projectJpaRepository.save(project);
                result.setResult(project);
                result.setCode(200);
                result.setSuccess(true);
                result.setMessage("OK");
            } else {
                result.setResult(project);
                result.setCode(404);
                result.setSuccess(false);
                result.setMessage("Internal error. Go to log.");
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(project);
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("Internal error. Go to log.");
        }
        return result;
    }

    public boolean checkProjectExist(String projectId){
        return projectJpaRepository.findById(projectId).isPresent();
    }

    @Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class)
    public void deleteProject(String projectId){
        try {
            Optional<Project> data = projectJpaRepository.findById(projectId);
            if (data.isPresent()){
                Project project = data.get();
                project.setActive(false);
                project.setName(project.getName() + "-deleted");
                projectJpaRepository.save(project);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public InternalApiRequest<List<Project>> getAllProjects(){
        InternalApiRequest result = new InternalApiRequest();
        List<Project> projects = new ArrayList<>();
        try {
            result.setResult(projectJpaRepository.findAll());
            result.setCode(200);
            result.setSuccess(true);
            result.setMessage("OK");
        } catch (Exception e){
            e.printStackTrace();
            result.setResult(projects);
            result.setCode(500);
            result.setSuccess(false);
            result.setMessage("Internal error. Go to log.");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class)
    public InternalApiRequest<UserProject> createProject(CreateProjectRq request, String userId) {
        InternalApiRequest<UserProject> result = new InternalApiRequest<UserProject>();
        if (checkNull(request.getDescription()) | checkNull(request.getName())){
            if (checkEmpty(request.getDescription()) | checkEmpty(request.getName())){
                result.setMessage("invalid data. Null or empty");
                result.setSuccess(false);
                return result;
            } else {
                String projectId = UUID.randomUUID().toString();
                Project project = new Project();
                project.setCreated(new Date());
                project.setId(projectId);
                project.setDescription(request.getDescription());
                project.setName(request.getName());
                if (!projectJpaRepository.findByName(request.getName()).isPresent()){
                    project = projectJpaRepository.save(project);
                    PrPermissionKey key = new PrPermissionKey(projectId, userId);
                    ProjectPermissions projectPermissions = new ProjectPermissions();
                    projectPermissions.setId(key);
                    projectPermissions.setRole(Role.ADMIN.role);
                    projectPermissions.setId(key);
                    permissionValidator.permissionRepository.save(projectPermissions);
                    UserProject body = new UserProject();
                    body.setDescription(request.getDescription());
                    body.setId(projectId);
                    body.setPermission(Role.ADMIN.name());
                    body.setName(request.getName());
                    result.setSuccess(true);
                    return result.setResult(body);
                } else {
                    result.setSuccess(false);
                    result.setMessage("Project with current name already exists");
                    return result;
                }
            }
        } else {
            result.setMessage("invalid data. Null or empty");
            result.setSuccess(false);
            return result;
        }
    }

    public InternalApiRequest getTeam(String projectId){
        InternalApiRequest result = new InternalApiRequest();
        result.setResult(permissionValidator.getTeam(projectId));
        result.setSuccess(true);
        result.setMessage("OK");
        return result;
    }

    @Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class)
    public InternalApiRequest addMember(TeamMemberRequest request, String userId){
        InternalApiRequest result = new InternalApiRequest();
        if (permissionValidator.checkProjectPermission(userId, request.getProjectId(), Role.ADMIN)){
            if (request.getUserId()!=null){
                ProjectPermissions projectPermissions = new ProjectPermissions()
                        .setId(new PrPermissionKey(request.getProjectId(), request.getUserId()))
                        .setRole(request.getRole().role);
                permissionValidator.permissionRepository.save(projectPermissions);
            } else if (request.getEmail()!=null){
                InternalApiRequest<Users> user = accountService.getUserByEmail(request.getEmail());
                if (user.isSuccess()){
                    ProjectPermissions projectPermissions = new ProjectPermissions()
                            .setId(new PrPermissionKey(request.getProjectId(), user.getResult().getId()))
                            .setRole(request.getRole().role);
                    permissionValidator.permissionRepository.save(projectPermissions);
                } else {
                    return user;
                }
            } else {
                result.setSuccess(false);
                result.setCode(400);
                result.setResult(new Message("bad data"));
                return result;
            }
        }
        result.setResult(permissionValidator.permissionRepository.selectProjectTeam(request.getProjectId()));
        result.setSuccess(true);
        result.setMessage("OK");
        return result;
    }

    @Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class)
    public InternalApiRequest deleteMember(TeamMemberRequest request, String userId){
        InternalApiRequest result = new InternalApiRequest();
        if (permissionValidator.checkProjectPermission(userId, request.getProjectId(), Role.ADMIN)){
            Optional<ProjectPermissions> perm = permissionValidator.permissionRepository.findById(new PrPermissionKey()
                    .setProjectId(request.getProjectId())
                    .setUserId(request.getUserId())
            );
            if (perm.isPresent()){
                permissionValidator.permissionRepository.delete(perm.get());
            } else {
                result.setMessage("not found");
                result.setCode(404);
                result.setSuccess(false);
                result.setResult(new Message("Not found user with id: " + request.getUserId()));
                return result;
            }
        }
        result.setResult(new Message("OK"));
        result.setSuccess(true);
        result.setMessage("OK");
        return result;
    }

    @Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class)
    public InternalApiRequest createToken(CreateTokenRq request, String userId, String mail){
        InternalApiRequest result = new InternalApiRequest();
        String tokenValue = UUID.randomUUID().toString();
        result = cacheManagerService.saveTokenInCache(tokenValue, request.getProjectId());
        if (result.isSuccess()){
            Token token = new Token()
                    .setToken(tokenValue)
                    .setCreator(mail)
                    .setProjectId(request.getProjectId())
                    .setName(request.getName());
            if (permissionValidator.checkProjectPermission(userId, request.getProjectId(), Role.ADMIN)){
                tokensRepository.save(token);
            }
            result.setResult(token);
            result.setSuccess(true);
            result.setMessage("OK");
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class, noRollbackFor = EntityNotFoundException.class)
    public InternalApiRequest deleteToken(String id, String userId){
        InternalApiRequest result = new InternalApiRequest();
        Token token = tokensRepository.getById(id);
        if (permissionValidator.checkProjectPermission(userId, token.getProjectId(), Role.ADMIN)){
            tokensRepository.delete(token);
            result = cacheManagerService.deleteToken(id);
            if (result.isSuccess()){
                result = getProjectTokens(token.getProjectId(), userId);
            }
        } else {
            result.setResult(new Message("You are not a member of the project team. "));
            result.setSuccess(false);
            result.setMessage("OK");
        }

        return result;
    }

    public InternalApiRequest updateTokensInCache(List<Token> tokens){
        InternalApiRequest result = new InternalApiRequest();
        try {
            for (Token token : tokens){
                cacheManagerService.saveTokenInCache(token.getToken(), token.getProjectId());
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setCode(500);
            result.setMessage(e.getMessage());
            result.setResult(tokens);
        }
        result.setResult(tokens);
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("OK");
        return result;
    }

    public InternalApiRequest getProjectTokens(String projectId, String userId){
        InternalApiRequest result = new InternalApiRequest();
        List<Token> tokens = new ArrayList<>();
        if (permissionValidator.checkProjectPermission(userId, projectId)){
            tokens = tokensRepository.findByProjectId(projectId);
            result.setResult(tokens);
            result.setSuccess(true);
            result.setMessage("OK");
        } else {
            result.setResult(new Message("You are not a member of the project team. "));
            result.setSuccess(false);
            result.setMessage("Permission denied");
        }
        return result;
    }

    public InternalApiRequest getToken(String projectId, String userId, String id){
        InternalApiRequest result = new InternalApiRequest();
        Optional<Token> token = null;
        if (permissionValidator.checkProjectPermission(userId, projectId)){
            token = tokensRepository.findById(id);
            if (token.isPresent()){
                result.setResult(token);
                result.setSuccess(true);
                result.setMessage("OK");
            } else {
                result.setResult(new Message("Token not found"));
                result.setSuccess(true);
                result.setMessage("OK");
            }
        } else {
            result.setResult(new Message("You are not a member of the project team. "));
            result.setSuccess(false);
            result.setMessage("Permission denied");
        }
        return result;
    }

    public InternalApiRequest<List<UserProject>> getProjects(String userId) {
        InternalApiRequest result = new InternalApiRequest<List<UserProjectPublicData>>();
        try {
            List<Object[]> projects = permissionValidator.permissionRepository.selectUserProjects(userId);
            List<UserProjectPublicData> resultData = new ArrayList<>();
            for (Object[] item : projects){
                if ((Boolean) item[3] == true){
                    UserProjectPublicData project = new UserProjectPublicData();
                    project.setId((String) item[0]);
                    project.setName((String) item[1]);
                    project.setDescription((String) item[2]);
                    project.setActive((Boolean) item[3]);
                    project.setUserId((String) item[4]);
                    project.setRole(permissionValidator.getRole((Integer) item[5]));
                    project.setCreated((Date) item[6]);
                    resultData.add(project);
                }
            }
            result.setResult(resultData);
            result.setSuccess(true);
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("Internal error. Go to logs.");
        }
        return result;
    }
}
