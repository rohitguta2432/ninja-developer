package io.rammila.api.repository;

import io.rammila.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query(value="SELECT * FROM roles WHERE id IN (SELECT role_id FROM user_roles WHERE user_id = :userId)", nativeQuery=true)
    List<Role> findByUserId(@Param("userId") UUID userId);
}
