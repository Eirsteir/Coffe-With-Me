package com.eirsteir.coffeewithme.social.service.user

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto
import com.eirsteir.coffeewithme.commons.exception.APIException
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.eirsteir.coffeewithme.social.domain.user.User
import com.eirsteir.coffeewithme.social.dto.UserProfile
import com.eirsteir.coffeewithme.social.web.request.UpdateProfileRequest
import com.eirsteir.coffeewithme.commons.exception.EntityType
import com.eirsteir.coffeewithme.commons.exception.ExceptionType
import com.eirsteir.coffeewithme.social.repository.FriendshipRepository
import com.eirsteir.coffeewithme.social.repository.UniversityRepository
import com.eirsteir.coffeewithme.social.repository.UserRepository
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import javax.transaction.Transactional


private val logger = KotlinLogging.logger {  }

@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val universityRepository: UniversityRepository,
    private val friendshipRepository: FriendshipRepository,
    private val modelMapper: ModelMapper,
) : UserService {

    override fun findUserByEmail(email: String): UserDetailsDto {
        val userModel: User = userRepository
            .findByEmail(email)
            .orElseThrow { APIException.of(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email) }
        return modelMapper.map(userModel, UserDetailsDto::class.java)
    }

    override fun findUserById(id: Long): User {
        return userRepository
            .findById(id)
            .orElseThrow { APIException.of(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, id.toString()) }
    }

    override fun findUserById(id: Long, viewerId: Long): UserDetailsDto {
        val user= findUserById(id)
        val currentUser= findUserById(id)
        val userDetails = modelMapper.map(user, UserDetailsDto::class.java)
        return includeFriendshipProperties(userDetails, user, currentUser)
    }

    // TODO: clearly separate this from retrieving other users
    private fun includeFriendshipProperties(
        userDetails: UserDetailsDto, otherUser: User, currentUser: User
    ): UserDetailsDto {
        val areFriends = getAreFriends(otherUser, currentUser)
        val friendshipCount = friendshipRepository.countByUserId(currentUser.id!!)

        return userDetails.copy(
            isFriend = areFriends,
            friendsCount = friendshipCount
        )
    }

    private fun getAreFriends(otherUser: User, currentUser: User) =
        otherUser in currentUser.getFriends()

    // TODO: add friends properties
    override fun searchUsers(spec: Specification<User>): List<UserDetailsDto> =
        userRepository.findAll(spec).stream()
            .map { user -> modelMapper.map(user, UserDetailsDto::class.java) }
            .collect(Collectors.toList())

    override fun updateProfile(
        updateProfileRequest: UpdateProfileRequest, currentUser: UserDetailsImpl
    ): UserProfile {
        var userToUpdate = findUserById(currentUser.id)
        universityRepository.findById(updateProfileRequest.universityId)
            .ifPresent { userToUpdate = userToUpdate.copy(university = it) }

        userToUpdate = userToUpdate.copy(nickname = updateProfileRequest.nickname)

        logger.info("[x] Updated user profile: {}", userToUpdate)

        return modelMapper.map(userRepository.save(userToUpdate), UserProfile::class.java)
    }

    override fun findByIdIn(friendsIds: List<Long>): List<User> =
        userRepository.findAllById(friendsIds)

}