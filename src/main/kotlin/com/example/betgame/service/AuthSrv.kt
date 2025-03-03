package com.example.betgame.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import com.example.betgame.data.BetUserRepository
import com.example.betgame.data.UserRegisterDTO
import com.example.betgame.data.UserReadDTO
import com.example.betgame.data.BetUser
import com.example.betgame.data.toReadDTO
import com.example.betgame.data.AuthTokenDTO
import com.example.betgame.data.UserLoginDTO
import com.example.betgame.service.JwtSrv
import com.example.betgame.utils.Loggable

@Service
class AuthSrv {


    @Autowired
    lateinit var userRepository: BetUserRepository
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    lateinit var authenticationManager: AuthenticationManager
    @Autowired
    lateinit var jwtService: JwtSrv

    /**
     * Register a new user
     */
    @Loggable
    fun signup(newUserDTO: UserRegisterDTO): UserReadDTO {
        val user = BetUser(null, null, null, newUserDTO.username, passwordEncoder.encode(newUserDTO.password), newUserDTO.firstName, newUserDTO.lastName)
        return userRepository.save(user).toReadDTO()
    }

    /**
     * Authenticate a user
     */
    @Loggable
    fun authenticate(input: UserLoginDTO): AuthTokenDTO {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(input.username, input.password)
        )

        val user = userRepository.findByUsername(input.username)
        val jwtToken = jwtService.generateToken(user)

        return AuthTokenDTO(jwtToken)
    }
}
