package io.rammila.api.service;

import io.rammila.api.mapper.UserMapper;
import io.rammila.api.model.*;
import io.rammila.api.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    public User save(User user) {
        log.info("user save :{} ", user);
        if (!ObjectUtils.isEmpty(user.getId())) {
            Optional<User> users = userRepository.findById(user.getId());
            users.get().setFirstName(user.getFirstName());
            users.get().setFullName(user.getLastName());
            users.get().setFullName(user.getFullName());
            return userRepository.save(users.get());
        }
        return userRepository.save(user);
    }

    public UserMapper loadUserByMobileAndPassword(String mobile, String password) {
        Optional<User> user = userRepository.findByMobile(mobile);

        if (user.get() == null) {
            log.info("No user found with username.");
            throw new UsernameNotFoundException("No user found with username.");
        }
        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            log.info("Invalid Credential..");
            throw new UsernameNotFoundException("Invalid Credential..");
        }
        return getUserRole(user.get());
    }

    public UserMapper getUserRole(User user) {

        List<Role> roles = roleRepository.findByUserId(user.getId());
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        List<UUID> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<RolePermission> rolePermission = rolePermissionRepository.findByRoleIdIn(roleIds);
        List<UUID> permissionId = rolePermission.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        List<Permission> permissions = permissionRepository.findAllByIdIn(permissionId);
        List<String> authority = permissions.parallelStream().map(Permission::getName).collect(Collectors.toList());

        UserMapper userMapper = UserMapper.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .isActive(user.getStatus())
                .mobile(user.getMobile())
                .permissions(authority)
                .roles(roleNames)
                .build();
        return userMapper;
    }

    public User findUserByMobile(String mobile) {
        Optional<User> user = userRepository.findByMobile(mobile);
        return user.orElse(null);
    }

    public List<String> getUserRoleForAuthFilter(UUID userId) {

        List<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
        List<UUID> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RolePermission> rolePermission = rolePermissionRepository.findByRoleIdIn(roleIds);
        List<UUID> permissionId = rolePermission.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        List<Permission> permissions = permissionRepository.findAllByIdIn(permissionId);
        List<String> authority = permissions.parallelStream().map(Permission::getName).collect(Collectors.toList());

        return authority;
    }
}
