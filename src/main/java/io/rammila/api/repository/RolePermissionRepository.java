package io.rammila.api.repository;

import io.rammila.api.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {
    List<RolePermission> findByRoleIdIn(List<UUID> roleId);
    RolePermission findByRoleIdAndAndPermissionId(UUID roleId,UUID permissionId);
}
