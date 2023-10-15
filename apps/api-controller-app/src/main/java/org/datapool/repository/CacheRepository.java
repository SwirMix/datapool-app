package org.datapool.repository;

import org.datapool.dto.db.Cache;
import org.datapool.dto.db.CacheKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository extends JpaRepository<Cache, CacheKey> {
}
