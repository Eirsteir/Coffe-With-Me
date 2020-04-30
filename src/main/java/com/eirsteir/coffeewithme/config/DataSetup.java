package com.eirsteir.coffeewithme.config;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
class DataSetup implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        userRepository.save(User.builder()
                                    .username("alex")
                                    .email("alex.testesen@email.com")
                                    .password("12345678")
                                    .build());

        userRepository.save(User.builder()
                                    .username("adam")
                                    .email("adam.testesen@email.com")
                                    .password("12345678")
                                    .build());
    }
}
