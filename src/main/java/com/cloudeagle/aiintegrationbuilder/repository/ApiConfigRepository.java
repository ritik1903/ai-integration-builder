package com.cloudeagle.aiintegrationbuilder.repository;

import com.cloudeagle.aiintegrationbuilder.entity.ApiConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ApiConfigRepository extends JpaRepository<ApiConfig, Long> {
    Optional<ApiConfig> findByName(String name);
}
