package io.rammila.api.repository;

import io.rammila.api.model.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, UUID> {
    List<AuthToken> findAllByUserId(UUID userId);
    void deleteByUserId(UUID userId);

}
