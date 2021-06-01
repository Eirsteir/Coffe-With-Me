package com.eirsteir.coffeewithme.social.repository

import com.eirsteir.coffeewithme.config.EventuateTestConfig
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus
import com.eirsteir.coffeewithme.social.domain.user.User
import com.eirsteir.coffeewithme.social.repository.rsql.RqslVisitorImpl
import cz.jirutka.rsql.parser.RSQLParser
import cz.jirutka.rsql.parser.ast.Node
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.domain.Specification
import org.springframework.test.context.ActiveProfiles
import java.util.*

@DataJpaTest
@ActiveProfiles("test")
@Import(EventuateTestConfig::class)
internal class UserRepositoryTest {

    private val ALEX_ID: Long = 1L
    private val USER_NAME_ALEX: String = "Alex"
    private val REQUESTER_ID: Long = 2L
    private val ADDRESSEE_ID: Long = 3L
    private val OTHER_USER_ID: Long = 4L

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var user: User
    private lateinit var requester: User
    private lateinit var addressee: User
    private lateinit var otherUser: User
    private lateinit var userJohn: User
    private lateinit var userTom: User
    private lateinit var userPercy: User

    @BeforeEach
    fun setUp() {
        user = User(id = ALEX_ID, nickname = USER_NAME_ALEX, email = EMAIL_ALEX, name = "Alex")
        requester = entityManager.persistFlushFind(
            User(
                id = REQUESTER_ID,
                email = REQUESTER_EMAIL,
                nickname = REQUESTER_NICKNAME
            )
        )
        addressee = entityManager.persistFlushFind(
            User(
                id = ADDRESSEE_ID,
                email = ADDRESSEE_EMAIL,
                nickname = ADDRESSEE_NICKNAME
            )
        )
        otherUser = entityManager.persistFlushFind(
            User(
                id = OTHER_USER_ID,
                email = OTHER_USER_EMAIL,
                nickname = OTHER_USER_NICKNAME
            )
        )
        requester.addFriend(addressee, FriendshipStatus.ACCEPTED)
        requester.addFriend(otherUser, FriendshipStatus.REQUESTED)

        val JOHN_ID = 1005L
        userJohn = entityManager.persistFlushFind(
            User(id = JOHN_ID, name = "john doe", email = "john@doe.com")
        )
        userTom = entityManager.persistFlushFind(
            User(id = 1006L, name = "tom doe", email = "tom@doe.com")
        )
        userPercy = entityManager.persistFlushFind(
            User(id = 1007L, name = "percy blackney", email = "percy@blackney.com")
        )
    }

    @Test
    fun testFindByEmail_thenReturnsOptionalUser() {
        entityManager.persistAndFlush(user)
        val foundUser = userRepository.findByEmail(EMAIL_ALEX)

        assertThat(foundUser).isPresent()
        assertThat(foundUser.get().email).isEqualTo(EMAIL_ALEX)
    }

    @Test
    fun testFindByEmailNotFound_thenReturnEmptyOptional() {
        val foundUser = userRepository.findByEmail(EMAIL_ALEX)
        assertThat(foundUser).isEmpty()
    }

    @Test
    fun testFindAllGivenNameWhenGettingListOfUsers_thenReturnMatchingUsers() {
        val rootNode = RSQLParser().parse("name=='john doe'")
        val spec = rootNode.accept(RqslVisitorImpl<User>())
        val results = userRepository.findAll(spec)

        assertThat(userJohn).isIn(results)
        assertThat(userTom).isNotIn(results)
    }

    @Test
    fun testFindAllGivenFirstNameNegation_thenReturnMatchingUsers() {
        val rootNode = RSQLParser().parse("name!='john doe'")
        val spec = rootNode.accept(RqslVisitorImpl<User>())
        val results = userRepository.findAll(spec)

        assertThat(userTom).isIn(results)
        assertThat(userJohn).isNotIn(results)
    }

    @Test
    fun testFindAllGivenNamePrefix_thenReturnMatchingUsers() {
        val rootNode = RSQLParser().parse("name==jo*")
        val spec = rootNode.accept(RqslVisitorImpl<User>())
        val results = userRepository.findAll(spec)

        assertThat(userJohn).isIn(results)
        assertThat(userTom).isNotIn(results)
    }

    @Test
    fun testFindAllGivenListOfName_thenReturnMatchingUsers() {
        val rootNode = RSQLParser().parse("name=in=('john doe',jack)")
        val spec = rootNode.accept(RqslVisitorImpl<User>())
        val results = userRepository.findAll(spec)

        assertThat(userJohn).isIn(results)
        assertThat(userTom).isNotIn(results)
    }

    @Test
    fun testFindFriendsWhenUserHasFriends_thenReturnFriends() {
    }

    @Test
    fun testFindFriendsWhenUserHasNoFriends_thenReturnEmptyList() {
    }

    @Test
    fun testFindFriendsOfWhenUserHasFriendsOf_thenReturnFriendsOf() {
    }

    @Test
    fun testFindFriendsOfWhenUserHasNoFriendsOf_thenReturnEmptyList() {
    }

    companion object {
        private val EMAIL_ALEX: String = "alex@mail.com"
        private val REQUESTER_EMAIL: String = "requester@test.com"
        private val ADDRESSEE_EMAIL: String = "addressee@test.com"
        private val REQUESTER_NICKNAME: String = "requester"
        private val ADDRESSEE_NICKNAME: String = "addressee"
        private val OTHER_USER_EMAIL: String = "other-user@test.com"
        private val OTHER_USER_NICKNAME: String = "other-user"
    }
}