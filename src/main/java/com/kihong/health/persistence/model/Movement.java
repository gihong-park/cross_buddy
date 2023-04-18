package com.kihong.health.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "movement")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Movement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "movement_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  private String description;
}