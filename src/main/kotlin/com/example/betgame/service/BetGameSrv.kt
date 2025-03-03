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
        if (bet.betAmount > (balance - (bet.betAmount))) throw MaxBetAmountExceededEx("MBE_01", "Maximum bet amount exceeded")
        
        val randomNumber = Random.nextInt(1, 11)
        val winAmount = calculateWinnings(bet.betNumber, randomNumber, bet.betAmount)
        val betResult: BetResult = if (winAmount > 0) BetResult.WIN else BetResult.LOST

        user.walletBalance += winAmount
        userRepository.save(user)

        var entity = BetTransaction(null, null, null, bet.betAmount, bet.betNumber, betResult, randomNumber, winAmount, user)

        return betTransactionRepository.save(entity).toDTO()
    }

    fun me(): UserReadDTO = (SecurityContextHolder.getContext().authentication.principal as BetUser).toReadDTO()

    private fun calculateWinnings(bet: Int, generated: Int, betAmount: Long): Long {
        return when (kotlin.math.abs(bet - generated)) {
            0 -> betAmount * 10
            1 -> betAmount * 5
            2 -> betAmount / 2
            else -> 0
        }
    }
}