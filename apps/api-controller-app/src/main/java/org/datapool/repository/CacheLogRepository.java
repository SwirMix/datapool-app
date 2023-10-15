package org.datapool.repository;

import org.datapool.dto.db.CacheLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CacheLogRepository extends PagingAndSortingRepository<CacheLog, String> {

    @Query("select l from CacheLog l where l.id.projectId = ?1 and l.id.cacheName = ?2")
    public List<CacheLog> getCacheLogByCache(String projectId, String cacheName);

    @Query("select l from CacheLog l where l.id.projectId = ?1")
    public List<CacheLog> getCacheLogByProject(String projectId);

    @Query(value = "select * from last_cache_actions where project_id = ?1 and cache_name = ?2 and type = ?3", nativeQuery = true)
    public CacheLog getLastDataView(String projectId, String cache_name, String type);

    public Page<CacheLog> findByProjectId(String project, Pageable pageable);

}
