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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceImplTest {

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

    List<User> allUsers = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .username(USER_NAME_ALEX)
                .firstName("Test")
                .lastName("Testesen")
                .password("12345678")
                .confirmPassword("12345678")
                .roles(new ArrayList<>())
                .build();

        allUsers.add(user);

        Mockito.when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        Mockito.when(userRepository.findAll()).thenReturn(allUsers);

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

    @Test
    void testGetAllUsersReturnsAllUsers() {
        List<User> foundUsers = userService.getAllUsers();
        assertThat(foundUsers).isEqualTo(allUsers);
    }

    @Test
    void testUpdateUserReturnsUpdatedUser() {
        User savedUser = userService.saveUser(user);
        assertThat(savedUser.getUsername()).isEqualTo(USER_NAME_ALEX);
    }
}
