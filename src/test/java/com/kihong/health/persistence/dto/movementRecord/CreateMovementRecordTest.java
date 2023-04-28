package com.kihong.health.persistence.dto.movementRecord;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateMovementRecordTest {

  Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  @DisplayName("VALIDATE CREATE MOVEMENTRECORD DTO")
  void validateMovementRecord() {
    CreateMovementRecord createMovementRecord = CreateMovementRecord.builder().cal(100).build();
    Set<ConstraintViolation<CreateMovementRecord>> validate = validator.validate(
        createMovementRecord);

    assertEquals(validate.size(), 1);
    assertEquals(
        "[ConstraintViolationImpl{interpolatedMessage='must not be empty', propertyPath=name, rootBeanClass=class com.kihong.health.persistence.dto.movementRecord.CreateMovementRecord, messageTemplate='{jakarta.validation.constraints.NotEmpty.message}'}]",
        validate.toString());
    assertEquals("name", validate.stream().findFirst().get().getPropertyPath().toString());
    assertEquals("must not be empty", validate.stream().findFirst().get().getMessage());
  }

}