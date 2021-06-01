package com.eirsteir.coffeewithme.authservice.config

import com.eirsteir.coffeewithme.authservice.repository.AccountRepository
import com.eirsteir.coffeewithme.authservice.service.AccountService
import com.eirsteir.coffeewithme.authservice.service.AccountServiceImpl
import com.eirsteir.coffeewithme.authservice.service.RoleService
import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@Import(
    TramJdbcKafkaConfiguration::class, TramEventsPublisherConfiguration::class, TramEventSubscriberConfiguration::class
)
class AccountConfiguration {

    @Bean
    fun accountService(
        domainEventPublisher: DomainEventPublisher,
        accountRepository: AccountRepository,
        roleService: RoleService,
        encoder: BCryptPasswordEncoder
    ) = AccountServiceImpl(
            domainEventPublisher,
            accountRepository,
            roleService,
            encoder
        )
}