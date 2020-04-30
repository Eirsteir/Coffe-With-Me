package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    private final String USER_NAME_ALEX = "Alex";
    private static final String EMAIL_ADDRESS_ALEX = "alex@mail.com";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        user = User.builder()
        .username(USER_NAME_ALEX)
        .emailAddress(EMAIL_ADDRESS_ALEX)
        .firstName("Test")
        .lastName("Testesen")
        .password("12345678")
        .confirmPassword("12345678")
        .build();

    }

    @Test
    public void testFindByUsernameReturnsOptionalUser() {
        entityManager.persistAndFlush(user);
        Optional<User> foundUser = userRepository.findByUsername(USER_NAME_ALEX);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void givenEmptyDBWhenFindByUsernameThenReturnEmptyOptional() {
        Optional<User> foundUser = userRepository.findByUsername(USER_NAME_ALEX);

        assertThat(foundUser).isEmpty();
    }

}
