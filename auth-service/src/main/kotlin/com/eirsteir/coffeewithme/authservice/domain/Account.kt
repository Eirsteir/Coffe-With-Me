package com.eirsteir.coffeewithme.authservice.domain

import com.eirsteir.coffeewithme.commons.domain.account.AccountCreatedEvent
import io.eventuate.tram.events.publisher.ResultWithEvents
import lombok.*
import javax.persistence.*


@Entity
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1L,
    @Column(unique = true)
    val email: String = "",
    val name: String = "",
    val password: String = "",
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "account_roles",
        joinColumns = [JoinColumn(name = "account_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    val roles: Set<Role>? = null
) {

    companion object {
        fun createAccount(account: Account): ResultWithEvents<Account> {
            val accountCreatedEvent = AccountCreatedEvent(account.id, account.email, account.name)
            return ResultWithEvents(account, listOf(accountCreatedEvent))
        }
    }
}