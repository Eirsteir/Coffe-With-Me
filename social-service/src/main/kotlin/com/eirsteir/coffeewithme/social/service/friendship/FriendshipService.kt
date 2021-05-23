package com.eirsteir.coffeewithme.social.service.friendship

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipId
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus
import com.eirsteir.coffeewithme.social.domain.user.User
import com.eirsteir.coffeewithme.social.dto.FriendshipDto
import com.eirsteir.coffeewithme.social.web.request.FriendRequest

interface FriendshipService {

    fun findFriendshipsOf(id: Long, status: FriendshipStatus): List<FriendshipDto>

    fun findFriendshipsAtUniversity(user: User): List<FriendshipDto>

    fun registerFriendship(friendRequest: FriendRequest): FriendshipDto

    fun friendshipExists(friendshipId: FriendshipId): Boolean

    fun removeFriendship(friendshipDto: FriendshipDto)

    fun updateFriendship(friendshipDto: FriendshipDto): FriendshipDto

    fun findFriendshipsOf(user: UserDetailsDto): List<FriendshipDto>

    fun findAllFriendshipsWithStatus(
        user: UserDetailsDto,
        status: FriendshipStatus
    ): List<FriendshipDto>

    fun getFriendsCount(userId: Long): Int
}