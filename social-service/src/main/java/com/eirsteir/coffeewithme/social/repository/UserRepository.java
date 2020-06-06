package com.eirsteir.coffeewithme.social.repository;

import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.university.University;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.UserProfile;
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
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    @Query("UPDATE User u SET u.lastLogin=:lastLogin WHERE u.email = ?#{ principal?.username }")
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

    @Query("select case " +
            "when friendship.id.requester.id=:userId then friendship.id.addressee " +
            "else friendship.id.requester end as addresseeId " +
            "from Friendship friendship " +
            "where (friendship.id.requester.id=:userId " +
            "or friendship.id.addressee.id=:userId) " +
            "and friendship.status = :status")
    List<User> findFriendsOfWithStatus(Long userId, FriendshipStatus status);

    @Query("select case " +
            "when friendship.id.requester.id=:userId then friendship.id.addressee " +
            "else friendship.id.requester end as addresseeId " +
            "from Friendship friendship " +
            "where (friendship.id.requester.id=:userId " +
            "or friendship.id.addressee.id=:userId) " +
            "and friendship.status = :status")
    List<User> findFriendsByUserIdAndStatus(Long userId, FriendshipStatus status);

    @Query("SELECT friend " +
            "FROM User friend " +
            "JOIN Friendship friendship ON friendship.id.addressee.id = friend.id " +
            "JOIN User user ON user.id = friendship.id.requester.id " +
            "WHERE user.id = :userId " +
            "AND friendship.status = :status " +
            "AND user.university = :university")
    Set<User> findFriendsFromWithStatusAndUniversity(Long userId, FriendshipStatus status, University university);

    @Query("SELECT friend " +
            "FROM User friend " +
            "JOIN Friendship friendship ON friendship.id.requester.id = friend.id " +
            "JOIN User user ON user.id = friendship.id.addressee.id " +
            "WHERE user.id = :userId " +
            "AND friendship.status = :status " +
            "AND user.university = :university")
    Set<User> findFriendsOfWithStatusAndUniversity(Long userId, FriendshipStatus status, University university);

    // TODO: 06.06.2020 query returning a user profile with friends count
}
