package com.eirsteir.coffeewithme.authservice.domain

import java.io.Serializable
import javax.persistence.*


@Entity
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: Long = -1L,
    @Column(unique = true)
    val type: RoleType = RoleType.ROLE_USER,
) : Serializable {

    companion object {
        private const val serialVersionUID = -3444524593791786238L
    }
}