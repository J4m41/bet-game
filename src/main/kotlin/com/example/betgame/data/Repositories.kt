package com.example.betgame.data

import java.util.UUID
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, UUID> {
}