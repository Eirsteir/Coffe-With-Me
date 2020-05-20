package com.eirsteir.coffeewithme.domain.notification;

import com.eirsteir.coffeewithme.domain.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String message;

    @CreationTimestamp
    private Date createdDateTime;

    @Builder.Default
    private boolean isRead = false;

    @ManyToOne
    private User user;

}