package com.eirsteir.coffeewithme.service.notification;

import com.eirsteir.coffeewithme.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.domain.notification.Notification;
import com.eirsteir.coffeewithme.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.NotificationDto;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.repository.NotificationRepository;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.testconfig.MessageTemplateUtilTestConfig;
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
@TestPropertySource("classpath:exception.properties")
@ExtendWith(SpringExtension.class)
class NotificationServiceImplTest {

    private final long TO_USER_ID = 1L;

    private final long CURRENT_USER_ID = 2L;

    private Notification friendRequestNotification;

    private User toUser;

    private User currentUser;

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

        currentUser = User.builder()
                .id(CURRENT_USER_ID)
                .name("From user")
                .build();

        friendRequestNotification = Notification.builder()
                .id(1L)
                .type(NotificationType.FRIENDSHIP_REQUESTED)
                .user(toUser)
                .requestedByViewer(false)
                .build();
    }

    @Test
    void testNotifyRequestedRegistersAndNotifiesUser() {
        when(userService.findUserById(TO_USER_ID))
                .thenReturn(toUser);
        when(userService.findUserById(CURRENT_USER_ID))
                .thenReturn(currentUser);
        when(notificationRepository.save(Mockito.any(Notification.class)))
                .thenReturn(friendRequestNotification);

        notificationService.notify(TO_USER_ID, currentUser, NotificationType.FRIENDSHIP_REQUESTED);

        NotificationDto expectedNotificationDto = modelMapper.map(friendRequestNotification, NotificationDto.class);

        verify(template, times(1))
                .convertAndSendToUser(friendRequestNotification.getUser().getId().toString(),
                                      "/queue/notifications",
                                      expectedNotificationDto);
    }

    @Test
    void testNotifyAccepted_thenRegisterAndNotifyUser() {
        when(userService.findUserById(TO_USER_ID))
                .thenReturn(toUser);
        when(userService.findUserById(CURRENT_USER_ID))
                .thenReturn(currentUser);

        friendRequestNotification.setType(NotificationType.FRIENDSHIP_ACCEPTED)
                .setRequestedByViewer(true);
        when(notificationRepository.save(Mockito.any(Notification.class)))
                .thenReturn(friendRequestNotification);
        NotificationDto expectedNotificationDto = modelMapper.map(friendRequestNotification, NotificationDto.class);

        notificationService.notify(CURRENT_USER_ID, currentUser, NotificationType.FRIENDSHIP_ACCEPTED);

        verify(template, times(1))
                .convertAndSendToUser(friendRequestNotification.getUser().getId().toString(),
                                      "/queue/notifications",
                                      expectedNotificationDto);
    }

    @Test
    void testFindAllByUserWhenUserHasNotifications_thenReturnListOfNotificationDto() {
        Pageable firstPage = PageRequest.of(0, 2);
        when(notificationRepository.findAllByUserOrderByTimestamp(toUser, firstPage))
                .thenReturn(Arrays.asList(
                        Notification.builder()
                                .user(toUser)
                                .build(),
                        Notification.builder()
                                .user(toUser)
                                .build()
                ));

        List<NotificationDto> notifications = notificationService.findAllByUser(toUser, firstPage);

        assertThat(notifications).hasSize(2);
        assertThat(notifications.get(0).getUser().getId()).isEqualTo(toUser.getId());
    }

    @Test
    void testUpdateNotificationToReadWhenFound_thenSetIsReadToTrue() {
        friendRequestNotification.setSeen(true);

        Notification notification = Notification.builder()
                .id(friendRequestNotification.getId())
                .user(friendRequestNotification.getUser())
                .seen(false)
                .build();

        when(notificationRepository.findById(friendRequestNotification.getUser().getId()))
                .thenReturn(Optional.ofNullable(notification));
        when(notificationRepository.save(Mockito.any(Notification.class)))
                .thenReturn(friendRequestNotification);

        NotificationDto notificationDtoToUpdate = modelMapper.map(notification, NotificationDto.class);
        NotificationDto updatedNotificationDto = notificationService.updateNotificationToRead(notificationDtoToUpdate);

        assertThat(updatedNotificationDto.getSeen()).isTrue();
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