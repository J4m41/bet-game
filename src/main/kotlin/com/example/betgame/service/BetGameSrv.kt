package com.example.betgame.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import com.example.betgame.data.BetTransactionRepository
import com.example.betgame.data.BetTransactionDTO

@Service
class BetGameSrv {

    @Autowired
    lateinit var betTransactionRepository: BetTransactionRepository

    fun findAllBets(): List<BetTransactionDTO> {
        val authentication = SecurityContextHolder.getContext().authentication

        return listOf()
    }
}