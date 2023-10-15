package org.datapool.repository;

import org.datapool.dto.api.internal.UserProject;
import org.datapool.dto.db.PrPermissionKey;
import org.datapool.dto.db.ProjectPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPermissionRepository extends JpaRepository<ProjectPermissions, PrPermissionKey> {

    @Query("select " +
            "p.id," +
            "p.name," +
            "p.description," +
            "p.active," +
            "pp.id.userId, " +
            "pp.role, " +
            "p.created " +
            "from Project p join ProjectPermissions pp on p.id = pp.id.projectId where pp.id.userId = ?1"
    )
    public List<Object[]> selectUserProjects(String userId);

    @Query("select " +
            "p.id," +
            "p.name," +
            "p.description," +
            "p.active," +
            "pp.id.userId, " +
            "pp.role, " +
            "p.created " +
            "from Project p join ProjectPermissions pp on p.id = pp.id.projectId where pp.id.projectId = ?1"
    )
    public List<Object[]> selectProjectTeam(String projectId);

    @Query("select u.id, u.email, pp.role from ProjectPermissions pp join Users u on u.id = pp.id.userId where pp.id.projectId =?1")
    public List<Object[]> selectTeam(String projectId);

}
