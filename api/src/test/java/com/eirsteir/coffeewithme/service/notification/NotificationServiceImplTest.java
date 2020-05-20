package com.eirsteir.coffeewithme.service.notification;

import com.eirsteir.coffeewithme.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.domain.notification.Notification;
import com.eirsteir.coffeewithme.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.NotificationDto;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.EntityType;
import com.eirsteir.coffeewithme.repository.NotificationRepository;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.testconfig.MessageTemplateUtilTestConfig;
import com.eirsteir.coffeewithme.util.MessageTemplateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@Import({MessageTemplateUtilTestConfig.class, ModelMapperConfig.class})
@TestPropertySource({"classpath:notifications.properties", "classpath:exception.properties"})
@ExtendWith(SpringExtension.class)
class NotificationServiceImplTest {

    private final long TO_USER_ID = 1L;

    private final long FROM_USER_ID = 2L;

    private Notification friendRequestNotification;

    private User toUser;

    private User fromUser;

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private UserService userService;

    @MockBean
    private NotificationRepository notificationRepository;

    @MockBean
    private SimpMessagingTemplate template;

    @Autowired
    private ModelMapper modelMapper;

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
                .name("To user")
                .build();

        fromUser = User.builder()
                .id(FROM_USER_ID)
                .name("From user")
                .build();

        friendRequestNotification = Notification.builder()
                .id(1L)
                .message(MessageTemplateUtil.getMessageTemplate(EntityType.FRIENDSHIP, NotificationType.REQUESTED))
                .to(toUser)
                .build();
    }

    @Test
    void testNotifyRequestedRegistersAndNotifiesUser() {
        when(userService.findUserById(TO_USER_ID))
                .thenReturn(toUser);
        when(userService.findUserById(FROM_USER_ID))
                .thenReturn(fromUser);
        when(notificationRepository.save(Mockito.any(Notification.class)))
                .thenReturn(friendRequestNotification);

        notificationService.notify(TO_USER_ID, FROM_USER_ID, NotificationType.REQUESTED);

        verify(template, times(1))
                .convertAndSendToUser(friendRequestNotification.getTo().getId().toString(),
                                      "/queue/notifications",
                                      friendRequestNotification);
    }

    @Test
    void testNotifyAccepted_thenRegisterAndNotifyUser() {
        when(userService.findUserById(TO_USER_ID))
                .thenReturn(toUser);
        when(userService.findUserById(FROM_USER_ID))
                .thenReturn(fromUser);
        when(notificationRepository.save(Mockito.any(Notification.class)))
                .thenReturn(friendRequestNotification);

        notificationService.notify(FROM_USER_ID, TO_USER_ID, NotificationType.ACCEPTED);

        verify(template, times(1))
                .convertAndSendToUser(friendRequestNotification.getTo().getId().toString(),
                                      "/queue/notifications",
                                      friendRequestNotification);
    }

    @Test
    void testFindAllByUserWhenUserHasNotifications_thenReturnNotificationDtos() {
        Pageable firstPage = PageRequest.of(0, 2);
        when(notificationRepository.findAllByTo_IdOrderByCreatedDateTime(toUser.getId(), firstPage))
                .thenReturn(Arrays.asList(
                        Notification.builder()
                                .to(toUser)
                                .build(),
                        Notification.builder()
                                .to(toUser)
                                .build()
                ));

        List<NotificationDto> notifications = notificationService.findAllByUser(toUser, firstPage);

        assertThat(notifications).hasSize(2);
        assertThat(notifications.get(0).getToUserId()).isEqualTo(toUser.getId());
    }

    @Test
    void testUpdateNotificationToReadWhenFound_thenSetIsReadToTrue() {
        friendRequestNotification.setRead(true);

        when(notificationRepository.findById(friendRequestNotification.getTo().getId()))
                .thenReturn(Optional.ofNullable(friendRequestNotification));
        when(notificationRepository.save(Mockito.any(Notification.class)))
                .thenReturn(friendRequestNotification);

        NotificationDto notificationDtoToUpdate = modelMapper.map(friendRequestNotification, NotificationDto.class);
        NotificationDto updatedNotificationDto = notificationService.updateNotificationToRead(notificationDtoToUpdate);

        assertThat(updatedNotificationDto.getIsRead()).isTrue();
    }

    @Test
    void testUpdateNotificationToReadWhenNotFound_thenThrowEntityNotFoundException() {
        when(notificationRepository.findById(friendRequestNotification.getId()))
                .thenReturn(Optional.empty());

        NotificationDto notificationDtoToUpdate = modelMapper.map(friendRequestNotification, NotificationDto.class);

        assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
                .isThrownBy(() -> notificationService.updateNotificationToRead(notificationDtoToUpdate))
                .withMessage("Requested notification with id - " +
                                     friendRequestNotification.getId() + " does not exist");
    }
}