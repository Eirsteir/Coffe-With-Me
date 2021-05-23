package com.eirsteir.coffeewithme.social.repository

import com.eirsteir.coffeewithme.social.domain.friendship.Friendship
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipId
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus
import com.eirsteir.coffeewithme.social.domain.university.University
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface FriendshipRepository : JpaRepository<Friendship, FriendshipId> {
    @Query(
        "SELECT f from Friendship f where (f.id.requester.id = :userId or f.id.addressee.id ="
                + " :userId) and f.status = :status"
    )
    fun findByUserAndStatus(userId: Long, status: FriendshipStatus): List<Friendship>

    @Query(
        "SELECT f from Friendship f "
                + "where (f.id.requester.id = :userId or f.id.addressee.id = :userId) "
                + "and f.status = :status "
                + "and f.id.requester.university = :university "
                + "and f.id.addressee.university = :university"
    )
    fun findByUserAndStatusAndUniversity(
        userId: Long, status: FriendshipStatus, university: University
    ): List<Friendship>

    fun findByIdRequesterIdAndIdAddresseeId(requesterId: Long, addresseeId: Long): Optional<Friendship>

    @Query(
        "SELECT count(f) from Friendship f where (f.id.requester.id = :userId or f.id.addressee.id ="
                + " :userId) and f.status ="
                + " com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus.ACCEPTED"
    )
    fun countByUserId(userId: Long): Int // TODO: 09.05.2020 What about when addressee sends request back to original requester
}