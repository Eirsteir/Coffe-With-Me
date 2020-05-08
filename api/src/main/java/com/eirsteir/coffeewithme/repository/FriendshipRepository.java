package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {



}
