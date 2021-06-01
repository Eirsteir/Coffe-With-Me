package com.eirsteir.coffeewithme.commons.domain.coffeebreak

import com.eirsteir.coffeewithme.commons.domain.notification.AbstractEntityNotificationEvent
import com.eirsteir.coffeewithme.commons.domain.user.UserDetails

open class CoffeeBreakCreatedEvent(
    val coffeeBreakDetails: CoffeeBreakDetails,
) : AbstractEntityNotificationEvent(), CoffeeBreakEvent