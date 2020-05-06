package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.request.Friendship;
import com.eirsteir.coffeewithme.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    boolean existsByFromAndTo(User from, User to);

}
