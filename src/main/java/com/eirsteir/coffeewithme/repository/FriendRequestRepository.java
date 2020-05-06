package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.request.FriendRequest;
import com.eirsteir.coffeewithme.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findByFromAndTo(User from, User to);

    boolean existsByFromAndTo(User from, User to);

}
