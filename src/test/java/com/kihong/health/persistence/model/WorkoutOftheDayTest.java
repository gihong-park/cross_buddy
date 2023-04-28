package com.kihong.health.persistence.model;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WorkoutOftheDayTest {

  Long id = Long.valueOf(1);
  String value = "value";
  String description = """
      #Warm Ups
      3 Rounds 45 ON : 15 OFF
      • Jumping Jacks
      • Front Rack Mob. L R / Front SO
      • Hollow Arch Rolls 5
            
      #Skills
      \s
      #Conditionings
      5 rounds for time of:
      9 Hang Power Cleans, 155/105 lbs
      21 Toes-to-bars
      Rest 1 min
      Time cap: 16 mins (including rest)
      Goal: 9-10 mins (not including rest)
      Level 3: Prescribed
      Level 2: 135/95 Ibs, 15 T2B
      Level 1: 95/65 Ibs or 1RM Power Clean 55%
      15 Knee Raise or Midlines
      """;
  boolean isDeleted = false;
  String type = "EMOM";
  String name = "Strong Body";
  LocalDate now = LocalDate.now();

  WorkoutOftheDay wod;

  @BeforeEach
  public void beforeEach() {
    wod = WorkoutOftheDay.builder()
        .id(id)
        .type(type)
        .name(name)
        .description(description)
        .date(now)
        .build();
  }

  @Test
  @DisplayName("WOD_GET_TEST")
  public void wodGetTest() {
    Assertions.assertEquals(now, wod.getDate());
    Assertions.assertEquals(id, wod.getId());
    Assertions.assertEquals(name, wod.getName());
    Assertions.assertEquals(type, wod.getType());
    Assertions.assertEquals(description, wod.getDescription());

    Assertions.assertNull(wod.getCreatedAt());
    Assertions.assertNull(wod.getUpdatedAt());
  }

  @Test
  @DisplayName("WOD_SET_TEST")
  public void wodSetTest() {
    Long setId = Long.valueOf(2);
    String setName = "set name";
    String setType = "set Type";
    String setDescription = """
        #Warm Ups
        3 Rounds 45 ON : 15 OFF
        • Jumping Jacks
        • Front Rack Mob. L R / Front SO
        • Hollow Arch Rolls 5
              
        #Skills
        \s
        #Conditionings
        5 rounds for time of:
        9 Hang Power Cleans, 155/105 lbs
        21 Toes-to-bars
        Rest 1 min
        Time cap: 16 mins (including rest)
        Goal: 9-10 mins (not including rest)
        Level 3: Prescribed
        Level 2: 135/95 Ibs, 15 T2B
        Level 1: 95/65 Ibs or 1RM Power Clean 55%
        15 Knee Raise or Midlines 
        """;
    boolean setIsDeleted = true;
    LocalDate setDate = LocalDate.now();

    wod.setId(setId);
    wod.setName(setName);
    wod.setType(setType);
    wod.setDescription(setDescription);
    wod.setDate(setDate);
    wod.setDeleted(setIsDeleted);

    Assertions.assertEquals(setId, wod.getId());
    Assertions.assertEquals(setName, wod.getName());
    Assertions.assertEquals(setType, wod.getType());
    Assertions.assertEquals(setDescription, wod.getDescription());
    Assertions.assertEquals(setDate, wod.getDate());
    Assertions.assertEquals(setIsDeleted, wod.isDeleted());
  }
}
