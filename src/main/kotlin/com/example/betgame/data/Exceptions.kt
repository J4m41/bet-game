package com.example.betgame.data

data class MaxBetAmountExceededEx(
    val errorCode: String,
    val errorMessage: String? = null,
): RuntimeException()