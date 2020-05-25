package com.eirsteir.coffeewithme.api.repository;

import com.eirsteir.coffeewithme.api.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.api.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.api.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.api.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class FriendshipRepositoryTest {
    private static final String REQUESTER_USERNAME = "requester";
    private static final String ADDRESSEE_USERNAME = "addressee";

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

        entityManager.persistAndFlush(Friendship.builder()
                                              .requester(requester)
                                              .addressee(addressee)
                                              .status(FriendshipStatus.ACCEPTED)
                                              .build());
    }

    @Test
    void testFindByIdRequesterOrIdAddresseeAndStatusWhenNoResults() {
        List<Friendship> friendsFound = friendshipRepository
                .findByUserIdAndStatus(requester.getId(), FriendshipStatus.REQUESTED);

        assertThat(friendsFound).isEmpty();
    }

    @Test
    void testFindAllByExampleOfRequesterWhenRequesterIsAddressee() {
        User otherUser = entityManager.persistFlushFind(User.builder().build());

        entityManager.persistAndFlush(Friendship.builder()
                                                   .requester(addressee)
                                                   .addressee(requester)
                                                   .status(FriendshipStatus.ACCEPTED)
                                                   .build());

        List<Friendship> friendsFound = friendshipRepository
                .findByUserIdAndStatus(requester.getId(), FriendshipStatus.ACCEPTED);

        assertThat(friendsFound).hasSize(2);
    }

    @Test
    void testFindByIdRequesterIdAndIdAddresseeIdWhenExists() {
        Optional<Friendship> friendshipFound = friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
                requester.getId(), addressee.getId());

        assertThat(friendshipFound).isPresent();
    }

    @Test
    void testFindByIdRequesterIdAndIdAddresseeIdWhenNotExists() {
        Optional<Friendship> friendshipFound = friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
                requester.getId(), 100L);

        assertThat(friendshipFound).isEmpty();
    }

}