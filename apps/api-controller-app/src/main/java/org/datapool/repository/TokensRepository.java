package org.datapool.repository;

import org.datapool.dto.db.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokensRepository extends JpaRepository<Token, String> {
    List<Token> findByProjectId(String projectId);

}
