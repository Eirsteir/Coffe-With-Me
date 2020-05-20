package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.notification.Notification;
import com.eirsteir.coffeewithme.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findByTo(User user);

    @Query("select notification from Notification notification " +
            "where notification.to.id=:userId " +
            "ORDER BY notification.createdDateTime DESC")
    List<Notification> userNotification(Long userId, Pageable pageSize);

    Notification findByToAndId(User user, Long id);

}