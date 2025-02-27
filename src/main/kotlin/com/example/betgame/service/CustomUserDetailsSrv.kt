package com.example.betgame.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import com.example.betgame.data.BetUserRepository
import com.example.betgame.data.QBetUser
import com.querydsl.core.BooleanBuilder

@Service
class CustomUserDetailsSrv : UserDetailsService {

    @Autowired
    lateinit var userRepository: BetUserRepository
    
    companion object {
        private val Q_U = QBetUser.betUser
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
        return user
    }
}
