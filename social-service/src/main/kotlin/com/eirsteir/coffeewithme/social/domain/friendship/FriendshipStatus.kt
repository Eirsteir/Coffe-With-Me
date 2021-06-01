package com.eirsteir.coffeewithme.social.domain.friendship

import com.eirsteir.coffeewithme.social.domain.RequestStatus

enum class FriendshipStatus(private val value: Int) : RequestStatus {
    REQUESTED(1),
    ACCEPTED(2),
    DECLINED(3),
    BLOCKED(4);

    override fun getStatus() = name

    override fun getValue() = value
}