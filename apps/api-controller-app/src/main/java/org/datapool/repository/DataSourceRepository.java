package org.datapool.repository;

import org.datapool.dto.db.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSource, String> {
   List<DataSource> findByProjectId(String projectId);
}
