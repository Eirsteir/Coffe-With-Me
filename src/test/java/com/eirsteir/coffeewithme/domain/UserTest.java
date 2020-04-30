package com.eirsteir.coffeewithme.domain;


import com.eirsteir.coffeewithme.domain.user.Role;
import com.eirsteir.coffeewithme.domain.user.User;
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

import static org.assertj.core.api.Assertions.assertThat;


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
        user = new User();
        user.setUsername(USER_NAME_ALEX);
        user.setFirstName("Test");
        user.setLastName("Testesen");
        user.setPassword("12345678");
        user.setConfirmPassword("12345678");
        user.setRoles(new ArrayList<>());

        adminRole = entityManager.persist(ADMIN_ROLE);
    }

    @Test
    public void addRole() {
        user.addRole(adminRole);
        User savedUser = entityManager.persistFlushFind(user);

        assertThat(savedUser.getRoles()).hasSize(1);
        assertThat(savedUser.getRoles()).contains(adminRole);
    }

    @Test
    public void removeRole() {
        user.addRole(adminRole);
        User foundUser = entityManager.persistFlushFind(user);
        foundUser.removeRole(adminRole);

        User updatedUser = entityManager.persistFlushFind(foundUser);

        assertThat(updatedUser.getRoles()).isEmpty();
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