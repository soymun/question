package com.example.site.security;

import com.example.site.model.util.Role;
import com.example.site.service.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {

    private final UserServiceImpl userServiceImp;


    @Value("${jwt.token.security}")
    private String secret;

    @Value("${jwt.token.validate}")
    private Long validateInMillisecond;

    @Value("${jwt.token.validate}")
    private Long refreshValidateInMillisecond;

    @Override
    public void afterPropertiesSet() throws Exception {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public Pair<String, String> createToken(String email, Role role) {
        Claims claims = Jwts.claims().setSubject(email);

        Date issue = new Date();
        Date ex = new Date(validateInMillisecond + issue.getTime());
        Date refreshDate = new Date(refreshValidateInMillisecond + issue.getTime());

        String refresh = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issue)
                .setExpiration(refreshDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        Claims claimsRole = Jwts.claims().setSubject(email);
        claimsRole.put("role", getPermission(role));

        String access = Jwts.builder()
                .setClaims(claimsRole)
                .setIssuedAt(issue)
                .setExpiration(ex)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return Pair.of(access, refresh);
    }

    public List<String> getPermission(Role role) {
        List<String> strings = new ArrayList<>();
        role.getAuthority().forEach(n -> strings.add(n.getAuthority()));
        return strings;
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication authentication(String token) {
        UserDetails userDetails = userServiceImp.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            Object role = claimsJws.getBody().get("role");
            if (role != null) {
                return !claimsJws.getBody().getExpiration().before(new Date());
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailByRefreshToken(String token){
        if (token != null && token.startsWith("Bearer ")) {
            token =  token.substring(7);
        }
        if(validateRefreshToken(token)){
            return getEmail(token);
        } else {
            throw new RuntimeException("Невозможно авторизоваться");
        }
    }
}
