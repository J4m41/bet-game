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
import com.example.betgame.data.LeaderboardDTO
import com.example.betgame.data.QBetUser    

import com.example.betgame.utils.log
import com.example.betgame.utils.Loggable
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.core.Tuple
import kotlin.random.Random
import jakarta.persistence.PersistenceContext
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException

@Service
@Transactional
class BetGameSrv {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Autowired
    lateinit var betTransactionRepository: BetTransactionRepository

    @Autowired
    lateinit var userRepository: BetUserRepository

    companion object {
        private val Q_BT = QBetTransaction.betTransaction
        private val Q_U = QBetUser.betUser
    }

    /**
     * Find all bets for the current user
     */
    @Loggable
    fun findAllBets(pageable: Pageable): Page<BetTransactionDTO> {
        val principal = SecurityContextHolder.getContext().authentication.principal as BetUser
        var userId: UUID = principal.id!!
        var betsPage = betTransactionRepository.findAllByBetUserId(userId, pageable)
        var betsDTOList = betsPage.content.map { it -> it.toDTO() }
        return PageImpl<BetTransactionDTO>(betsDTOList, pageable, betsPage.totalElements)
    }

    /**
     * Create a new bet
     */
    @Loggable
    fun createBet(bet: BetTransactionCreateDTO): BetTransactionDTO {
        val principal = SecurityContextHolder.getContext().authentication.principal as BetUser
        val user = userRepository.findOne(Q_U.username.eq(principal.username))
            .orElseThrow { EntityNotFoundException("User not found with username: $principal.username") }

        if (bet.betAmount > (user.walletBalance - (bet.betAmount))) throw MaxBetAmountExceededEx("MBE_01", "Maximum bet amount exceeded")
        
        user.walletBalance -= bet.betAmount
        
        val randomNumber = Random.nextInt(1, 11)
        val winAmount = calculateWinnings(bet.betNumber, randomNumber, bet.betAmount)
        val betResult: BetResult = if (winAmount > 0) BetResult.WIN else BetResult.LOST

        user.walletBalance += winAmount
        userRepository.save(user)

        var entity = BetTransaction(null, null, null, bet.betAmount, bet.betNumber, betResult, randomNumber, winAmount, user)

        return betTransactionRepository.save(entity).toDTO()
    }

    /**
     * Get the current user alongside wallet balance
     */
    @Loggable
    fun me(): UserReadDTO = (SecurityContextHolder.getContext().authentication.principal as BetUser).toReadDTO()

    /**
     * Get the leaderboard
     */
    @Loggable
    fun leaderboard(): List<LeaderboardDTO> = JPAQuery<Tuple>(entityManager)
            .select(Q_BT.betUser.username, Q_BT.betUser.username.count(), Q_BT.winAmount.sum())
            .from(Q_BT)
            .where(Q_BT.betResult.eq(BetResult.WIN))
            .groupBy(Q_BT.betUser.username)
            .orderBy(Q_BT.betUser.username.count().desc())
            .fetch()
            .map { it -> LeaderboardDTO(it.get(0, String::class.java), it.get(1, Long::class.java), it.get(2, Double::class.java)) }

    private fun calculateWinnings(bet: Int, generated: Int, betAmount: Double): Double {
        return when (kotlin.math.abs(bet - generated)) {
            0 -> betAmount * 10
            1 -> betAmount * 5
            2 -> betAmount / 2
            else -> 0.0
        }
    }
}