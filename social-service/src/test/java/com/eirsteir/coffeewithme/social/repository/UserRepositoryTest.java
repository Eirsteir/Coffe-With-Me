package com.eirsteir.coffeewithme.social.repository;

import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.repository.rsql.RqslVisitorImpl;
import com.eirsteir.coffeewithme.testconfig.EventuateTestConfig;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@Import(EventuateTestConfig.class)
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

    private final Long ALEX_ID = 1L;
    private final String USER_NAME_ALEX = "Alex";
    private static final String EMAIL_ALEX = "alex@mail.com";

    private final Long REQUESTER_ID = 2L;
    private static final String REQUESTER_EMAIL = "requester@test.com";

    private final Long ADDRESSEE_ID = 3L;
    private static final String ADDRESSEE_EMAIL = "addressee@test.com";
    private static final String REQUESTER_NICKNAME = "requester";
    private static final String ADDRESSEE_NICKNAME = "addressee";

    private final Long OTHER_USER_ID = 4L;
    private static final String OTHER_USER_EMAIL = "other-user@test.com";
    private static final String OTHER_USER_NICKNAME = "other-user";

    private final Long JOHN_ID = 5L;
    private final Long TOM_ID = 6L;
    private final Long PERCY_ID = 7L;


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

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(ALEX_ID)
                .nickname(USER_NAME_ALEX)
                .email(EMAIL_ALEX)
                .name("Alex")
                .build();

        requester = entityManager.persistFlushFind(User.builder()
                .id(REQUESTER_ID)
                .email(REQUESTER_EMAIL)
                .nickname(REQUESTER_NICKNAME)
                .build());

        addressee = entityManager.persistFlushFind(User.builder()
                .id(ADDRESSEE_ID)
                .email(ADDRESSEE_EMAIL)
                .nickname(ADDRESSEE_NICKNAME)
                .build());

        otherUser = entityManager.persistFlushFind(User.builder()
                .id(OTHER_USER_ID)
                .email(OTHER_USER_EMAIL)
                .nickname(OTHER_USER_NICKNAME)
                .build());

        requester.addFriend(addressee, FriendshipStatus.ACCEPTED);
        requester.addFriend(otherUser, FriendshipStatus.REQUESTED);

        userJohn = entityManager.persistFlushFind(User.builder()
                .id(JOHN_ID)
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
    void testFindFriendsFromWithStatus_thenReturnFriend() {
        List<User> friendsFound = userRepository.findFriendsWithStatus(requester.getId(), FriendshipStatus.ACCEPTED);

        assertThat(addressee).isIn(friendsFound);
        assertThat(otherUser).isNotIn(friendsFound);
    }

    @Test
    void testFindFriendsFromWithStatusWhenRequested_thenReturnFriends() {
        List<User> friendsFound = userRepository.findFriendsWithStatus(requester.getId(), FriendshipStatus.REQUESTED);

        assertThat(otherUser).isIn(friendsFound);
        assertThat(addressee).isNotIn(friendsFound);
    }

    @Test
    void testFindFriendsFromWithStatusWhenUserHasNoFriends_thenReturnEmptyList() {
        List<User> friendsFound = userRepository.findFriendsWithStatus(userJohn.getId(), FriendshipStatus.ACCEPTED);

        assertThat(friendsFound).isEmpty();
    }

    @Test
    void testFindFriendsOfWithStatusWhenUserIsFriendOfAndStatusIsRequested_thenReturnFriend() {
        List<User> friendsFound = userRepository.findFriendsWithStatus(otherUser.getId(), FriendshipStatus.REQUESTED);

        assertThat(requester).isIn(friendsFound);
        assertThat(addressee).isNotIn(friendsFound);
    }

    @Test
    void testFindFriendsOfWithStatusWhenUserIsFriendOfReturnsFriend_thenReturnFriends() {
        List<User> friendsFound = userRepository.findFriendsWithStatus(addressee.getId(), FriendshipStatus.ACCEPTED);

        assertThat(requester).isIn(friendsFound);
        assertThat(otherUser).isNotIn(friendsFound);
    }

    @Test
    void testFindFriendsOfWithStatusWhenUserHasNoFriends_thenReturnEmptyList() {
        List<User> friendsFound = userRepository.findFriendsWithStatus(userJohn.getId(), FriendshipStatus.ACCEPTED);

        assertThat(friendsFound).isEmpty();
    }

    @Test
    void testFindByEmail_thenReturnsOptionalUser() {
        entityManager.persistAndFlush(user);
        Optional<User> foundUser = userRepository.findByEmail(EMAIL_ALEX);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(EMAIL_ALEX);
    }

    @Test
    void testFindByEmailNotFound_thenReturnEmptyOptional() {
        Optional<User> foundUser = userRepository.findByEmail(EMAIL_ALEX);

        assertThat(foundUser).isEmpty();
    }

    @Test
    void testFindAllGivenNameWhenGettingListOfUsers_thenReturnMatchingUsers() {
        Node rootNode = new RSQLParser().parse("name=='john doe'");
        Specification<User> spec = rootNode.accept(new RqslVisitorImpl<>());
        List<User> results = userRepository.findAll(spec);

        assertThat(userJohn).isIn(results);
        assertThat(userTom).isNotIn(results);
    }

    @Test
    void testFindAllGivenFirstNameNegation_thenReturnMatchingUsers() {
        Node rootNode = new RSQLParser().parse("name!='john doe'");
        Specification<User> spec = rootNode.accept(new RqslVisitorImpl<>());
        List<User> results = userRepository.findAll(spec);

        assertThat(userTom).isIn(results);
        assertThat(userJohn).isNotIn(results);
    }

    @Test
    void testFindAllGivenNamePrefix_thenReturnMatchingUsers() {
        Node rootNode = new RSQLParser().parse("name==jo*");
         Specification<User> spec = rootNode.accept(new RqslVisitorImpl<>());
        List<User> results = userRepository.findAll(spec);

        assertThat(userJohn).isIn(results);
        assertThat(userTom).isNotIn(results);
    }

    @Test
    void testFindAllGivenListOfName_thenReturnMatchingUsers() {
        Node rootNode = new RSQLParser().parse("name=in=('john doe',jack)");
        Specification<User> spec = rootNode.accept(new RqslVisitorImpl<>());
        List<User> results = userRepository.findAll(spec);

        assertThat(userJohn).isIn(results);
        assertThat(userTom).isNotIn(results);
    }

    @Test
    void testFindFriendsWhenUserHasFriends_thenReturnFriends() {

    }

    @Test
    void testFindFriendsWhenUserHasNoFriends_thenReturnEmptyList() {

    }

    @Test
    void testFindFriendsOfWhenUserHasFriendsOf_thenReturnFriendsOf() {

    }

    @Test
    void testFindFriendsOfWhenUserHasNoFriendsOf_thenReturnEmptyList() {

    }
}
