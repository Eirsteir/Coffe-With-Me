package com.eirsteir.coffeewithme.social.dto

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus
import lombok.Builder
import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors


data class FriendshipDto(
    val requester: UserDetailsDto,
    val addressee: UserDetailsDto,
    val status: FriendshipStatus
)