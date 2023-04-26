package com.kihong.health.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

  @Override
  public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializerProvider)
      throws IOException {
    gen.writeStartArray();
    errors.getFieldErrors().forEach(e -> {
      try {
        gen.writeStartObject();
        gen.writeStringField("objectName", e.getObjectName());
        gen.writeStringField("field", e.getField());
        gen.writeStringField("code", e.getCode());
        gen.writeStringField("defaultMessage", e.getDefaultMessage());
        Object rejectedValue = e.getRejectedValue();
        if (rejectedValue != null) {
          gen.writeStringField("rejectedValue", rejectedValue.toString());
        }
        gen.writeEndObject();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });

    errors.getGlobalErrors().forEach(e -> {
      try {
        gen.writeStartObject();
        gen.writeStringField("objectName", e.getObjectName());
        gen.writeStringField("code", e.getCode());
        gen.writeStringField("defaultMessage", e.getDefaultMessage());
        gen.writeEndObject();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    });
    gen.writeEndArray();
  }
}