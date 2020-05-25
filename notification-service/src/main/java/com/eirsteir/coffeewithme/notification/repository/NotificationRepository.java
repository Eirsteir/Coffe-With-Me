package com.eirsteir.coffeewithme.notification.repository;

import com.eirsteir.coffeewithme.notification.domain.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUser_idOrderByTimestamp(Long userId, Pageable pageable);

}