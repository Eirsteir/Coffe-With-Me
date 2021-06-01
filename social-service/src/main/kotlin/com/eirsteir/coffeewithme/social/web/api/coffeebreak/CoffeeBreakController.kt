package com.eirsteir.coffeewithme.social.web.api.coffeebreak

import com.eirsteir.coffeewithme.commons.domain.coffeebreak.CoffeeBreakDetails
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakService
import com.eirsteir.coffeewithme.social.web.request.CoffeeBreakRequest
import mu.KotlinLogging
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


private val logger = KotlinLogging.logger {  }

@RequestMapping("/coffee-breaks")
@RestController
class CoffeeBreakController(private val coffeeBreakService: CoffeeBreakService) {

    @PostMapping
    fun registerCoffeeBreak(
        @RequestBody coffeeBreakRequest: @Valid CoffeeBreakRequest,
        @AuthenticationPrincipal principal: UserDetailsImpl
    ) = coffeeBreakService.registerCoffeeBreak(coffeeBreakRequest, principal)
}