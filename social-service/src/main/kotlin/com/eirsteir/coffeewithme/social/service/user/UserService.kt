package com.eirsteir.coffeewithme.social.service.user

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.eirsteir.coffeewithme.social.domain.user.User
import com.eirsteir.coffeewithme.social.dto.UserProfile
import com.eirsteir.coffeewithme.social.web.request.UpdateProfileRequest
import org.springframework.data.jpa.domain.Specification

interface UserService {

    fun findUserByEmail(email: String): UserDetailsDto

    fun updateProfile(updateProfileRequest: UpdateProfileRequest, currentUser: UserDetailsImpl): UserProfile

    fun findUserById(id: Long): User

    fun searchUsers(spec: Specification<User>): List<UserDetailsDto>

    fun findUserById(id: Long, viewerId: Long): UserDetailsDto

    fun findByIdIn(friendsIds: List<Long>): List<User>
}