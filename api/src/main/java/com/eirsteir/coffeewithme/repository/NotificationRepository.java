package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.notification.Notification;
import com.eirsteir.coffeewithme.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserOrderByTimestamp(User user, Pageable pageable);

}