package com.example.betgame.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.ProceedingJoinPoint
import org.springframework.stereotype.Component

/**
 * Extension property for easy logging in Kotlin.
 * Usage: Just call `log.info("message")` inside any class.
 */
val <T : Any> T.log: Logger
    get() = LoggerFactory.getLogger(this::class.java)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME) // Specifies that this annotation will be available at runtime
annotation class Loggable