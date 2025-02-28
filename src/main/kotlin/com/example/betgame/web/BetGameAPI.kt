package com.example.betgame.web

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page
import org.springframework.data.web.PageableDefault
import com.example.betgame.data.BetTransactionDTO
import com.example.betgame.data.BetTransactionCreateDTO
import com.example.betgame.data.UserReadDTO
import com.example.betgame.service.BetGameSrv
import jakarta.validation.Valid

@RestController
@RequestMapping("/api")
class BetGameAPI @Autowired constructor(
    private val betGameSrv: BetGameSrv
) {

    /**
     * API Endpoint to place a new bet for the currently authenticated user
     * 
     * @param bet a DTO containing the bet data (target number and amount)
     * @return a DTO with the result of the bet
     */
    @PostMapping("/bets")
    fun createBet(@Valid @RequestBody bet: BetTransactionCreateDTO): BetTransactionDTO = betGameSrv.createBet(bet)

    /**
     * API Endpoint that returns the bets of the currently authenticated user
     * 
     * @return a list of DTO bets
     */
    @GetMapping("/bets")
    fun getBets(@PageableDefault(page = 0, size = 25) pageable: Pageable): Page<BetTransactionDTO> = betGameSrv.findAllBets(pageable)

    /**
     * API Endpoint that returns the current logged user info, including wallet balance
     * 
     * @return a DTO containing the currently authenticated user details
     */
    @GetMapping("/me")
    fun me(): UserReadDTO = betGameSrv.me()
}