package com.kihong.health.persistence.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kihong.health.persistence.model.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class User extends BaseEntity implements UserDetails {

  @Builder.Default
  private final boolean accountNonExpired = true;
  @Builder.Default
  private final boolean accountNonLocked = true;
  @Builder.Default
  private final boolean credentialsNonExpired = true;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;
  private String username;
  @Column(unique = true)
  private String email;
  private String password;
  private LocalDate birthDay;

  @Builder.Default
  private LocalDateTime lastLogined = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
  private Gender gender;
  @Builder.Default
  private Role role = Role.USER;
  @Builder.Default
  @JsonIgnore
  private boolean enabled = true;

  public String getGender() {
    if (gender == null) {
      return "null";
    } else {
      return this.gender.getGenderName();
    }
  }

  @JsonIgnore
  @Override
  public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
    if (this.role.getRoleName() == Role.USER.getRoleName()) {
      return Collections.singleton(
          new SimpleGrantedAuthority(this.role.getRoleName()));
    } else if (this.role.getRoleName()
        .equals(Role.ADMIN.getRoleName())) {
      Set<GrantedAuthority> adminAuthorites = new HashSet<>();
      adminAuthorites.add(new SimpleGrantedAuthority(Role.USER.getRoleName()));
      adminAuthorites.add(new SimpleGrantedAuthority(Role.ADMIN.getRoleName()));
      return adminAuthorites;
    } else if (this.role.getRoleName()
        .equals(Role.MASTER.getRoleName())) {
      Set<GrantedAuthority> masterAuthorities = new HashSet<>();
      masterAuthorities.add(
          new SimpleGrantedAuthority(Role.USER.getRoleName()));
      masterAuthorities.add(
          new SimpleGrantedAuthority(Role.ADMIN.getRoleName()));
      masterAuthorities.add(
          new SimpleGrantedAuthority(Role.MASTER.getRoleName()));
      return masterAuthorities;
    }

    return Collections.singleton(
        new SimpleGrantedAuthority(this.role.getRoleName()));
  }

  public User update(String username) {
    this.username = username;

    return this;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MASTER("ROLE_MASTER");

    private String roleName;

    Role(String roleName) {
      this.roleName = roleName;
    }

    public String getRoleName() {
      return this.roleName;
    }
  }


}
