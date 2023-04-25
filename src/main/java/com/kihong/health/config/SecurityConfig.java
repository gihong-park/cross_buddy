package com.kihong.health.config;
import com.kihong.health.persistence.service.user.CustomUserDetailsService;
import com.kihong.health.web.secure.JwtAuthenticationFilter;
import com.kihong.health.web.secure.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig  {
  private final JwtTokenProvider jwtTokenProvider;
  private final CustomUserDetailsService userDetailsService;
  private final PasswordEncoder passwordEncoder;

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);

    return authProvider;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.cors()
//        .configurationSource(corsConfigurationSource())
        .and()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeHttpRequests()
        .requestMatchers(HttpMethod.GET, "/api/v1/user/signin", "/api/v1/user/signup", "/api/v1/movement", "/api/v1/wod", "/actuator/**", "/actuator")
        .permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/user/signin", "/api/v1/user/signup")
        .permitAll()
        .requestMatchers(HttpMethod.GET, "/api/v1/record")
        .hasRole("USER")
        .requestMatchers(HttpMethod.POST, "/api/v1/record")
        .hasRole("USER")
        .requestMatchers(HttpMethod.PUT, "/api/v1/record/**")
        .hasRole("USER")
        .requestMatchers("/api/v1/**", "/api/v1/admin/**")
        .hasRole("ADMIN")
        .and()
        .exceptionHandling()
        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
            UsernamePasswordAuthenticationFilter.class).authenticationProvider(authenticationProvider()).build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.addAllowedOrigin("*");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(false);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
