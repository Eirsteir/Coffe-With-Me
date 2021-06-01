package com.eirsteir.coffeewithme.commons.domain.notification

import com.eirsteir.coffeewithme.commons.domain.user.UserDetails
import io.eventuate.tram.events.common.DomainEvent

abstract class AbstractEntityNotificationEvent(
    val subjectId: Long? = null,
    val user: UserDetails? = null,
) : DomainEvent