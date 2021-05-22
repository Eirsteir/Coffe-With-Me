package com.eirsteir.coffeewithme.authservice.service

import com.eirsteir.coffeewithme.authservice.domain.Account
import com.eirsteir.coffeewithme.authservice.repository.AccountRepository
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.*

class UserDetailsServiceImpl(private val repository: AccountRepository) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val account = repository.findByEmail(email)
            ?: throw UsernameNotFoundException("[x] Email: $email not found")

        return UserDetailsImpl.builder()
            .id(account.id)
            .email(account.email)
            .password(account.password)
            .roles(getAuthorities(account))
            .build()
    }

    private fun getAuthorities(account: Account): Collection<GrantedAuthority> {
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        account.roles
            ?.forEach { authorities.add(SimpleGrantedAuthority(it.type.name)) }
            ?: setOf<GrantedAuthority>()

        return authorities
    }
}