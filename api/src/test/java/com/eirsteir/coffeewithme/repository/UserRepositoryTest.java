package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.web.util.SearchOperation;
import com.eirsteir.coffeewithme.web.util.SpecSearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
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

    private User userJohn;

    private User userTom;

    private User userPercy;

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
                .requester(requester)
                .addressee(addressee)
                .build();
        entityManager.persistAndFlush(Friendship.builder()
                                              .id(friendshipId)
                                              .requester(requester)
                                              .addressee(addressee)
                                              .status(FriendshipStatus.ACCEPTED)
                                              .build());

        FriendshipId requestedFriendshipId = FriendshipId.builder()
                                                .requester(requester)
                                                .addressee(otherUser)
                                                .build();
        entityManager.persistFlushFind(Friendship.builder()
                                               .id(requestedFriendshipId)
                                               .requester(requester)
                                               .addressee(otherUser)
                                               .status(FriendshipStatus.REQUESTED)
                                               .build());

        userJohn = entityManager.persistFlushFind(User.builder()
                                                          .name("john doe")
                                                          .email("john@doe.com")
                                                          .build());

        userTom = entityManager.persistFlushFind(User.builder()
                                                         .name("tom doe")
                                                         .email("tom@doe.com")
                                                         .build());

        userPercy = entityManager.persistFlushFind(User.builder()
                                                         .name("percy blackney")
                                                         .email("percy@blackney.com")
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

    @Test
    public void givenFirstAndLastName_whenGettingListOfUsers_thenCorrect() {
        UserSpecification spec = new UserSpecification(
                new SpecSearchCriteria("name", SearchOperation.EQUALITY, "john doe"));
        List<User> results = userRepository.findAll(Specification.where(spec));

        assertThat(userJohn).isIn(results);
        assertThat(userTom).isNotIn(results);
    }

    @Test
    public void givenFirstNameInverse_whenGettingListOfUsers_thenCorrect() {
        UserSpecification spec = new UserSpecification(
                new SpecSearchCriteria("name", SearchOperation.NEGATION, "john doe"));
        List<User> results = userRepository.findAll(Specification.where(spec));

        assertThat(userTom).isIn(results);
        assertThat(userJohn).isNotIn(results);
    }

    @Test
    public void givenFirstNamePrefix_whenGettingListOfUsers_thenCorrect() {
        UserSpecification spec = new UserSpecification(
                new SpecSearchCriteria("name", SearchOperation.STARTS_WITH, "jo"));
        List<User> results = userRepository.findAll(spec);

        assertThat(userJohn).isIn(results);
        assertThat(userTom).isNotIn(results);
    }

    @Test
    public void givenFirstNameSuffix_whenGettingListOfUsers_thenCorrect() {
        UserSpecification spec = new UserSpecification(
                new SpecSearchCriteria("name", SearchOperation.ENDS_WITH, "ey"));
        List<User> results = userRepository.findAll(spec);

        assertThat(userPercy).isIn(results);
        assertThat(userTom).isNotIn(results);
    }

    @Test
    public void givenFirstNameSubstring_whenGettingListOfUsers_thenCorrect() {
        UserSpecification spec = new UserSpecification(
                new SpecSearchCriteria("name", SearchOperation.CONTAINS, "oh"));
        List<User> results = userRepository.findAll(spec);

        assertThat(userJohn).isIn(results);
        assertThat(userTom).isNotIn(results);
    }
}
