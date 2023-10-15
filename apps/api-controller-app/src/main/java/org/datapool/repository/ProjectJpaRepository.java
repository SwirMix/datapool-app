package org.datapool.repository;

import org.datapool.dto.db.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, String> {
    public Optional<Project> findByName(String name);
}
