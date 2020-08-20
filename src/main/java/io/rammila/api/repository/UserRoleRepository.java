package io.rammila.api.repository;

import io.rammila.api.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    List<UserRole> findAllByUserId(UUID userId);
    UserRole findByRoleIdAndAndUserId(UUID roleId,UUID userId);
}
