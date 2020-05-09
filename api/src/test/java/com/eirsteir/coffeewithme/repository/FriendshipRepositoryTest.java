package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
class FriendshipRepositoryTest {
    private static final String REQUESTER_USERNAME = "requester";
    private static final String ADDRESSEE_USERNAME = "addressee";

    private Friendship friendship;
    private FriendshipId friendshipId;
    private User requester;
    private User addressee;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @BeforeEach
    void setUp() {
        requester = entityManager.persistFlushFind(User.builder()
                .username(REQUESTER_USERNAME)
                .build());

        addressee = entityManager.persistFlushFind(User.builder()
                .username(ADDRESSEE_USERNAME)
                .build());

        friendshipId = FriendshipId.builder()
                .requester(requester)
                .addressee(addressee)
                .build();
    }

    @Test
    void testFindByIdRequesterOrIdAddresseeAndStatusWhenNoResults() {
        List<Friendship> friendsFound = friendshipRepository
                .findByIdRequesterOrIdAddresseeAndStatus(requester, requester, FriendshipStatus.ACCEPTED);
        assertThat(friendsFound).isEmpty();
    }

    @Test
    void testFindAllByExampleOfRequesterWhenRequesterIsAddressee() {
        User otherUser = entityManager.persistFlushFind(User.builder().build());
        FriendshipId id = FriendshipId.builder()
                .requester(otherUser)
                .addressee(requester)
                .build();

        friendship = entityManager.persistAndFlush(Friendship.builder()
                                                           .id(id)
                                                           .status(FriendshipStatus.ACCEPTED)
                                                           .build());
        friendship = entityManager.persistAndFlush(Friendship.builder()
                                                           .id(friendshipId)
                                                           .status(FriendshipStatus.ACCEPTED)
                                                           .build());

        List<Friendship> friendsFound = friendshipRepository
                .findByIdRequesterOrIdAddresseeAndStatus(requester, requester, FriendshipStatus.ACCEPTED);

        assertThat(friendsFound).hasSize(2);
    }

    @Test
    void testFindByIdRequesterOrIdAddresseeAndStatusWhenExists() {
        friendship = entityManager.persistAndFlush(Friendship.builder()
                .id(friendshipId)
                .status(FriendshipStatus.ACCEPTED)
                .build());

        List<Friendship> friendsFound = friendshipRepository
                .findByIdRequesterOrIdAddresseeAndStatus(requester, requester, FriendshipStatus.ACCEPTED);
        assertThat(friendsFound).hasSize(1);
    }
}