package com.eirsteir.coffeewithme.domain;


import com.eirsteir.coffeewithme.domain.user.Role;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.testUtils.BlankStringsArgumentsProvider;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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

    @Autowired
    private TestEntityManager entityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private User user;
    private Role adminRole;

    @Before
    public void setUp() {
        user = User.builder()
                .username("Alex")
                .emailAddress("alex@email.com")
                .password("12345678")
                .roles(new ArrayList<>())
                .build();

        adminRole = Role.builder()
                .name("ADMIN")
                .build();
    }

    @Test
    public void testAddRole() {
        Role savedAdminRole = entityManager.persistAndFlush(adminRole);
        user.addRole(savedAdminRole);
        User updatedUser = entityManager.persistAndFlush(user);

        assertThat(updatedUser.getRoles()).hasSize(1);
    }

    @Test
    public void testRemoveRole() {
        Role savedAdminRole = entityManager.persistAndFlush(adminRole);
        user.addRole(savedAdminRole);
        User updatedUser = entityManager.persistAndFlush(user);

        updatedUser.removeRole(savedAdminRole);
        User userWithoutRoles = entityManager.persistFlushFind(user);

        assertThat(userWithoutRoles.getRoles()).isEmpty();
    }

    private static Stream<Arguments> invalidEmailAddressProvider() {
        return Stream.of(
                Arguments.of("Invalid email")
        );
    }

    @ParameterizedTest
    @ArgumentsSource(BlankStringsArgumentsProvider.class)
    @MethodSource("invalidEmailAddressProvider")
    public void testCreateUserWithInvalidEmail(String invalidInput) throws ConstraintViolationException {
        thrown.expect(ConstraintViolationException.class);

        user.setEmailAddress(invalidInput);
    }


    @ParameterizedTest
    @ArgumentsSource(BlankStringsArgumentsProvider.class)
    public void testCreateUserWithInvalidUsername(String invalidInput) throws ConstraintViolationException {
        thrown.expect(ConstraintViolationException.class);

        user.setUsername(invalidInput);
    }
}