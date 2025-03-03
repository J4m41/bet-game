package com.example.betgame.utils

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.JoinPoint
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Aspect
@Component
class LoggingAspect {

    private val logger = LoggerFactory.getLogger(LoggingAspect::class.java)

    @Pointcut("@annotation(Loggable)")
    fun loggableMethods() {}

    @Before("@annotation(Loggable)")
    fun logMethodCall(joinPoint: JoinPoint) {
        logger.info("Calling method: ${joinPoint.signature.name} with arguments: ${joinPoint.args.joinToString()}")
    }

    @AfterReturning(value = "@annotation(Loggable)", returning = "result")
    fun logMethodReturn(joinPoint: JoinPoint, result: Any?) {
        logger.info("Method ${joinPoint.signature.name} returned: $result")
    }

    @AfterThrowing(value = "@annotation(Loggable)", throwing = "exception")
    fun logMethodException(joinPoint: JoinPoint, exception: Throwable) {
        logger.error("Method ${joinPoint.signature.name} threw an exception: ${exception.message}")
    }
}
