package com.eirsteir.coffeewithme.domain.notification;

import com.eirsteir.coffeewithme.domain.user.User;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    private Date timestamp;

    private NotificationType type; // TODO: 21.05.2020 Add converter

    @Builder.Default
    private boolean seen = false;

    @ManyToOne
    private User user;

    private Boolean requestedByViewer;

}