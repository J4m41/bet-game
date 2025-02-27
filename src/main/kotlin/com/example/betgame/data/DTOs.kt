package com.example.betgame.data

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Max

data class UserReadDTO(val username: String, val firstName: String, val lastName: String, val walletBalance: Long)

fun BetUser.toReadDTO(): UserReadDTO = UserReadDTO(
    username = this.username, 
    firstName = this.firstName, 
    lastName = this.lastName, 
    walletBalance = this.walletBalance)

data class UserRegisterDTO(
    @NotBlank val username: String, 
    @NotBlank val password: String, 
    @NotBlank val firstName: String, 
    @NotBlank val lastName: String
)

data class UserLoginDTO(
    @NotBlank val username: String, 
    @NotBlank val password: String
)

data class AuthTokenDTO(
    val token: String,
    val expiresIn: Long?
)

data class ExceptionDTO(
    val errorCode: String,
    val errorMessage: String? = null,
    val errorDetails: MutableList<String> = mutableListOf()
)

data class BetTransactionDTO(
    @Min(0) var betAmount: Long,
    @Min(1) @Max(10) var betNumber: Int,
    var betResult: BetResult?,
    var winAmount: Long? = 0
)