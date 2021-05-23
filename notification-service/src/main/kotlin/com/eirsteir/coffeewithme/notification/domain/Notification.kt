package com.eirsteir.coffeewithme.notification.domain

import com.eirsteir.coffeewithme.commons.domain.notification.NotificationType
import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.*
import lombok.experimental.Accessors
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*


@Entity
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val notificationId: Long? = null,
    @CreationTimestamp
    val timestamp: Date? = null,
    val type : NotificationType? = null, // TODO: 21.05.2020 Add converter
    var seen: Boolean = false,
    @Embedded
    val user: UserDetails? = null,
    @JsonIgnore
    val subjectId: Long? = null,
) {
    @Embeddable
    data class UserDetails(
        var id: Long? = null,
        var name: String? = null,
        var nickname: String? = null,
    )
}