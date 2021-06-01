package com.eirsteir.coffeewithme.social.dto

import com.eirsteir.coffeewithme.social.domain.university.University
import lombok.Builder
import lombok.Getter
import lombok.Setter
import lombok.experimental.Accessors

data class UserProfile(
    val id: Long,
    val email: String,
    val nickname: String,
    val name: String,
    var friendsCount: Int,
    val university: University,
)