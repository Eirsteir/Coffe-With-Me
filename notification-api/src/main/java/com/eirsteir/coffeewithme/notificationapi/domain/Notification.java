package com.eirsteir.coffeewithme.notificationapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Embedded
    private UserDetails user;

    @JsonIgnore
    private Long subjectId;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class UserDetails {
        private Long id;
        private String name;
        private String username;
    }

}