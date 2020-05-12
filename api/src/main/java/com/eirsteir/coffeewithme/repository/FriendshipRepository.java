package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipPk;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipPk> {

    List<Friendship> findByPkRequesterIdOrPkAddresseeIdAndStatus(Long requesterId, Long addresseeId, FriendshipStatus status);

    Optional<Friendship> findByPkRequesterIdAndPkAddresseeId(Long requesterId, Long addresseeId);

    boolean existsByPkRequesterAndPkAddressee(User requester, User addressee);

    // TODO: 09.05.2020 What about when addressee sends request back to original requester?

}
