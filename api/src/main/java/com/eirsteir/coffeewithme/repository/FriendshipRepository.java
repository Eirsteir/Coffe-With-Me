package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipPk;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipPk> {


    @Query("SELECT f from Friendship f where (f.pk.requester.id = :userId or f.pk.addressee.id = :userId) and f.status = :status")
    List<Friendship> findByUserIdAndStatus(Long userId, FriendshipStatus status);

    Optional<Friendship> findByPkRequesterIdAndPkAddresseeId(Long requesterId, Long addresseeId);

    boolean existsByPkRequesterAndPkAddressee(User requester, User addressee);

    // TODO: 09.05.2020 What about when addressee sends request back to original requester?

}
