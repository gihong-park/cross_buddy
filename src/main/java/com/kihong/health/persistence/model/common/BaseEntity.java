package com.kihong.health.persistence.model.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {

  @CreationTimestamp
  @CreatedDate
  private LocalDateTime createdAt;
  @UpdateTimestamp
  @LastModifiedDate
  private LocalDateTime updatedAt;

  @Column(name = "is_deleted", nullable = false, columnDefinition = "BIT(1) DEFAULT b'0'")
  @Builder.Default
  private boolean deleted = false;

  public boolean isDeleted() {
    return this.deleted;
  }

  public void setDeleted(boolean isDeleted) {
    this.deleted = isDeleted;
  }
}
