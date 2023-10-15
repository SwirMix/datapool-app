package org.datapool.repository;

import org.datapool.dto.db.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersJpaRepository extends JpaRepository<Users, String> {
    Optional<Users> findByEmailAndLogin(String email, String login);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByLogin(String login);
}
