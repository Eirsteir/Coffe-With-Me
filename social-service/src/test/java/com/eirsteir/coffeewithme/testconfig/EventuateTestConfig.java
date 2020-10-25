package com.eirsteir.coffeewithme.testconfig;

import io.eventuate.tram.consumer.common.DuplicateMessageDetector;
import io.eventuate.tram.events.common.DomainEventNameMapping;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.messaging.common.ChannelMapping;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.producer.MessageProducer;
import io.eventuate.tram.spring.consumer.common.TramConsumerBaseCommonConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@TestConfiguration
public class EventuateTestConfig {
    @MockBean
    private ChannelMapping channelMapping;
    @MockBean
    private MessageProducer messageProducer;
    @MockBean
    private MessageConsumer messageConsumer;
    @MockBean
    private DuplicateMessageDetector duplicateMessageDetector;
    @MockBean
    private DomainEventNameMapping domainEventNameMapping;
    @MockBean
    private TramConsumerBaseCommonConfiguration tramConsumerBaseCommonConfiguration;
    @MockBean
    private DomainEventPublisher domainEventPublisher;
    @MockBean
    private JdbcTemplate jdbcTemplate;
    @MockBean
    private org.springframework.transaction.support.TransactionTemplate transactionTemplate;

    @TestConfiguration
    static class FriendshipRepositoryTestConfiguration {
        @Bean(name = "mvcHandlerMappingIntrospector")
        public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
            return new HandlerMappingIntrospector();
        }
    }
}
