package com.eirsteir.coffeewithme.social.repository;

import com.eirsteir.coffeewithme.social.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {


    @Query("SELECT f from Friendship f where (f.id.requester.id = :userId or f.id.addressee.id = :userId) and f.status = :status")
    List<Friendship> findByUserIdAndStatus(Long userId, FriendshipStatus status);

    Optional<Friendship> findByIdRequesterIdAndIdAddresseeId(Long requesterId, Long addresseeId);

    // TODO: 09.05.2020 What about when addressee sends request back to original requester?

}
