package com.eirsteir.coffeewithme.commons.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


data class UserDetailsImpl(
    val id: Long,
    val email: String,
    val nickname: String,
    private val password: String,
    var roles: Collection<GrantedAuthority>,
    private val isAccountNonExpired: Boolean = true,
    private val isAccountNonLocked: Boolean = false,
    private val isCredentialsNonExpired: Boolean = false,
    private val isEnabled: Boolean = false,
) : UserDetails {

    override fun getAuthorities() = roles

    override fun getPassword() = password

    override fun getUsername() = nickname

    override fun isAccountNonExpired() = isAccountNonExpired

    override fun isAccountNonLocked() = isAccountNonLocked

    override fun isCredentialsNonExpired() = isCredentialsNonExpired

    override fun isEnabled() = isEnabled
}