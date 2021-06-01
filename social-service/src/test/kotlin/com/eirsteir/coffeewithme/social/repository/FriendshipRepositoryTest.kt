package com.eirsteir.coffeewithme.social.repository

import com.eirsteir.coffeewithme.config.EventuateTestConfig
import com.eirsteir.coffeewithme.social.domain.friendship.Friendship
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus
import com.eirsteir.coffeewithme.social.domain.user.User
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("test")
@Import(EventuateTestConfig::class)
@DataJpaTest
internal class FriendshipRepositoryTest {

    private lateinit var requester: User
    private lateinit var addressee: User

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var friendshipRepository: FriendshipRepository

    @BeforeEach
    fun setUp() {
        requester = entityManager.persistFlushFind(
            User(id = 1000, nickname = REQUESTER_NICKNAME)
        )
        addressee = entityManager.persistFlushFind(
            User(id = 1001L, nickname = ADDRESSEE_NICKNAME)
        )
        entityManager.persistAndFlush(
            Friendship(
                requester = requester,
                addressee = addressee,
                status = FriendshipStatus.ACCEPTED,
                )
        )
    }

    @Test
    fun testFindByIdRequesterOrIdAddresseeAndStatusWhenNoResults() {
        val friendsFound = friendshipRepository.findByUserAndStatus(requester.id!!, FriendshipStatus.REQUESTED)

        assertThat(friendsFound).isEmpty()
    }

    @Test
    fun testFindAllByExampleOfRequesterWhenRequesterIsAddressee() {
        val otherUser = entityManager.persistFlushFind(User(id = 1003L))
        entityManager.persistAndFlush(
            Friendship(
                requester = addressee,
                addressee = requester,
                status = FriendshipStatus.ACCEPTED
            )
        )
        val friendsFound = friendshipRepository.findByUserAndStatus(requester.id!!, FriendshipStatus.ACCEPTED)

        assertThat(friendsFound).hasSize(2)
    }

    @Test
    fun testFindByIdRequesterIdAndIdAddresseeIdWhenExists() {
        val friendshipFound = friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
            requester.id!!, addressee.id!!
        )

        assertThat(friendshipFound).isPresent
    }

    @Test
    fun testFindByIdRequesterIdAndIdAddresseeIdWhenNotExists() {
        val friendshipFound = friendshipRepository.findByIdRequesterIdAndIdAddresseeId(requester.id!!, 100L)

        assertThat(friendshipFound).isEmpty
    }

    companion object {
        private val REQUESTER_NICKNAME: String = "requester"
        private val ADDRESSEE_NICKNAME: String = "addressee"
    }
}