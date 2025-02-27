package com.example.betgame

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

import com.example.betgame.utils.log

@SpringBootTest
class BetGameApplicationTests {

	@Test
	fun contextLoads() {
		log.info("Context loaded!!!")
	}

}
