package com.example.betgame.data

import java.util.UUID
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.repository.CrudRepository
import com.querydsl.core.types.dsl.StringPath
import org.springframework.stereotype.Repository

@Repository
interface BetUserRepository : CrudRepository<BetUser, UUID>, QuerydslPredicateExecutor<BetUser> {

    fun findByUsername(username: String): BetUser

}

@Repository
interface BetTransactionRepository : CrudRepository<BetTransaction, UUID>, QuerydslPredicateExecutor<BetTransaction> {

    fun findAllByBetUserId(betUserId: UUID): List<BetTransaction>

}