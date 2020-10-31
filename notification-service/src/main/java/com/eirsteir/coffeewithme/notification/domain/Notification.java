package com.eirsteir.coffeewithme.notification.domain;

import com.eirsteir.coffeewithme.commons.domain.notification.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long notificationId;

  @CreationTimestamp private Date timestamp;

  private NotificationType type; // TODO: 21.05.2020 Add converter

  @Builder.Default private boolean seen = false;

  @Embedded private UserDetails user;

  @JsonIgnore private Long subjectId;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Embeddable
  public static class UserDetails {
    private Long id;
    private String name;
    private String nickname;
  }
}
