package com.example.backend.config;

import com.example.backend.domain.User;
import com.example.backend.service.user.UserDto;
import com.example.backend.utils.enums.AppRoles;
import com.example.backend.utils.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class JwtTokenService {

    private static final long EXPIRATIONTIME = Duration.ofDays(3).toMillis();
    private static final String HEADER_STRING = "app-auth";
    private static final String CLAIM_USER = "user";
    private static final String CLAIM_ROLES = "roles";
    @Value("${application.secret}")
    private String secret;

    public Authentication getAuthentication(final HttpServletRequest request) {
        Authentication auth = null;
        // we still need the old header for various external services calling gfs
        final String token = Optional.ofNullable(request.getHeader(HEADER_STRING)).orElse(null);
        try {
            // use token != null && token.isBlack() if jdk11
            if (token != null && !("").equals(token)) {
                // parse the token.
                Jws<Claims> claims;
                claims = Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8))
                        .parseClaimsJws(token);
                if (null != claims) {
                    // Check if there is a userId present
                    final String userId = Optional//
                            .ofNullable(claims.getBody().get(CLAIM_USER))//
                            .map(Object::toString)//
                            .orElseThrow(
                                    () -> new AuthenticationCredentialsNotFoundException("No username given in jwt"));
                    // check roles
                    String role = Optional//
                            .ofNullable(claims.getBody().get(CLAIM_ROLES))//
                            .map(Object::toString)//
                            .orElseThrow(
                                    () -> new AuthenticationCredentialsNotFoundException("No roles given in jwt"));

                    // [ADMIN] -> ADMIN
                    role = role.replaceAll("\\[|\\]", "");
                    ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(role));
                    auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
                }
            }

        } catch (final SignatureException | MalformedJwtException | UnsupportedJwtException ex) {
            log.error("Unsupported jwt token {} with exception {}",
                    token,
                    ex.getMessage());
            throw new JwtAuthenticationException(ex);
        } catch (final ExpiredJwtException ex) {
            log.error("Expired jwt token {}",
                    ex.getMessage());
            throw new JwtAuthenticationException(ex);
        } catch (final AuthenticationCredentialsNotFoundException ex) {
            log.error("An error occured while trying to create authentication based on jwt token, missing credentials {}",
                    ex.getMessage());
            throw new JwtAuthenticationException(ex);
        } catch (final Exception ex) {
            log.error( "Unexpected exception occured while parsing jwt {} exception: {}",
                    token,
                    ex.getMessage());
            throw new JwtAuthenticationException(ex);
        }

        log.debug("The authentication constructed by the JwtService");
        return auth;
    }

    public String createJwtToken(final UserDto user, final Set<AppRoles> roles) {
        // create the jwt token
        String jwtToken;

        user.setPassword(null);

        jwtToken = Jwts.builder()//
                .claim(CLAIM_USER, user)//
                .claim(CLAIM_ROLES, new ArrayList<>(roles))//
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))//
                .signWith(SignatureAlgorithm.HS512, secret.getBytes(StandardCharsets.UTF_8))//
                .compact();//
        return jwtToken;
    }

    public User getUserFromToken(String token) {
        try {
            DecodedJWT decoded = DecodedJWT.getDecoded(token);
            return decoded.user;
        } catch (UnsupportedEncodingException e) {
            System.out.println("oof");
            return null;
        }
    }
}
