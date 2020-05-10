package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    List<Friendship> findByRequesterIdOrAddresseeIdAndStatus(Long requesterId, Long addresseeId, FriendshipStatus status);

    // TODO: 09.05.2020 What about when addressee sends request back to original requester?

}
