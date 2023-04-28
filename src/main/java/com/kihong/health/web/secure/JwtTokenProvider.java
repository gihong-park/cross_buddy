package com.kihong.health.web.secure;

import com.kihong.health.persistence.dto.user.TokenInfo;
import com.kihong.health.persistence.service.user.CustomUserDetailsService;
import com.kihong.health.web.exception.ErrorCode;
import com.kihong.health.web.exception.HttpException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider { // JWT토큰 생성 및 유효성을 검증하는 컴포넌트

  @Value("spring.jwt.secret")
  private String SECRET_KEY;

  private final CustomUserDetailsService userDetailsService;

  @PostConstruct
  protected void init() {
    SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
  }

  // Jwt 토큰 생성
  public TokenInfo createToken(String userPk,
      Collection<? extends GrantedAuthority> roles, long expired) {
    Claims claims = Jwts.claims().setSubject(userPk);
    List<String> _roles = new ArrayList<>();
    _roles.add("USER");

    claims.put("roles", _roles);
    Date now = new Date();
    String accessToken = Jwts.builder()
        .setClaims(claims) // 데이터
        .setIssuedAt(now)  // 토큰 발행일자
        .setExpiration(
            new Date(now.getTime() + expired)) // set Expire Time
        .signWith(SignatureAlgorithm.HS256,
            SECRET_KEY) // 암호화 알고리즘, secret값 세팅
        .compact();

    String refreshToken = Jwts.builder()
        .setClaims(claims) // 데이터
        .setIssuedAt(now)  // 토큰 발행일자
        .setExpiration(
            new Date(now.getTime() + expired * 30)) // set Expire Time
        .signWith(SignatureAlgorithm.HS256,
            SECRET_KEY) // 암호화 알고리즘, secret값 세팅
        .compact();

    return TokenInfo.builder()
        .grantType("Bearer")
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

  // JWT 토큰에서 인증 정보 조회
  public Authentication getAuthentication(String token) {
    UserDetails userDetails =
        userDetailsService.loadUserByUsername(this.getUserPk(token));
    return new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities());
  }

  // 토큰에서 회원 정보 추출
  public String getUserPk(String token) {
    return Jwts.parser()
        .setSigningKey(SECRET_KEY)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "Bearer ${TOKEN 값}'
  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  // 토큰의 유효성 + 만료일자 확인
  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claims =
          Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (SignatureException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
      throw new HttpException(ErrorCode.TOKEN_EXPIRED, ErrorCode.TOKEN_EXPIRED.getDetail());
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }
}