package com.eirsteir.coffeewithme.social.web.api.friendship

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus
import com.eirsteir.coffeewithme.social.dto.FriendshipDto
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService
import com.eirsteir.coffeewithme.social.service.user.UserService
import com.eirsteir.coffeewithme.social.web.request.FriendRequest
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import javax.validation.Valid

private val logger = KotlinLogging.logger {  }

@RequestMapping("/friends")
@RestController
class FriendshipController(
    private val friendshipService: FriendshipService,
    private val userService: UserService,
    private val modelMapper: ModelMapper
) {

    @GetMapping
    @ResponseBody
    fun getFriends(@AuthenticationPrincipal principal: UserDetailsImpl): Collection<FriendshipDto> {
        val userDetailsDto: UserDetailsDto =
            modelMapper.map(userService.findUserById(principal.id), UserDetailsDto::class.java)
        val friendships: List<FriendshipDto> = friendshipService.findFriendshipsOf(userDetailsDto)
        if (friendships.isEmpty())
            throw ResponseStatusException(
            HttpStatus.NO_CONTENT,
            "User with email - " + userDetailsDto.email + " has no friends"
        )
        return friendships
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addFriend(
        @RequestParam("to_friend") toFriend: Long,
        @AuthenticationPrincipal principal: UserDetailsImpl
    ): FriendshipDto? {
        logger.debug("[x] Request to /friends?to_friend={}", toFriend)

        val currentUser = userService.findUserById(principal.id)
        if (currentUser.id!! == toFriend)
            throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Cannot send friend requests to yourself"
        )
        val friendRequest = FriendRequest(requesterId = currentUser.id, addresseeId = toFriend)
        return friendshipService.registerFriendship(friendRequest)
    }

    @PutMapping
    fun updateFriendship(
        @RequestBody friendshipDto: @Valid FriendshipDto,
        @AuthenticationPrincipal principal: UserDetailsImpl
    ): FriendshipDto? {
        validateFriendshipDto(friendshipDto, principal)
        return friendshipService.updateFriendship(friendshipDto)
    }

    @DeleteMapping
    fun deleteFriendship(
        @RequestBody friendshipDto: @Valid FriendshipDto,
        @AuthenticationPrincipal principal: UserDetailsImpl
    ): Nothing = throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED)

    @GetMapping("/requests")
    fun getFriendRequests(@AuthenticationPrincipal principal: UserDetailsImpl): List<FriendshipDto> {
        val userDto: UserDetailsDto = modelMapper.map(principal, UserDetailsDto::class.java)
        val friendRequests = friendshipService.findFriendshipsOf(userDto.id!!, FriendshipStatus.REQUESTED)

        if (friendRequests.isEmpty())
            throw ResponseStatusException(
            HttpStatus.NO_CONTENT,
            "User with email - " + userDto.email + " has no friend requests"
        )
        return friendRequests
    }

    private fun validateFriendshipDto(friendshipDto: FriendshipDto, principal: UserDetailsImpl) {
        val requesterId = friendshipDto.requester.id!!
        val addresseeId = friendshipDto.addressee.id!!
        val principalId = principal.id

        if (addresseeId == principalId)
            return
        else if (requesterId == principalId)
            throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Cannot accept friend request sent by yourself"
        )
        throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Friendship does not belong to current user"
        )
    }
}