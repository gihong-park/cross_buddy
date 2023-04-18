package com.kihong.health;

import com.kihong.health.common.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@Profile("test")
class HealthApplicationTests extends BaseControllerTest {

	@Test
	void contextLoads() {
	}
}
