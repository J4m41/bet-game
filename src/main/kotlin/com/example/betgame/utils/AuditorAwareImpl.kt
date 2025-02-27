package com.example.betgame.utils

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*
import com.example.betgame.data.BetUser

class AuditorAwareImpl : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        val authentication = SecurityContextHolder.getContext().authentication

        return if (authentication != null) {
            val principal = authentication.principal
            when (principal) {
                is BetUser -> Optional.of(principal.username)
                is String -> Optional.of(principal) // Handle case where principal is a String
                else -> Optional.of("system")
            }
        } else {
            Optional.of("system")
        }
    }
}
