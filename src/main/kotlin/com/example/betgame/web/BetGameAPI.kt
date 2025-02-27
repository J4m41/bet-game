package com.example.betgame.web

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import com.example.betgame.data.BetTransactionDTO
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/bets")
class BetGameAPI {

    /**
     * API Endpoint to place a new bet for the currently authenticated user
     * 
     * @param bet a DTO containing the bet data (target number and amount)
     * @return a DTO with the result of the bet
     */
    @PostMapping
    fun createBet(@RequestBody @Valid bet: BetTransactionDTO): ResponseEntity<BetTransactionDTO> {
        return ResponseEntity(HttpStatus.CREATED)
    }

    /**
     * API Endpoint that returns the bets of the currently authenticated user
     * 
     * @return a list of DTO bets
     */
    @GetMapping
    fun getBets(): ResponseEntity<List<BetTransactionDTO>> {
        return ResponseEntity(HttpStatus.OK)
    }
}