package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.User;
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
public class UserRepositoryIntegrationTest {

    private final String USER_NAME_ALEX = "Alex";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        user = new User("test@test.com",
                USER_NAME_ALEX,
                "Test",
                "Testesen",
                "12345678",
                "12345678",
                null);
    }

    @Test
    public void givenUserInDBWhenFindByUsernameThenReturnOptionalWithUser() {
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
