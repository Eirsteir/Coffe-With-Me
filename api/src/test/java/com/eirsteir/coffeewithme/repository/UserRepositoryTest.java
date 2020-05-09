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


@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    private final String USER_NAME_ALEX = "Alex";
    private static final String EMAIL_ALEX = "alex@mail.com";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        user = User.builder()
                .username(USER_NAME_ALEX)
                .email(EMAIL_ALEX)
                .name("Alex")
                .mobileNumber("12345678")
                .build();
    }

    @Test
    public void testFindByEmailReturnsOptionalUser() {
        entityManager.persistAndFlush(user);
        Optional<User> foundUser = userRepository.findByEmail(EMAIL_ALEX);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(EMAIL_ALEX);
    }

    @Test
    public void testFindByEmailNotFoundReturnEmptyOptional() {
        Optional<User> foundUser = userRepository.findByEmail(EMAIL_ALEX);

        assertThat(foundUser).isEmpty();
    }

}
