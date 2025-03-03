package com.example.betgame

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpEntity
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.security.test.context.support.WithMockUser
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance

import com.example.betgame.utils.log
import com.example.betgame.data.UserReadDTO
import com.example.betgame.data.AuthTokenDTO
import com.example.betgame.data.BetTransactionDTO

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BetGameApplicationTests {

	companion object {
        const val TEST_USERNAME = "testuser"
        const val TEST_PASSWORD = "password123"
    }

    private lateinit var jwtToken: String

    @Autowired
    lateinit var restTemplate: TestRestTemplate

	/**
	 * Setup the test environment by registering a user and logging in to get a JWT token
	 */
    @BeforeAll
    fun setup() {
        // Register user
        val registerResponse = restTemplate.postForEntity(
            "/auth/register",
            mapOf(
                "username" to TEST_USERNAME, 
                "password" to TEST_PASSWORD,
                "firstName" to "first_name",
                "lastName" to "last_name"),
			UserReadDTO::class.java
        )
        assert(registerResponse.statusCode == HttpStatus.OK)

        // Login to get JWT token
        val loginResponse = restTemplate.postForEntity(
            "/auth/authenticate",
            mapOf(
				"username" to TEST_USERNAME, 
				"password" to TEST_PASSWORD),
			AuthTokenDTO::class.java
        )
        assert(loginResponse.statusCode == HttpStatus.OK)

		log.info("test register and authenticate COMPLETE")
        log.info("Response body: ${loginResponse.body}")

		// Extract JWT token from response body (assume response is a JSON with token field)
		val token = loginResponse.body?.token ?: throw IllegalStateException("Token not found")
		jwtToken = "Bearer $token"
    }

	/**
	 * Test the protected API endpoint after successful authentication
	 */
    @Test
    fun `test protected API endpoint after successful authentication`() {
		
        val headers = HttpHeaders()
        headers.set("Authorization", jwtToken)
        val entity = org.springframework.http.HttpEntity<Void>(headers)

        val response: ResponseEntity<UserReadDTO> = restTemplate.exchange(
            "/api/me",
            HttpMethod.GET,
            entity,
            UserReadDTO::class.java
        )

        assert(response.statusCode == HttpStatus.OK)

		log.info("test protected API endpoint after successful authentication COMPLETE")
        log.info("Response body: ${response.body}")
    }

	/**
	 * Test the betting API and game logics to win 10 times the bet amount on exact match
	 */
	@Test
	fun `test exact match wins 10 times bet`() {

		var betNumber = 5
		var betAmount = 10L
		
		val headers = HttpHeaders().apply {
			set("Authorization", jwtToken)
			contentType = MediaType.APPLICATION_JSON
		}
		
		val requestBody = mapOf(
			"betAmount" to betAmount,
			"betNumber" to betNumber
		)
		
		val entity = HttpEntity(requestBody, headers)
		
		var betResultNumber: Int
		var winAmount: Long
		var tries = 0

		do {
			// Keep on betting until bet result number is eq to bet number
			val betResponse = restTemplate.postForEntity(
				"/api/bets",
				entity,
				BetTransactionDTO::class.java
			)
			assert(betResponse.statusCode == HttpStatus.OK)
			tries++
			betResultNumber = betResponse.body?.betResultNumber!!
			winAmount = betResponse.body?.winAmount!!
		} while (betResultNumber != betNumber)

		log.info("betResultNumber: $betResultNumber after $tries tries")
		assert(betResultNumber == betNumber)
		assert(winAmount == (betAmount * 10))
		log.info("test exact match wins 10 times bet COMPLETE")
	}

	/**
	 * Test the betting API and game logics to win 5 times the bet amount on 1 number off
	 */
	@Test
	fun `test 1 number off wins 5 times bet`() {

		var betNumber = 5
		var betAmount = 10L
		
		val headers = HttpHeaders().apply {
			set("Authorization", jwtToken)
			contentType = MediaType.APPLICATION_JSON
		}
		
		val requestBody = mapOf(
			"betAmount" to betAmount,
			"betNumber" to betNumber
		)
		
		val entity = HttpEntity(requestBody, headers)
		
		var betResultNumber: Int
		var winAmount: Long
		var tries = 0

		do {
			// Keep on betting until bet result number is eq to bet number +/- 1
			val betResponse = restTemplate.postForEntity(
				"/api/bets",
				entity,
				BetTransactionDTO::class.java
			)
			assert(betResponse.statusCode == HttpStatus.OK)
			tries++
			betResultNumber = betResponse.body?.betResultNumber!!
			winAmount = betResponse.body?.winAmount!!
		} while (kotlin.math.abs(betNumber - betResultNumber) != 1)

		log.info("betResultNumber: $betResultNumber after $tries tries")
		assert(kotlin.math.abs(betNumber - betResultNumber) == 1)
		assert(winAmount == (betAmount * 5))
		log.info("test 1 number off wins 5 times bet COMPLETE")
	}

	/**
	 * Test the betting API and game logics to win half the bet amount on 2 numbers off
	 */
	@Test
	fun `test 2 numbers off wins half bet`() {

		var betNumber = 5
		var betAmount = 10L
		
		val headers = HttpHeaders().apply {
			set("Authorization", jwtToken)
			contentType = MediaType.APPLICATION_JSON
		}
		
		val requestBody = mapOf(
			"betAmount" to betAmount,
			"betNumber" to betNumber
		)
		
		val entity = HttpEntity(requestBody, headers)
		
		var betResultNumber: Int
		var winAmount: Long
		var tries = 0

		do {
			// Keep on betting until bet result number is eq to bet number +/- 2
			val betResponse = restTemplate.postForEntity(
				"/api/bets",
				entity,
				BetTransactionDTO::class.java
			)
			assert(betResponse.statusCode == HttpStatus.OK)
			tries++
			betResultNumber = betResponse.body?.betResultNumber!!
			winAmount = betResponse.body?.winAmount!!
		} while (kotlin.math.abs(betNumber - betResultNumber) != 2)

		log.info("betResultNumber: $betResultNumber after $tries tries")
		assert(kotlin.math.abs(betNumber - betResultNumber) == 2)
		assert(winAmount == (betAmount / 2))
		log.info("test 2 numbers off wins half bet COMPLETE")
	}

	/**
	 * Test the betting API and game logics to lose the bet amount on 3+ numbers off
	 */
	@Test
	fun `test 3 numbers off loses bet`() {

		var betNumber = 5
		var betAmount = 10L
		
		val headers = HttpHeaders().apply {
			set("Authorization", jwtToken)
			contentType = MediaType.APPLICATION_JSON
		}
		
		val requestBody = mapOf(
			"betAmount" to betAmount,
			"betNumber" to betNumber
		)
		
		val entity = HttpEntity(requestBody, headers)
		
		var betResultNumber: Int
		var winAmount: Long
		var tries = 0

		do {
			// Keep on betting until bet result number is gt bet number +2 or lt bet number -2
			val betResponse = restTemplate.postForEntity(
				"/api/bets",
				entity,
				BetTransactionDTO::class.java
			)
			assert(betResponse.statusCode == HttpStatus.OK)
			tries++
			betResultNumber = betResponse.body?.betResultNumber!!
			winAmount = betResponse.body?.winAmount!!
		} while (kotlin.math.abs(betNumber - betResultNumber) < 3)

		log.info("betResultNumber: $betResultNumber after $tries tries")
		assert(kotlin.math.abs(betNumber - betResultNumber) > 2)
		assert(winAmount == 0L)
		log.info("test 3+ numbers off loses bet COMPLETE")
	}
}
