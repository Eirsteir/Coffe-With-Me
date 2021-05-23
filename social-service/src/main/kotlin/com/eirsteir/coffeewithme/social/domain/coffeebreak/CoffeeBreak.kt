package com.eirsteir.coffeewithme.social.domain.coffeebreak

import com.eirsteir.coffeewithme.commons.domain.coffeebreak.CoffeeBreakCreatedEvent
import com.eirsteir.coffeewithme.commons.domain.coffeebreak.CoffeeBreakDetails
import com.eirsteir.coffeewithme.social.domain.CreatedUpdatedDateTimeBaseModel
import com.eirsteir.coffeewithme.social.domain.university.Campus
import com.eirsteir.coffeewithme.social.domain.user.User
import io.eventuate.tram.events.publisher.ResultWithEvents
import java.time.LocalTime
import javax.persistence.*

@Entity
data class CoffeeBreak(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val id: Long? = null,
    private val scheduledTo: LocalTime? = null,
    @ManyToOne
    private val requester: User? = null,
    @ManyToMany
    private val addressees: Set<User> = setOf(),
    @ManyToOne
    private val campus: Campus? = null
) : CreatedUpdatedDateTimeBaseModel() {

    companion object {
        fun createCoffeeBreak(
            coffeeBreakDetails: CoffeeBreakDetails
        ): ResultWithEvents<CoffeeBreakDetails> {
            val coffeeBreakCreatedEvent = CoffeeBreakCreatedEvent(coffeeBreakDetails)
            return ResultWithEvents<CoffeeBreakDetails>(
                coffeeBreakDetails, listOf(coffeeBreakCreatedEvent)
            )
        }
    }
}