package com.kihong.health.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateTemperTest {
  final LocalDate sunday = LocalDate.of(2023, 4, 9);
  final LocalDate saturday = LocalDate.of(2023, 4, 9);
  final LocalDate friday = LocalDate.of(2023, 4, 9);
  final LocalDate thursday = LocalDate.of(2023, 4, 9);
  final LocalDate wednesday = LocalDate.of(2023, 4, 9);
  final LocalDate tuesday = LocalDate.of(2023, 4, 9);
  final LocalDate monday = LocalDate.of(2023, 4, 3);

  @Test
  @DisplayName("DATETEMPER GET MONDAY OF WEEK")
  void getFirstDayOfWeek () {
    assertWeekEquals(monday, (date)-> DateTemper.getFirstDayOfWeek(date));
  }

  @Test
  @DisplayName("DATETEMPER GET SUNDAY OF WEEK")
  void getLastDayOfWeek() {
    assertWeekEquals(sunday, (date)-> DateTemper.getLastDayOfWeek(date));
  }

  void assertWeekEquals(LocalDate expectDate, Function<LocalDate, LocalDate> temper) {
    List<LocalDate> week = List.of(monday, tuesday, wednesday, thursday, friday, saturday, sunday);

    week.forEach(date -> assertEquals(expectDate, temper.apply(date)));
  }

}