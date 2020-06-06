package com.eirsteir.coffeewithme;

import com.eirsteir.coffeewithme.notification.domain.Notification;
import com.eirsteir.coffeewithme.commons.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository;
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
    private static final String REQUESTER_NICKNAME = "requester";
    private static final String ADDRESSEE_NICKNAME = "addressee";
    public static final String OTHER_USER_NICKNAME = "other-user";

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
                .nickname(REQUESTER_NICKNAME)
                .build();

        Notification.UserDetails addressee = Notification.UserDetails.builder()
                .id(ADDRESSEE_ID)
                .name("Addressee")
                .nickname(ADDRESSEE_NICKNAME)
                .build();

        Notification.UserDetails otherUser = Notification.UserDetails.builder()
                .id(OTHER_USER_ID)
                .name("Other User")
                .nickname(OTHER_USER_NICKNAME)
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