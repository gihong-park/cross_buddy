package com.kihong.health.test;

import com.kihong.health.common.BaseControllerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BasicTest extends BaseControllerTest {

  @Test
  @DisplayName("easy test")
  void someTest() {
    int a = 10;
    Assertions.assertEquals(10, a);
  }
}
