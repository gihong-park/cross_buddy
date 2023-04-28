package com.kihong.health.util;

import java.time.LocalDate;

public class DateTemper {

  static public LocalDate getFirstDayOfWeek(LocalDate date) {
    return date.minusDays(date.getDayOfWeek().getValue() - 1);
  }

  static public LocalDate getLastDayOfWeek(LocalDate date) {
    return date.plusDays(7 - date.getDayOfWeek().getValue());
  }
}
