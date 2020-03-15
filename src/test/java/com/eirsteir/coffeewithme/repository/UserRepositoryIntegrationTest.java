package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


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
    public void givenUserInDBWhenFindOneByUsernameThenReturnOptionalWithUser() {
        entityManager.persistAndFlush(user);
        Optional<User> foundUser = userRepository.findOneByUsername(USER_NAME_ALEX);

        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
    }

    @Test
    public void givenEmptyDBWhenFindOneByUsernameThenReturnEmptyOptional() {
        Optional<User> foundUser = userRepository.findOneByUsername(USER_NAME_ALEX);

        assertFalse(foundUser.isPresent());
    }

}
