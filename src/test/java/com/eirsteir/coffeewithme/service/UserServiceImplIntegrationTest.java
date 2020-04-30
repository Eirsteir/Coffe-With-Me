package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.testUtils.BlankStringsArgumentsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
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

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUsername(USER_NAME_ALEX);
        user.setFirstName("Test");
        user.setLastName("Testesen");
        user.setPassword("12345678");
        user.setConfirmPassword("12345678");
        user.setRoles(new ArrayList<>());

        Mockito.when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
    }

    @Test
    void testGetUserByUsernameWithValidUsernameShouldFindUser() {
        Optional<User> foundUser = userService.getUserByUsername(USER_NAME_ALEX);

        assertThat(foundUser).isPresent();
        assertThat(USER_NAME_ALEX).isEqualTo(foundUser.get().getUsername());
    }


    @ParameterizedTest
    @ArgumentsSource(BlankStringsArgumentsProvider.class)
    void testGetUserByUsernameWithInvalidUsernameDoesNotFindUser() {
        Optional<User> foundUser = userService.getUserByUsername(null);

        assertThat(foundUser).isEmpty();
    }

    @Test
    void testSaveUserReturnsSavedUser() {
        User savedUser = userService.saveUser(user);

        assertThat(savedUser.getUsername()).isEqualTo(USER_NAME_ALEX);
    }
}
