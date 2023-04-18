package com.kihong.health.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kihong.health.persistence.dto.record.CreateRecord;
import com.kihong.health.persistence.model.common.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Record extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "record_id")
  private Long id;
  @ManyToOne(targetEntity = User.class)
  @JoinColumn(name = "user_id")
  private User user;
  @ManyToOne(targetEntity = WorkoutOftheDay.class)
  @JoinColumn(name = "wod_id")
  private WorkoutOftheDay wod;

  private LocalDate date;
  private String note;
  @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
  @JsonManagedReference(value = "record")
  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "record_id", insertable = false)
  private List<MovementRecord> movementRecords;
  @Type(JsonType.class)
  @Column(columnDefinition = "json")
  private HashMap<String, Object> result;

  static public Record getValueFrom(CreateRecord cr) {
    return Record.builder().note(cr.getNote()).date(cr.getDate()).result(cr.getResult()).build();
  }
}
