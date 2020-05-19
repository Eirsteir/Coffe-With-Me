package com.eirsteir.coffeewithme.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Component
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/queue");
//        registry.setApplicationDestinationPrefixes("/app");
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/notifications")
//                .setAllowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "http://client:3000")
//                .withSockJS();
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();

    }
}
