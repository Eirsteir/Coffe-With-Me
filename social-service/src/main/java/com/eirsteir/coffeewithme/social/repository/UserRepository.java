package com.eirsteir.coffeewithme.social.repository;

import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    @Query("UPDATE User u SET u.lastLogin=:lastLogin WHERE u.email = ?#{ principal?.nickname }")
    @Modifying
    @Transactional
    void updateLastLogin(@Param("lastLogin") Date lastLogin);

    @Query("SELECT friend " +
            "FROM User friend " +
            "JOIN Friendship friendship ON friendship.id.addressee.id = friend.id " +
            "JOIN User user ON user.id = friendship.id.requester.id " +
            "WHERE user.id = :userId " +
            "AND friendship.status = :status")
    List<User> findFriendsFromWithStatus(Long userId, FriendshipStatus status);

    @Query("SELECT friend " +
            "FROM User friend " +
            "JOIN Friendship friendship ON friendship.id.requester.id = friend.id " +
            "JOIN User user ON user.id = friendship.id.addressee.id " +
            "WHERE user.id = :userId " +
            "AND friendship.status = :status")
    List<User> findFriendsOfWithStatus(Long userId, FriendshipStatus status);

}
