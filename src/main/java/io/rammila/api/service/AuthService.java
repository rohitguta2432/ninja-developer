package io.rammila.api.service;

import io.rammila.api.mapper.UserMapper;
import io.rammila.api.model.AuthToken;
import io.rammila.api.model.User;
import io.rammila.api.repository.AuthTokenRepository;
import io.rammila.api.utility.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthTokenRepository authTokenRepository;
    @Autowired
    private TokenUtils tokenUtils;

    public UserMapper generateAuthToken(User user) {
        Map<String, Object> dataMap = new HashMap<>();
        UserMapper userDetails = userService.loadUserByMobileAndPassword(user.getMobile(), user.getPassword());

        AuthToken authenticationToken = this.getAuthTokenByUserId(userDetails.getId());
        String token = "";
        boolean isExpired = false;
        if (ObjectUtils.isEmpty(authenticationToken)) {
            isExpired = true;
        }
        if (isExpired) {
            token = tokenUtils.generateToken(userDetails);
            AuthToken newAuthenticationToken = new AuthToken(userDetails.getId(), token);
            newAuthenticationToken = this.save(newAuthenticationToken);
        } else {
            token = authenticationToken.getToken();
        }
        userDetails.setToken(token);
        return userDetails;
    }

    public AuthToken getAuthTokenByUserId(UUID userId) {

        AuthToken authenticationToken = null;
        if (StringUtils.isEmpty(userId)) {
            log.info("Invalid user id.");
            throw new IllegalArgumentException("Invalid user id.");
        }
        List<AuthToken> authToken = authTokenRepository.findAllByUserId(userId);
        if (!ObjectUtils.isEmpty(authToken)) {
            authenticationToken = authToken.get(0);
        }

        return authenticationToken;
    }

    public AuthToken save(AuthToken authenticationToken) {

        if (authenticationToken == null) {
            log.info("Authentication object cannot be null/empty");
            throw new IllegalArgumentException("Authentication object cannot be null/empty");
        }

        if (StringUtils.isEmpty(authenticationToken.getUserId())) {
            log.info("Invalid user id.");
            throw new IllegalArgumentException("Invalid user id.");
        }

        if (StringUtils.isEmpty(authenticationToken.getToken())) {
            log.info("Authentication token cannot be null/empty");
            throw new IllegalArgumentException("Authentication token cannot be null/empty");
        }

        AuthToken existingToken = this.getAuthTokenByUserId(authenticationToken.getUserId());

        if (existingToken != null) {
            this.deleteAuthenticationToken(authenticationToken.getUserId());
        }
        return authTokenRepository.save(authenticationToken);
    }

    public boolean deleteAuthenticationToken(UUID userId) {

        if (StringUtils.isEmpty(userId)) {
            log.info("Invalid user id.");
            throw new IllegalArgumentException("Invalid user id.");
        }
        authTokenRepository.deleteByUserId(userId);
        return true;
    }
}

