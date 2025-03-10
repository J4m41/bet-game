package com.example.betgame

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class BetGameApplication

fun main(args: Array<String>) {
	runApplication<BetGameApplication>(*args)
}
