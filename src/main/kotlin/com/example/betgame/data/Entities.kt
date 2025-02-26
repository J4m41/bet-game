package com.example.betgame.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
class User(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID? = null,
    var username: String,
    var firstname: String,
    var lastname: String
)