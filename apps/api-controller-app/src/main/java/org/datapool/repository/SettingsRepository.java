package org.datapool.repository;

import org.datapool.dto.db.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingsRepository extends JpaRepository<Setting, String> {
    public Setting findByName(String name);
}
