package com.eirsteir.coffeewithme.social.domain.friendship

import com.eirsteir.coffeewithme.commons.domain.friendship.FriendRequestAcceptedEvent
import com.eirsteir.coffeewithme.commons.domain.friendship.FriendRequestEvent
import com.eirsteir.coffeewithme.commons.domain.user.UserDetails
import com.eirsteir.coffeewithme.social.domain.user.User
import io.eventuate.tram.events.publisher.ResultWithEvents
import lombok.Builder
import lombok.Data
import lombok.experimental.Accessors
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*

@Entity
@AssociationOverrides(
    AssociationOverride(name = "id.requester", joinColumns = [JoinColumn(name = "requester_id")]),
    AssociationOverride(name = "id.addressee", joinColumns = [JoinColumn(name = "addressee_id")])
)
data class Friendship(
    @EmbeddedId
    val id: FriendshipId? = null,
    var status: FriendshipStatus? = null,
    @CreationTimestamp
    val createdDateTime: Date? = null,
    @UpdateTimestamp
    val updatedDateTime: Date? = null
) {
    @Transient
    val requester: User? = id?.requester

    @Transient
    val addressee: User? = id?.addressee

    constructor(requester: User, addressee: User, status: FriendshipStatus):
            this(id = FriendshipId(requester, addressee), status = status)

    companion object {
        fun createFriendRequest(
            friendship: Friendship, user: UserDetails
        ): ResultWithEvents<Friendship> {
            val friendRequestEvent = FriendRequestEvent(friendship.addressee?.id!!, user)
            return ResultWithEvents<Friendship>(friendship, listOf(friendRequestEvent))
        }

        fun createFriendRequestAccepted(
            friendship: Friendship, user: UserDetails
        ): ResultWithEvents<Friendship> {
            val friendRequestAcceptedEvent = FriendRequestAcceptedEvent(friendship.requester?.id!!, user)
            return ResultWithEvents<Friendship>(
                friendship, listOf(friendRequestAcceptedEvent)
            )
        }
    }
}