package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    @Query("UPDATE User u SET u.lastLogin=:lastLogin WHERE u.email = ?#{ principal?.username }")
    @Modifying
    @Transactional
    void updateLastLogin(@Param("lastLogin") Date lastLogin);

    @Query("SELECT f " +
            "FROM User f " +
            "JOIN Friendship fr ON fr.id.addressee.id = f.id " +
            "JOIN User u ON u.id = fr.id.requester.id " +
            "WHERE u.id = :userId " +
            "AND fr.status = :status")
    List<User> findFriendsFromWithStatus(Long userId, FriendshipStatus status);

    @Query("SELECT f " +
            "FROM User f " +
            "JOIN Friendship fr ON fr.id.requester.id = f.id " +
            "JOIN User u ON u.id = fr.id.addressee.id " +
            "WHERE u.id = :userId " +
            "AND fr.status = :status")
    List<User> findFriendsOfWithStatus(Long userId, FriendshipStatus status);

}
