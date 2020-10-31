package com.eirsteir.coffeewithme.social.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.eirsteir.coffeewithme.config.EventuateTestConfig;
import com.eirsteir.coffeewithme.social.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ActiveProfiles("test")
@Import(EventuateTestConfig.class)
@ExtendWith(SpringExtension.class)
class FriendshipRepositoryTest {
  private static final String REQUESTER_NICKNAME = "requester";
  private static final String ADDRESSEE_NICKNAME = "addressee";

  private User requester;
  private User addressee;

  @Autowired private TestEntityManager entityManager;

  @Autowired private FriendshipRepository friendshipRepository;

  @BeforeEach
  void setUp() {
    requester =
        entityManager.persistFlushFind(
            User.builder().id(1000L).nickname(REQUESTER_NICKNAME).build());

    addressee =
        entityManager.persistFlushFind(
            User.builder().id(1001L).nickname(ADDRESSEE_NICKNAME).build());

    entityManager.persistAndFlush(
        Friendship.builder()
            .requester(requester)
            .addressee(addressee)
            .status(FriendshipStatus.ACCEPTED)
            .build());
  }

  @Test
  void testFindByIdRequesterOrIdAddresseeAndStatusWhenNoResults() {
    List<Friendship> friendsFound =
        friendshipRepository.findByUserAndStatus(requester.getId(), FriendshipStatus.REQUESTED);

    assertThat(friendsFound).isEmpty();
  }

  @Test
  void testFindAllByExampleOfRequesterWhenRequesterIsAddressee() {
    User otherUser = entityManager.persistFlushFind(User.builder().id(1003L).build());

    entityManager.persistAndFlush(
        Friendship.builder()
            .requester(addressee)
            .addressee(requester)
            .status(FriendshipStatus.ACCEPTED)
            .build());

    List<Friendship> friendsFound =
        friendshipRepository.findByUserAndStatus(requester.getId(), FriendshipStatus.ACCEPTED);

    assertThat(friendsFound).hasSize(2);
  }

  @Test
  void testFindByIdRequesterIdAndIdAddresseeIdWhenExists() {
    Optional<Friendship> friendshipFound =
        friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
            requester.getId(), addressee.getId());

    assertThat(friendshipFound).isPresent();
  }

  @Test
  void testFindByIdRequesterIdAndIdAddresseeIdWhenNotExists() {
    Optional<Friendship> friendshipFound =
        friendshipRepository.findByIdRequesterIdAndIdAddresseeId(requester.getId(), 100L);

    assertThat(friendshipFound).isEmpty();
  }
}
