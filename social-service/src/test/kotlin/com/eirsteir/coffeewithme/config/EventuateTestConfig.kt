package com.eirsteir.coffeewithme.config

import io.eventuate.tram.consumer.common.DuplicateMessageDetector
import io.eventuate.tram.events.common.DomainEventNameMapping
import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.messaging.common.ChannelMapping
import io.eventuate.tram.messaging.consumer.MessageConsumer
import io.eventuate.tram.messaging.producer.MessageProducer
import io.eventuate.tram.spring.consumer.common.TramConsumerBaseCommonConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@TestConfiguration
class EventuateTestConfig {
    @MockBean
    private val channelMapping: ChannelMapping? = null

    @MockBean
    private val messageProducer: MessageProducer? = null

    @MockBean
    private val messageConsumer: MessageConsumer? = null

    @MockBean
    private val duplicateMessageDetector: DuplicateMessageDetector? = null

    @MockBean
    private val domainEventNameMapping: DomainEventNameMapping? = null

    @MockBean
    private val tramConsumerBaseCommonConfiguration: TramConsumerBaseCommonConfiguration? = null

    @MockBean
    private val domainEventPublisher: DomainEventPublisher? = null

    @MockBean
    private val jdbcTemplate: JdbcTemplate? = null

    @MockBean
    private val transactionTemplate: TransactionTemplate? = null

    @TestConfiguration
    internal class FriendshipRepositoryTestConfiguration {
        @Bean(name = ["mvcHandlerMappingIntrospector"])
        fun mvcHandlerMappingIntrospector(): HandlerMappingIntrospector? {
            return HandlerMappingIntrospector()
        }
    }
}