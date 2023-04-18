package com.kihong.health.persistence.dto.record;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CreateRecordTest {

  @Autowired
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  @DisplayName("CREATE RECORD DTO VALIDATE TEST")
  void validateCreateRecord() {
    CreateRecord createRecord = CreateRecord.builder().build();
    Set<ConstraintViolation<CreateRecord>> cr = validator.validate(createRecord);

    assertEquals(cr.size(), 1);
    assertEquals(
        "[ConstraintViolationImpl{interpolatedMessage='must not be null', propertyPath=date, rootBeanClass=class com.kihong.health.persistence.dto.record.CreateRecord, messageTemplate='{jakarta.validation.constraints.NotNull.message}'}]",
        cr.toString());
  }
}