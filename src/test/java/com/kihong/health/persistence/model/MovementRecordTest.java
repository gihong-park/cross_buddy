package com.kihong.health.persistence.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MovementRecordTest {

  @Test
  @DisplayName("MOVEMENT_BUILD_TEST")
  public void movementRecordTest() {
    MovementRecord movementRecord = MovementRecord.builder().id(Long.valueOf(1)).build();
    Assertions.assertEquals(1, movementRecord.getId());
    Assertions.assertEquals(0, movementRecord.getCal());
    Assertions.assertEquals(0, movementRecord.getReps());
    Assertions.assertEquals(0, movementRecord.getDistance());
    Assertions.assertEquals(0, movementRecord.getWeight());
    Assertions.assertEquals(0, movementRecord.getHeight());

    Assertions.assertNull(movementRecord.getCreatedAt());
    Assertions.assertNull(movementRecord.getUpdatedAt());
  }
}