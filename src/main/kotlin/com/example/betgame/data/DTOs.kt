package com.example.betgame.data

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Max

data class UserReadDTO(
    val username: String, 
    val firstName: String, 
    val lastName: String, 
    val walletBalance: Double
)

fun BetUser.toReadDTO(): UserReadDTO = UserReadDTO(
    username = this.username, 
    firstName = this.firstName, 
    lastName = this.lastName, 
    walletBalance = this.walletBalance)

data class UserRegisterDTO(
    @field:NotBlank val username: String, 
    @field:NotBlank val password: String, 
    @field:NotBlank val firstName: String, 
    @field:NotBlank val lastName: String
)

data class UserLoginDTO(
    @field:NotBlank val username: String, 
    @field:NotBlank val password: String
)

data class AuthTokenDTO(
    val token: String
)

data class ExceptionDTO(
    val errorCode: String,
    val errorMessage: String? = null,
    val errorDetails: MutableList<String> = mutableListOf()
)

data class BetTransactionDTO(
    var betAmount: Double,
    var betNumber: Int,
    var betResult: BetResult,
    var betResultNumber: Int,
    var winAmount: Double
)

fun BetTransaction.toDTO(): BetTransactionDTO = BetTransactionDTO(this.betAmount, this.betNumber, this.betResult, this.betResultNumber, this.winAmount)

data class BetTransactionCreateDTO(
    @field:Min(1) var betAmount: Double,
    @field:Min(1) @field:Max(10) var betNumber: Int,
)

data class LeaderboardDTO(
    val username: String?,
    val totalWins: Long?,
    val totalWinsAmount: Double?
)