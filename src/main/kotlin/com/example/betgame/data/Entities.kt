package com.example.betgame.data

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.TemporalType
import jakarta.persistence.Enumerated
import jakarta.persistence.EnumType
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.EntityListeners
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.repository.Temporal
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.UUID
import java.util.Date

@EntityListeners(AuditingEntityListener::class)
@Entity
data class BetUser (
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID? = null,
    @CreatedBy var createdBy: String? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @Column(unique = true, nullable = false) private var username: String,
    @Column(nullable = false) private var password: String,
    @Column(nullable = false) var firstName: String,
    @Column(nullable = false) var lastName: String,
    @Column(nullable = false, columnDefinition = "BIGINT CHECK (\"wallet_balance\" > 0)") var walletBalance: Long = 1000,
    @OneToMany(mappedBy = "betUser") var betTransactions: MutableList<BetTransaction> = mutableListOf()
): UserDetails {

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()
}

@EntityListeners(AuditingEntityListener::class)
@Entity
data class BetTransaction(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID? = null,
    @CreatedBy var createdBy: String? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @Column(nullable = false) var betAmount: Long,
    @Column(nullable = false) var betNumber: Int,
    @Column(nullable = false) @Enumerated(EnumType.STRING) var betResult: BetResult,
    @Column(nullable = false) var betResultNumber: Int,
    @Column(nullable = false) var winAmount: Long,
    @ManyToOne(optional = false) var betUser: BetUser? = null
)