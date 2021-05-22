package com.eirsteir.coffeewithme.commons.domain.coffeebreak

import com.eirsteir.coffeewithme.commons.domain.university.CampusDetails
import com.eirsteir.coffeewithme.commons.domain.user.UserDetails
import java.time.LocalTime

data class CoffeeBreakDetails(
    val scheduledTo: LocalTime,
    val requester: UserDetails,
    val addressees: Set<UserDetails>,
    val campus: CampusDetails,
)