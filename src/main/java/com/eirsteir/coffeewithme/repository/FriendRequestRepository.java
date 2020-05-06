package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.request.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
}
