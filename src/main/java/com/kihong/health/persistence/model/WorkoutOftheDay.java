package com.kihong.health.persistence.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kihong.health.persistence.dto.workoutOftheDay.CreateWorkoutOftheDay;
import com.kihong.health.persistence.model.common.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

@SuperBuilder
@Entity
@Table(name = "workoutOftheDay")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutOftheDay extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "wod_id")
  private Long id;

  private String name;
  private String type;
  private String description;
  private LocalDate date;
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @JsonManagedReference(value = "wod")
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "wod_id", insertable = false, updatable = false, nullable = true)
  @JsonProperty("movement_records")
  private List<MovementRecord> movementRecords;
  @Type(JsonType.class)
  @Column(columnDefinition = "json")
  private HashMap<String, Object> result;

  public static WorkoutOftheDay getValueFrom(CreateWorkoutOftheDay cwod) {
    return WorkoutOftheDay.builder().date(cwod.getDate()).description(cwod.getDescription())
        .name(cwod.getName()).type(cwod.getType()).result(cwod.getResult()).build();
  }
}
