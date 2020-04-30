package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryIntegrationTest {

    private final String USER_NAME_ALEX = "Alex";
    private static final String EMAIL_ADDRESS_ALEX = "alex@email.com";

    @Autowired
    private Validator validator;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        user = User.builder()
                .username(USER_NAME_ALEX)
                .emailAddress(EMAIL_ADDRESS_ALEX)
                .firstName(null)
                .lastName("Testesen")
                .password("12345678")
                .confirmPassword("12345678")
                .build();

    }

    @Test
    public void testFindByUsernameReturnsOptionalUser() {
    }

    @Test
    public void givenEmptyDBWhenFindByUsernameThenReturnEmptyOptional() {
    }

    @Test
    public void testCreateUserWithNonUniqueUsername() {
        userRepository.save(user);

        // when
        User newUser = User.builder()
                .username(USER_NAME_ALEX)
                .emailAddress("unique@email.com")
                .firstName("Test")
                .lastName("Testesen")
                .password("12345678")
                .confirmPassword("12345678")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(newUser);
        // then
        assertThat(violations).hasSize(1);;
    }
}
