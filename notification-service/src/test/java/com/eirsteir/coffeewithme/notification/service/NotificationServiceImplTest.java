package com.eirsteir.coffeewithme.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import com.eirsteir.coffeewithme.commons.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.notification.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.notification.domain.Notification;
import com.eirsteir.coffeewithme.notification.dto.NotificationDto;
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import({ModelMapperConfig.class})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {NotificationServiceImpl.class})
class NotificationServiceImplTest {

  private final long TO_USER_ID = 1L;
  private final long CURRENT_USER_ID = 2L;

  private Notification friendRequestNotification;
  private Notification.UserDetails currentUser;

  @Autowired private NotificationService service;

  @MockBean private NotificationRepository notificationRepository;

  @Autowired private ModelMapper modelMapper;

  @BeforeEach
  void setUp() {
    currentUser = Notification.UserDetails.builder().id(CURRENT_USER_ID).name("From user").build();

    friendRequestNotification =
        Notification.builder()
            .notificationId(1L)
            .subjectId(TO_USER_ID)
            .type(NotificationType.FRIEND_REQUEST)
            .user(currentUser)
            .build();
  }

  @Test
  void testFindAllByUserWhenUserHasNotifications_thenReturnListOfNotificationDto() {
    Pageable firstPage = PageRequest.of(0, 2);
    when(notificationRepository.findAllByUser_idOrderByTimestamp(TO_USER_ID, firstPage))
        .thenReturn(
            Arrays.asList(
                Notification.builder().user(currentUser).build(),
                Notification.builder().user(currentUser).build()));

    List<NotificationDto> notifications = service.findAllByUserId(TO_USER_ID, firstPage);

    assertThat(notifications).hasSize(2);
    assertThat(notifications.get(0).getUser().getId()).isEqualTo(currentUser.getId());
  }

  @Test
  void testUpdateNotificationToReadWhenFound_thenSetIsReadToTrue() {
    friendRequestNotification.setSeen(true);

    Notification notification =
        Notification.builder()
            .notificationId(friendRequestNotification.getNotificationId())
            .user(friendRequestNotification.getUser())
            .seen(false)
            .build();

    when(notificationRepository.findById(friendRequestNotification.getNotificationId()))
        .thenReturn(Optional.ofNullable(notification));
    when(notificationRepository.save(Mockito.any(Notification.class)))
        .thenReturn(friendRequestNotification);

    NotificationDto notificationDtoToUpdate = modelMapper.map(notification, NotificationDto.class);
    NotificationDto updatedNotificationDto =
        service.updateNotificationToRead(notificationDtoToUpdate);

    assertThat(updatedNotificationDto.getSeen()).isTrue();
  }

  @Disabled
  @Test
  void testUpdateNotificationToReadWhenNotFound_thenThrowEntityNotFoundException() {
    when(notificationRepository.findById(friendRequestNotification.getNotificationId()))
        .thenReturn(Optional.empty());

    NotificationDto notificationDtoToUpdate =
        modelMapper.map(friendRequestNotification, NotificationDto.class);

    assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
        .isThrownBy(() -> service.updateNotificationToRead(notificationDtoToUpdate))
        .withMessage(
            "Requested notification with id - "
                + friendRequestNotification.getNotificationId()
                + " does not exist");
  }
}
