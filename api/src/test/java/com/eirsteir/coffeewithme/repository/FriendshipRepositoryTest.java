package com.eirsteir.coffeewithme.repository;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipPk;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
class FriendshipRepositoryTest {
    private static final String REQUESTER_USERNAME = "requester";
    private static final String ADDRESSEE_USERNAME = "addressee";

    private Friendship friendship;
    private FriendshipPk friendshipPk;
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

        friendshipPk = FriendshipPk.builder()
                .requester(requester)
                .addressee(addressee)
                .build();

        entityManager.persistAndFlush(Friendship.builder()
                                              .pk(friendshipPk)
                                              .requester(requester)
                                              .addressee(addressee)
                                              .status(FriendshipStatus.ACCEPTED)
                                              .build());
    }

    @Test
    void testFindByIdRequesterOrIdAddresseeAndStatusWhenNoResults() {
        List<Friendship> friendsFound = friendshipRepository
                .findByPkRequesterIdOrPkAddresseeIdAndStatus(requester.getId(), requester.getId(), FriendshipStatus.ACCEPTED);
        assertThat(friendsFound).isEmpty();
    }

    @Test
    void testFindAllByExampleOfRequesterWhenRequesterIsAddressee() {
        User otherUser = entityManager.persistFlushFind(User.builder().build());
        FriendshipPk id = FriendshipPk.builder()
                .requester(otherUser)
                .addressee(requester)
                .build();

        entityManager.persistAndFlush(Friendship.builder()
                                                            .pk(id)
                                                           .requester(requester)
                                                           .addressee(addressee)
                                                           .status(FriendshipStatus.ACCEPTED)
                                                           .build());

        List<Friendship> friendsFound = friendshipRepository
                .findByPkRequesterIdOrPkAddresseeIdAndStatus(requester.getId(), requester.getId(), FriendshipStatus.ACCEPTED);

        assertThat(friendsFound).hasSize(2);
    }

    @Test
    void testFindByIdRequesterIdAndIdAddresseeIdWhenExists() {
        Optional<Friendship> friendshipFound = friendshipRepository.findByPkRequesterIdAndPkAddresseeId(
                requester.getId(), addressee.getId());

        assertThat(friendshipFound).isPresent();
    }

    @Test
    void testFindByIdRequesterIdAndIdAddresseeIdWhenNotExists() {
        Optional<Friendship> friendshipFound = friendshipRepository.findByPkRequesterIdAndPkAddresseeId(
                requester.getId(), 100L);

        assertThat(friendshipFound).isEmpty();
    }

}