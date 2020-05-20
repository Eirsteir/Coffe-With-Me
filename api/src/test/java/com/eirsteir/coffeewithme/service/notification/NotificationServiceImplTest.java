package com.eirsteir.coffeewithme.service.notification;

import com.eirsteir.coffeewithme.domain.notification.Notification;
import com.eirsteir.coffeewithme.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.repository.NotificationRepository;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.testconfig.MessageTemplateUtilTestConfig;
import com.eirsteir.coffeewithme.util.MessageTemplateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@Import(MessageTemplateUtilTestConfig.class)
@ExtendWith(SpringExtension.class)
class NotificationServiceImplTest {

    private final long TO_USER_ID = 1L;

    private Notification friendRequestNotification;

    private User toUser;

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private UserService userService;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private SimpMessagingTemplate template;

    @TestConfiguration
    static class FriendshipServiceImplTestContextConfiguration {

        @Bean
        public NotificationService notificationService() {
            return new NotificationServiceImpl();
        }
    }

    @BeforeEach
    void setUp() {
        toUser = User.builder()
                .id(TO_USER_ID)
                .build();

        friendRequestNotification = Notification.builder()
                .message(MessageTemplateUtil.getMessageTemplate(NotificationType.FRIEND_REQUEST))
                .user(toUser)
                .build();
    }

    @Test
    void testNotifyRegistersAndNotifiesUser() {
        when(userService.findUserById(TO_USER_ID))
                .thenReturn(toUser);
        when(notificationRepository.save(Mockito.any(Notification.class)))
                .thenReturn(friendRequestNotification);

        notificationService.notify(TO_USER_ID, NotificationType.FRIEND_REQUEST);

        verify(template, times(1))
                .convertAndSendToUser(friendRequestNotification.getUser().getId().toString(),
                                      "/queue/notifications",
                                      friendRequestNotification);
    }
}