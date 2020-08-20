package io.rammila.api.mapper;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserMapper {
    private UUID id;
    private String mobile;
    private String firstName;
    private String lastName;
    private String fullName;
    private List<String> roles;
    private List<String> permissions;
    private Boolean isActive;
    private String token;
}
