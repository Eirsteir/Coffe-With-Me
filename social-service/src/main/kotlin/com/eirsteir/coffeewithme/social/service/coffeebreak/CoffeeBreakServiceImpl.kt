package com.eirsteir.coffeewithme.social.service.coffeebreak

import com.eirsteir.coffeewithme.commons.domain.coffeebreak.CoffeeBreakDetails
import com.eirsteir.coffeewithme.commons.exception.APIException
import com.eirsteir.coffeewithme.commons.exception.EntityType
import com.eirsteir.coffeewithme.commons.exception.ExceptionType
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.eirsteir.coffeewithme.social.domain.coffeebreak.CoffeeBreak
import com.eirsteir.coffeewithme.social.domain.user.User
import com.eirsteir.coffeewithme.social.repository.CampusRepository
import com.eirsteir.coffeewithme.social.repository.CoffeeBreakRepository
import com.eirsteir.coffeewithme.social.repository.UserRepository
import com.eirsteir.coffeewithme.social.web.request.CoffeeBreakRequest
import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.publisher.ResultWithEvents
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import java.time.LocalTime
import javax.transaction.Transactional

private val logger = KotlinLogging.logger {  }


@Service
@Transactional
class CoffeeBreakServiceImpl(
    private val domainEventPublisher: DomainEventPublisher,
    private val coffeeBreakRepository: CoffeeBreakRepository,
    private val userRepository: UserRepository,
    private val campusRepository: CampusRepository,
    private val modelMapper: ModelMapper
) : CoffeeBreakService {

    override fun registerCoffeeBreak(
        coffeeBreakRequest: CoffeeBreakRequest, currentUser: UserDetailsImpl
    ): CoffeeBreakDetails {
        var coffeeBreak: CoffeeBreak = createCoffeeBreak(coffeeBreakRequest, currentUser.id)
        coffeeBreak = coffeeBreakRepository.save(coffeeBreak)
        logger.info("[x] Registered coffee break: {}", coffeeBreak)

        // TODO: 29.05.2020 notify addressees
        val coffeeBreakDetails: CoffeeBreakDetails = getCoffeeBreakDetails(coffeeBreak)
        val coffeeBreakWithEvents = CoffeeBreak.createCoffeeBreak(coffeeBreakDetails)
        publish(coffeeBreakWithEvents)

        return modelMapper.map(coffeeBreak, CoffeeBreakDetails::class.java)
    }

    private fun createCoffeeBreak(coffeeBreakRequest: CoffeeBreakRequest, currentUserId: Long): CoffeeBreak {
        val requester = getUser(currentUserId)
        val addressees = requester.getFiendsAtUniversity()
        val campus = campusRepository.findById(coffeeBreakRequest.campusId).orElse(null)
        val scheduledTo = getScheduledToFromNow(coffeeBreakRequest.scheduledToInMinutes)

        return CoffeeBreak(
            requester = requester,
            addressees = addressees,
            campus = campus,
            scheduledTo = scheduledTo)
    }

    private fun getCoffeeBreakDetails(coffeeBreak: CoffeeBreak): CoffeeBreakDetails =
        modelMapper.map(coffeeBreak, CoffeeBreakDetails::class.java)

    private fun getUser(userId: Long): User =
        userRepository.findById(userId)
            .orElseThrow {
                APIException.of(
                    EntityType.USER,
                    ExceptionType.ENTITY_NOT_FOUND,
                    userId.toString()
                )
            }

    private fun getScheduledToFromNow(scheduledToInMinutes: Long): LocalTime =
        LocalTime.now().plusMinutes(scheduledToInMinutes)

    private fun publish(coffeeBreakWithEvents: ResultWithEvents<CoffeeBreakDetails>) {
        logger.info("[x] Publishing {} to {}", coffeeBreakWithEvents, CoffeeBreak::class.java)
        domainEventPublisher.publish(
            CoffeeBreak::class.java, coffeeBreakWithEvents, coffeeBreakWithEvents.events
        )
    }

}