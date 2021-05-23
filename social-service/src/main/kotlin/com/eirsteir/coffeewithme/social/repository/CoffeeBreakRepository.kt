package com.eirsteir.coffeewithme.social.repository

import com.eirsteir.coffeewithme.social.domain.coffeebreak.CoffeeBreak
import org.springframework.data.jpa.repository.JpaRepository

interface CoffeeBreakRepository : JpaRepository<CoffeeBreak, Long>