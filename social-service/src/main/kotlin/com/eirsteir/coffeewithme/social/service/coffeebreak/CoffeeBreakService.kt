package com.eirsteir.coffeewithme.social.service.coffeebreak

import com.eirsteir.coffeewithme.commons.domain.coffeebreak.CoffeeBreakDetails
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.eirsteir.coffeewithme.social.web.request.CoffeeBreakRequest

interface CoffeeBreakService {
    fun registerCoffeeBreak(
        coffeeBreakRequest: CoffeeBreakRequest, currentUser: UserDetailsImpl
    ): CoffeeBreakDetails
}