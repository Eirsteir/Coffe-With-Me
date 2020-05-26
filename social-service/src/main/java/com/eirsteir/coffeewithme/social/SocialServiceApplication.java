package com.eirsteir.coffeewithme.social;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.sql.SQLException;

@EnableEurekaClient
@EnableJpaRepositories(basePackages = "com.eirsteir.coffeewithme.social.repository")
@SpringBootApplication
public class SocialServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialServiceApplication.class, args);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server inMemoryH2DatabaseServer() throws SQLException {
        return Server.createTcpServer(
                "-tcp", "-tcpAllowOthers", "-tcpPort", "9090");
    }
}
