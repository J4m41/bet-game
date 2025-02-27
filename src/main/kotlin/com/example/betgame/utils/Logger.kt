package com.example.betgame.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Extension property for easy logging in Kotlin.
 * Usage: Just call `log.info("message")` inside any class.
 */
val <T : Any> T.log: Logger
    get() = LoggerFactory.getLogger(this::class.java)
