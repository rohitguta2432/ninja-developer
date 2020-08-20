package io.rammila.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rammila.api.exception.GlobalException;
import io.rammila.api.model.User;
import io.rammila.api.service.UserService;
import io.rammila.api.utility.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${rammila.auth.header}")
    private String tokenHeader;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader(this.tokenHeader);

        if (!StringUtils.isEmpty(token)
                && SecurityContextHolder.getContext().getAuthentication() == null && token.startsWith("Bearer")) {
            try {
                String authToken = token.replace("Bearer ", "");

                String mobile = this.tokenUtils.getMobileFromToken(authToken);

                User userDetails = userService.findUserByMobile(mobile);
                List<String> authority = userService.getUserRoleForAuthFilter(userDetails.getId());

                final List<SimpleGrantedAuthority> authorities = new LinkedList<>();
                authority.stream().forEach(userRole -> {
                    authorities.add(new SimpleGrantedAuthority(userRole));
                });

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

                SecurityContextHolder.getContext().setAuthentication(authentication);


            } catch (Exception e) {
                log.error(e.getMessage());
                throw new GlobalException("Invalid or expired authorization token [" + e.getMessage() + "]");
            }
        }
        chain.doFilter(request, response);
    }
}
