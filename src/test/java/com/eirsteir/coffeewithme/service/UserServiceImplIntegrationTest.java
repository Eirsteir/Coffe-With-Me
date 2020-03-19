package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.User;
import com.eirsteir.coffeewithme.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceImplIntegrationTest {

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    private static final String USER_NAME_ALEX = "Alex";

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


    @BeforeEach
    public void setUp() {
        User user = new User("test@test.com",
                USER_NAME_ALEX,
                "Test",
                "Testesen",
                "12345678",
                "12345678",
                new ArrayList<>());

        Mockito.when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
    }

    @Test
    void testGetUserByUsernameWithValidUsernameShouldFindUser() {
        Optional<User> foundUser = userService.getUserByUsername(USER_NAME_ALEX);

        assertThat(foundUser).isPresent();
        assertThat(USER_NAME_ALEX).isEqualTo(foundUser.get().getUsername());
    }
}
