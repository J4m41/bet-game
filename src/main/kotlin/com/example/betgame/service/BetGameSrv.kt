package com.example.betgame.service

import java.util.*
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.PageImpl
import org.springframework.transaction.annotation.Transactional
import com.example.betgame.data.BetTransaction
import com.example.betgame.data.BetTransactionRepository
import com.example.betgame.data.BetTransactionDTO
import com.example.betgame.data.QBetTransaction
import com.example.betgame.data.toDTO
import com.example.betgame.data.BetUserRepository
import com.example.betgame.data.MaxBetAmountExceededEx
import com.example.betgame.data.BetResult
import com.example.betgame.data.BetTransactionCreateDTO
import com.example.betgame.data.BetUser
import com.example.betgame.data.toReadDTO
import com.example.betgame.data.UserReadDTO

import com.example.betgame.utils.log
import kotlin.random.Random

@Service
@Transactional
class BetGameSrv {

    @Autowired
    lateinit var betTransactionRepository: BetTransactionRepository

    @Autowired
    lateinit var userRepository: BetUserRepository

    companion object {
        private val Q_BT = QBetTransaction.betTransaction
    }

    fun findAllBets(pageable: Pageable): Page<BetTransactionDTO> {
        val principal = SecurityContextHolder.getContext().authentication.principal as BetUser
        var userId: UUID = principal.id!!
        var betsPage = betTransactionRepository.findAllByBetUserId(userId, pageable)
        var betsDTOList = betsPage.content.map { it -> it.toDTO() }
        return PageImpl<BetTransactionDTO>(betsDTOList, pageable, betsPage.totalElements)
    }

    fun createBet(bet: BetTransactionCreateDTO): BetTransactionDTO {
        val principal = SecurityContextHolder.getContext().authentication.principal as BetUser
        val user = userRepository.findByUsername(principal.username)
        var balance = user.walletBalance
        var betResult: BetResult
        var winAmount: Long
        if (bet.betAmount > (balance - (bet.betAmount))) throw MaxBetAmountExceededEx("MBE_01", "Maximum bet amount exceeded")
        
        val randomNumber = Random.nextInt(1, 11)  // Generates a number between 1 and 10 (inclusive)
        
        if (randomNumber < (bet.betNumber - 2) || randomNumber > (bet.betNumber + 2)) {
            betResult = BetResult.LOST
            winAmount = -bet.betAmount
        } else if (randomNumber == (bet.betNumber - 2) || randomNumber == (bet.betNumber + 2)) {
            betResult = BetResult.WIN
            winAmount = bet.betAmount / 2
        } else if (randomNumber == (bet.betNumber - 1) || randomNumber == (bet.betNumber + 1)) {
            betResult = BetResult.WIN
            winAmount = bet.betAmount * 5
        } else {
            betResult = BetResult.WIN
            winAmount = bet.betAmount * 10
        }

        balance = balance + winAmount
        user.walletBalance = balance

        userRepository.save(user)
        var entity = BetTransaction(null, null, null, bet.betAmount, bet.betNumber, betResult, randomNumber, winAmount, user)

        return betTransactionRepository.save(entity).toDTO()
    }

    fun me(): UserReadDTO = (SecurityContextHolder.getContext().authentication.principal as BetUser).toReadDTO()
}