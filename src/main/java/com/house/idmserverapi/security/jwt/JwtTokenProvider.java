package com.house.idmserverapi.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.house.idmserverapi.entity.AppUser;
import com.house.idmserverapi.entity.dto.UserDto;
import com.house.idmserverapi.service.DataService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.joining;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "roles";

    public static final String HEADER_PREFIX = "Bearer ";

    private final JwtProperties jwtProperties;
    private final Converter<UserDto, AppUser> converter;
    private final DataService<AppUser, String> service;

    private Key secretKey;


    @PostConstruct
    protected void init() {
        final String secret = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Map<String, String> createToken(final Authentication authentication) {
        log.info("in progress method createToken jwt token provider: {}", authentication);

        final JwtUser user = (JwtUser) authentication.getPrincipal();
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        final Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("nickName", user.getNickName());
        claims.put(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));

        return getToken(claims);
    }


    public Authentication getAuthentication(final String token) {
        log.info("in progress method getAuthentication jwt token provider: {}", token);

        final Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token)
                .getBody();
        final Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get(AUTHORITIES_KEY).toString());
        final User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(final String token) {
        log.info("in progress method validateToken jwt token provider: {}", token);

        try {
            final Jws<Claims> claims = Jwts
                    .parserBuilder().setSigningKey(this.secretKey).build()
                    .parseClaimsJws(token);
            //  parseClaimsJws will check expiration date. No need do here.
            log.info("expiration date: {}", claims.getBody().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    public String resolveToken(final ServerHttpRequest request) {
        log.info("in progress method resolveToken jwt token provider: {}", request);

        final String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Mono<Map<String, String>> refreshToken(final String refreshToken) {
        log.info("in progress method resolveToken jwt token provider: {}", refreshToken);

        final Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(refreshToken)
                .getBody();

        return Mono.just(getToken(claims));

    }


    @SneakyThrows
    public Mono<Map<String, String>> validationTokenGoogle(final String token) {
        log.info("in progress method validationTokenGoogle jwt token provider: {}", token);

        final ObjectMapper mapper = new ObjectMapper();

        final String[] chunks = token.split("\\.");
        final Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload;

        if (chunks.length > 1) {
            payload = new String(decoder.decode(chunks[1]));

            final Mono<UserDto> userDtoMono = Mono.just(mapper.readValue(payload, UserDto.class));
            return userDtoMono.flatMap(userDto -> service
                    .register(converter.convert(userDto))
                    .doOnError(throwable -> log.error("unsuccessful parsing processing: ", throwable))
                    .map(appUser -> {
                        Claims claims = Jwts.claims().setSubject(appUser.getId().toString());
                        claims.put("roles", appUser.getStatus());
                        claims.put("nickName", appUser.getNickName());
                        return getToken(claims);
                    }));
        }
        return Mono.just(Collections.emptyMap());
    }

    private Map<String, String> getToken(final Claims claims) {

        final Map<String, String> tokenInformationMap = new HashMap<>();
        final Date now = Date.from(Instant.now().atZone(ZoneId.of(ZoneOffset.UTC.getId())).toInstant());
        final String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TimeUnit.MINUTES.toMillis(jwtProperties.getValidityInMin())))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        tokenInformationMap.put(jwtProperties.getNameAccessToken(), token);

        final String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getValidityRefreshInMin() + TimeUnit.MINUTES.toMillis(jwtProperties.getValidityInMin())))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        tokenInformationMap.put(jwtProperties.getNameRefreshToken(), refreshToken);

        return tokenInformationMap;
    }

}
