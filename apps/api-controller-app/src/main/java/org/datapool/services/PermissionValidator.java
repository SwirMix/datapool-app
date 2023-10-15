package org.datapool.services;


import org.datapool.dto.api.internal.TeamMember;
import org.datapool.dto.db.PrPermissionKey;
import org.datapool.dto.db.ProjectPermissions;
import org.datapool.dto.db.Role;
import org.datapool.repository.ProjectPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PermissionValidator {
    @Autowired
    protected ProjectPermissionRepository permissionRepository;

    public boolean checkProjectPermission(String userId, String projectId){
        Optional<ProjectPermissions> permissions = permissionRepository.findById(new PrPermissionKey(projectId, userId));
        if (!permissions.isPresent()){
            return false;
        } else {
            return true;
        }
    }

    public boolean checkProjectPermission(String userId, String projectId, Role role){
        Optional<ProjectPermissions> permissions = permissionRepository.findById(new PrPermissionKey(projectId, userId));
        if (!permissions.isPresent()){
            return false;
        } else {
            if (permissions.get().getRole() == role.role) return true;
            else return false;
        }
    }

    public List<TeamMember> getTeam(String projectId){
        List<Object[]> team = permissionRepository.selectTeam(projectId);
        List<TeamMember> teamMembers = new ArrayList<>();
        for (Object[] human : team){
            TeamMember member = new TeamMember();
            member.setId((String) human[0]);
            member.setEmail((String) human[1]);
            member.setRole(getObjRole((Integer) human[2]).name());
            teamMembers.add(member);
        }
        return teamMembers;
    }

    public Role getRole(int role){
        switch (role){
            case 0:
                return Role.ADMIN;
            case 1:
                return Role.READER;
            case 2:
                return Role.TEAMMATE;
        }
        return null;
    }

    public Role getObjRole(Integer role){
        switch (role){
            case 0:
                return Role.ADMIN;
            case 1:
                return Role.READER;
            case 2:
                return Role.TEAMMATE;
        }
        return null;
    }

}
