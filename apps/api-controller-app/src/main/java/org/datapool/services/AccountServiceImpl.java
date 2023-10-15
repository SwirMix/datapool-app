package org.datapool.services;

import org.datapool.ApplicationConfiguration;
import org.datapool.dto.GlobalToken;
import org.datapool.dto.Passwd;
import org.datapool.dto.api.internal.*;
import org.datapool.dto.commons.InternalApiRequest;
import org.datapool.dto.db.Role;
import org.datapool.dto.db.Users;
import org.datapool.jwt.GlobalTokenService;
import org.datapool.jwt.PasswordJwtService;
import org.datapool.model.Message;
import org.datapool.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.datapool.Utils.checkNull;

@Service
public class AccountServiceImpl {
    @Autowired
    private PasswordJwtService passwordJwtService;
    @Autowired
    private GlobalTokenService tokenService;
    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private ApplicationConfiguration configuration;
    @Autowired
    private UsersJpaRepository usersJpaRepository;
    @Autowired
    private ProjectPermissionRepository permissionRepository;
    @Autowired
    private TokensRepository tokensRepository;

    public InternalApiRequest updatePass(UpdatePass request, String userId){
        InternalApiRequest result = new InternalApiRequest<>();
        Optional<Users> data = usersJpaRepository.findById(userId);
        if (data.isPresent()){
            Users user = data.get();
            Passwd passwd = new Passwd().setPassword(request.getOld()).setTimestamp(user.getRegistration());
            Passwd oldPass = passwordJwtService.decryptGlobalToken(user.getPassword());
            if (passwd.getPassword().equals(oldPass.getPassword())){
                passwd = new Passwd().setPassword(request.getNewPass()).setTimestamp(user.getRegistration());
                String password = passwordJwtService.createGlobalToken(passwd);
                user.setPassword(password);
                usersJpaRepository.save(user);
                result.setSuccess(true);
                result.setCode(200);
                result.setMessage("OK");
                return result;
            } else {
                result.setSuccess(false);
                result.setCode(400);
                result.setMessage("Invalid password");
                return result;
            }
        }
        result.setSuccess(false);
        result.setCode(404);
        result.setMessage("user not found");
        return result;
    }

    public InternalApiRequest updateUser(UpdateUser user){
        InternalApiRequest result = new InternalApiRequest<>();
        try {
            Optional<Users> data = usersJpaRepository.findById(user.getId());
            if (data.isPresent()){
                Users item = data.get();

                if (user.getPassword() != null){
                    if (!user.getPassword().equals("")){
                        Passwd passwd = new Passwd().setPassword(user.getPassword()).setTimestamp(item.getRegistration());
                        String password = passwordJwtService.createGlobalToken(passwd);
                        item.setPassword(password);
                    }

                }
                item.setEmail(user.getEmail())
                        .setGlobalAdmin(user.isAdmin())
                        .setLogin(user.getLogin());
                usersJpaRepository.save(item);
                result.setSuccess(true);
                result.setMessage("OK");
                result.setResult(user);
                result.setCode(200);
            } else {
                result.setSuccess(false);
                result.setMessage("Invalid userId.");
                result.setCode(404);
            }
        } catch (Exception e){
            e.printStackTrace();
            result.setSuccess(false);
            result.setMessage("Invalid values.");
            result.setCode(500);
        }
        return result;
    }

    public InternalApiRequest<CreateAccountRs> createAccount(CreateAccountRq request) {
        InternalApiRequest<CreateAccountRs> result = new InternalApiRequest<>();
        if (checkNull(request.getEmail()) && checkNull(request.getLogin()) && checkNull(request.getPassword())){
            Optional<Users> userAccount = usersJpaRepository.findByEmail(request.getEmail());
            if (userAccount.isPresent() | usersJpaRepository.findByLogin(request.getLogin()).isPresent()){
                result.setSuccess(false);
                result.setMessage("User with current email or login already exists.");
            } else {
                String id = UUID.randomUUID().toString();
                Passwd passwd = new Passwd().setPassword(request.getPassword()).setTimestamp(new Date());
                String password = passwordJwtService.createGlobalToken(passwd);
                Users account = new Users()
                        .setActive(true)
                        .setPassword(password)
                        .setId(id)
                        .setRegistration(passwd.getTimestamp())
                        .setEmail(request.getEmail())
                        .setLogin(request.getLogin());
                usersJpaRepository.save(account);
                CreateAccountRs response = new CreateAccountRs()
                        .setEmail(account.getEmail())
                        .setLogin(account.getLogin())
                        .setId(account.getId());
                result.setSuccess(true);
                result.setResult(response);
                result.setMessage("OK");
                return result;
            }
        } else {
            result.setSuccess(false);
            result.setMessage("Invalid values.");
        }
        return result;
    }

