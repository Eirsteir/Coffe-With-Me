package com.eirsteir.coffeewithme;

import com.eirsteir.coffeewithme.notificationapi.domain.Notification;
import com.eirsteir.coffeewithme.notificationapi.domain.NotificationType;
import com.eirsteir.coffeewithme.notificationapi.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
public class SetupTestDataLoader implements ApplicationListener<ContextRefreshedEvent> {


    public static final long DEFAULT_USER_ID = 1L;
    public static final long REQUESTER_ID = 2L;
    public static final long ADDRESSEE_ID = 3L;
    public static final long OTHER_USER_ID = 4L;
    private static final String REQUESTER_USERNAME = "requester";
    private static final String ADDRESSEE_USERNAME = "addressee";
    public static final String OTHER_USER_USERNAME = "other-user";

    private boolean alreadySetup = false;

    @Autowired
    private NotificationRepository repository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Notification.UserDetails requester = Notification.UserDetails.builder()
                .id(REQUESTER_ID)
                .name("Requester")
                .username(REQUESTER_USERNAME)
                .build();

        Notification.UserDetails addressee = Notification.UserDetails.builder()
                .id(ADDRESSEE_ID)
                .name("Addressee")
                .username(ADDRESSEE_USERNAME)
                .build();

        Notification.UserDetails otherUser = Notification.UserDetails.builder()
                .id(OTHER_USER_ID)
                .name("Other User")
                .username(OTHER_USER_USERNAME)
                .build();

        Notification notification = Notification.builder()
                .subjectId(DEFAULT_USER_ID)
                .type(NotificationType.FRIENDSHIP_ACCEPTED)
                .user(addressee)
                .build();

        log.debug("[x] Preloading {}", repository.save(notification));

        notification = Notification.builder()
                .subjectId(REQUESTER_ID)
                .type(NotificationType.FRIENDSHIP_ACCEPTED)
                .user(addressee)
                .build();

        log.debug("[x] Preloading {}", repository.save(notification));

        notification = Notification.builder()
                .subjectId(OTHER_USER_ID)
                .type(NotificationType.FRIENDSHIP_REQUESTED)
                .user(requester)
                .build();

        log.debug("[x] Preloading {}", repository.save(notification));

        alreadySetup = true;
    }

}