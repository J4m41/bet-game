package com.example.betgame.web

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.beans.factory.annotation.Autowired
import com.example.betgame.data.UserRegisterDTO
import com.example.betgame.data.AuthTokenDTO
import com.example.betgame.data.UserReadDTO
import com.example.betgame.data.UserLoginDTO
import com.example.betgame.service.AuthSrv
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
class AuthAPI @Autowired constructor(
    private val authSrv: AuthSrv
) {

    /**
     * API Endpoint to register a new user
     * 
     * @param user the DTO of the user to be created
     * @return the created user DTO
     */
    @PostMapping("/register")
    fun register(@RequestBody @Valid user: UserRegisterDTO): UserReadDTO = authSrv.signup(user)

    /**
     * API Endpoint to authenticate an existing user
     * 
     * @param user the DTO containing sign-in data (username and password)
     * @return a DTO containing the JWT Bearer token
     */
    @PostMapping("/authenticate")
    fun authenticate(@RequestBody @Valid user: UserLoginDTO): AuthTokenDTO = authSrv.authenticate(user)
    
}