    public InternalApiRequest<Users> getUserByEmail(String email){
        InternalApiRequest result = new InternalApiRequest();
        Optional<Users> user = usersJpaRepository.findByEmail(email);
        if (user.isPresent()){
            result.setSuccess(true);
            result.setCode(200);
            result.setResult(user.get());
            result.setMessage("OK");
        } else {
            result.setSuccess(false);
            result.setCode(404);
            result.setMessage("not found");
            result.setResult(null);
        }
        return result;
    }

    public void deleteUser(String userId){
        Optional<Users> data = usersJpaRepository.findById(userId);
        if (data.isPresent()){
            Users account = data.get();
            account.setActive(false);
            account.setEmail(UUID.randomUUID().toString());
            account.setLogin("undefined");
            usersJpaRepository.save(account);
        }
    }

    public InternalApiRequest<List<TeamMember>> getUsers(String userId){
        InternalApiRequest<List<TeamMember>> result = new InternalApiRequest<>();
        result.setResult(new ArrayList<>());
        result.setSuccess(true);
        result.setCode(200);
        result.setMessage("OK");
        List<Users> users = usersJpaRepository.findAll();
        for (Users user : users){
            if (!userId.equals(user.getId()) & user.isActive()){
                result.getResult().add(new TeamMember(
                        user.getId(),
                        user.getEmail(),
                        Role.TEAMMATE.name(),
                        user.getLogin(),
                        user.isGlobalAdmin()
                        )
                );
            }
        }
        return result;
    }
    public boolean checkUserExist(String userId){
        return usersJpaRepository.findById(userId).isPresent();
    }

    public boolean checkGlobalAdminStatus(String userId){
        Users user = usersJpaRepository.findById(userId).get();
        return user.isGlobalAdmin();
    }

    public InternalApiRequest<AuthRs> authAuth(AuthRq request) {
        InternalApiRequest<AuthRs> result = new InternalApiRequest<>();
        if (checkNull(request.getEmail()) | checkNull(request.getPassword())){
            String email = request.getEmail();
            Optional<Users> user = usersJpaRepository.findByEmail(email);
            if (user.isPresent()){
                Users account = user.get();
                Passwd passwd = new Passwd().setPassword(request.getPassword()).setTimestamp(account.getRegistration());
                Passwd oldPass = passwordJwtService.decryptGlobalToken(account.getPassword());
                if (passwd.getPassword().equals(oldPass.getPassword())){
                    GlobalToken globalToken = new GlobalToken();
                    globalToken.setEmail(account.getEmail());
                    globalToken.setUserId(account.getId());
                    globalToken.setCreateTimestamp(System.currentTimeMillis());
                    globalToken.setEndValidTimestamp(System.currentTimeMillis() + 365 * 24 * 3600 * 1000);

                    String token = tokenService.createGlobalToken(globalToken);

                    AuthRs body = new AuthRs();
                    body.setUserId(user.get().getId());
                    body.setEmail(user.get().getEmail());
                    body.setGlobalAdmin(user.get().isGlobalAdmin());
                    body.setJwt(token);
                    body.setProjects(new ArrayList());
                    result.setSuccess(true);
                    result.setResult(body);
                    return result;
                } else {
                    result.setSuccess(false);
                    result.setMessage("Invalid email or password");
                }
            } else {
                result.setSuccess(false);
                result.setMessage("Invalid email or password");
            }
        } else {
            result.setSuccess(false);
            result.setMessage("Invalid values.");
        }
        return result;
    }

}
