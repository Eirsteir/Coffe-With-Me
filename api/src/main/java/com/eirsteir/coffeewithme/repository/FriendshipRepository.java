package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Collection<User> findByFrom(User user);

    boolean existsByFromAndTo(User from, User to);

}
