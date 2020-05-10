package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
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
    private static final String REQUESTER_EMAIL = "requester@test.com";
    private static final String ADDRESSEE_EMAIL = "addressee@test.com";
    private static final String REQUESTER_USERNAME = "requester";
    private static final String ADDRESSEE_USERNAME = "addressee";
    public static final String OTHER_USER_EMAIL = "other-user@test.com";
    public static final String OTHER_USER_USERNAME = "other-user";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User requester;
    private User addressee;
    private User otherUser;

    @Before
    public void setUp() {
        user = User.builder()
                .username(USER_NAME_ALEX)
                .email(EMAIL_ALEX)
                .name("Alex")
                .mobileNumber("12345678")
                .build();

        requester = entityManager.persistFlushFind(User.builder()
                                                                .email(REQUESTER_EMAIL)
                                                                .username(REQUESTER_USERNAME)
                                                                .build());

        addressee = entityManager.persistFlushFind(User.builder()
                                                                .email(ADDRESSEE_EMAIL)
                                                                .username(ADDRESSEE_USERNAME)
                                                                .build());

        otherUser = entityManager.persistFlushFind(User.builder()
                                                                .email(OTHER_USER_EMAIL)
                                                                .username(OTHER_USER_USERNAME)
                                                                .build());

        FriendshipId friendshipId = FriendshipId.builder()
                .requesterId(requester.getId())
                .addresseeId(addressee.getId())
                .build();
        entityManager.persistAndFlush(Friendship.builder()
                                              .id(friendshipId)
                                              .requester(requester)
                                              .addressee(addressee)
                                              .status(FriendshipStatus.ACCEPTED)
                                              .build());

        FriendshipId requestedFriendshipId = FriendshipId.builder()
                                                .requesterId(requester.getId())
                                                .addresseeId(otherUser.getId())
                                                .build();
        entityManager.persistFlushFind(Friendship.builder()
                                               .id(requestedFriendshipId)
                                               .requester(requester)
                                               .addressee(otherUser)
                                               .status(FriendshipStatus.REQUESTED)
                                               .build());
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
