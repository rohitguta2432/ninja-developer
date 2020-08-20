package io.rammila.api.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.rammila.api.exception.GlobalException;
import io.rammila.api.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenUtils {

    @Value("${rammila.token.secret}")
    private String secret;

    public String getMobileFromToken(String token) {
        String mobile = null;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            if(!ObjectUtils.isEmpty(claims)){
                mobile = claims.getSubject();
            }else{
                throw new GlobalException(String.format("Invalid or expired authorization token : %s",
                        token));
            }
        } catch (Exception e) {
            throw new GlobalException(String.format("Invalid or expired authorization token : %s",
                    token));
        }
        return mobile;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
    public String generateToken(UserMapper userDetails) {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("sub", userDetails.getMobile());
        claims.put("created", this.generateCurrentDate());
        return this.generateToken(claims);
    }
    private Date generateCurrentDate() {
        return new Date(System.currentTimeMillis());
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS512, this.secret).compact();
    }
}
