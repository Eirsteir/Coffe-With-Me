package com.eirsteir.coffeewithme.models;


import com.eirsteir.coffeewithme.testUtils.BlankStringsArgumentsProvider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTest {

    private final String USER_NAME_ALEX = "Alex";
    private final Role ADMIN_ROLE = new Role("ADMIN");

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;
    private Role adminRole;

    @BeforeEach
    public void setUp() {
        user = new User("test@test.com",
                USER_NAME_ALEX,
                "Test",
                "Testesen",
                "12345678",
                "12345678",
                new ArrayList<>());

        adminRole = entityManager.persist(ADMIN_ROLE);
    }

    @Test
    public void addRole() {
        user.addRole(adminRole);
        User savedUser = entityManager.persistFlushFind(user);

        assertEquals(1, savedUser.getRoles().size());
        assertEquals(adminRole, savedUser.getRoles().get(0));
    }

    @Test
    public void removeRole() {
        user.addRole(adminRole);
        User foundUser = entityManager.persistFlushFind(user);
        foundUser.removeRole(adminRole);

        User updatedUser = entityManager.persistFlushFind(foundUser);

        assertTrue(updatedUser.getRoles().isEmpty());
    }


    private static Stream<Arguments> invalidEmailAddressProvider() {
        return Stream.of(
                Arguments.of("Invalid email")
        );
    }

    @ParameterizedTest
    @ArgumentsSource(BlankStringsArgumentsProvider.class)
    @MethodSource("invalidEmailAddressProvider")
    public void createUserWithInvalidEmail(String invalidInput) throws Exception {
        thrown.expect(ConstraintViolationException.class);

        user.setEmailAddress(invalidInput);
    }


    @ParameterizedTest
    @ArgumentsSource(BlankStringsArgumentsProvider.class)
    public void createUserWithInvalidUsername(String invalidInput) throws ConstraintViolationException {
        thrown.expect(ConstraintViolationException.class);

        user.setUsername(invalidInput);
    }
}