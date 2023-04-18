package com.kihong.health.web.secure;

import com.kihong.health.persistence.service.user.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider { // JWT토큰 생성 및 유효성을 검증하는 컴포넌트

  @Value("spring.jwt.secret") private String SECRET_KEY;

  private final long tokenValidMilisecond = 1000L * 60 * 60 * 24 * 180;
  private final CustomUserDetailsService userDetailsService;

  @PostConstruct
  protected void init() {
    SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
  }

  // Jwt 토큰 생성
  public String createToken(String userPk,
      Collection<? extends GrantedAuthority> roles) {
    Claims claims = Jwts.claims().setSubject(userPk);
    List<String> _roles = new ArrayList<>();
    _roles.add("USER");

    claims.put("roles", _roles);
    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims) // 데이터
        .setIssuedAt(now)  // 토큰 발행일자
        .setExpiration(
            new Date(now.getTime() + tokenValidMilisecond)) // set Expire Time
        .signWith(SignatureAlgorithm.HS256,
            SECRET_KEY) // 암호화 알고리즘, secret값 세팅
        .compact();
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

  // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
  public String resolveToken(HttpServletRequest request) {
    return request.getHeader("Authorization");
  }

  // 토큰의 유효성 + 만료일자 확인
  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claims =
          Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }
}