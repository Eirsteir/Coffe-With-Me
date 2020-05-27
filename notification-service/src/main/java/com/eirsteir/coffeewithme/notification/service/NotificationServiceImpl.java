package com.eirsteir.coffeewithme.notification.service;


import com.eirsteir.coffeewithme.notification.domain.Notification;
import com.eirsteir.coffeewithme.notification.dto.NotificationDto;
import com.eirsteir.coffeewithme.notification.exception.CWMException;
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.eirsteir.coffeewithme.notification.exception.EntityType.NOTIFICATION;
import static com.eirsteir.coffeewithme.notification.exception.ExceptionType.ENTITY_NOT_FOUND;


@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public NotificationDto updateNotificationToRead(NotificationDto notificationDto) {
        Notification notificationToUpdate = notificationRepository.findById(notificationDto.getNotificationId())
                .orElseThrow(() -> CWMException.getException(NOTIFICATION,
                                                             ENTITY_NOT_FOUND,
                                                             notificationDto.getNotificationId().toString()));

        notificationToUpdate.setSeen(true);
        log.debug("[x] Updating notification: {}", notificationToUpdate);

        return modelMapper.map(
                notificationRepository.save(notificationToUpdate), NotificationDto.class);
    }

    @Override
    public List<NotificationDto> findAllByUserId(Long id, Pageable pageable) {
        return notificationRepository.findAllByUser_idOrderByTimestamp(id, pageable)
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .collect(Collectors.toList());
    }

}
