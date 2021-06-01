package com.eirsteir.coffeewithme.social.domain.user

import com.eirsteir.coffeewithme.social.domain.friendship.Friendship
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserTest {
    private lateinit var user: User
    private lateinit var friend: User

    @BeforeEach
    fun setUp() {
        user = User(id = 1L)
        friend = User(id = 2L)
        user.addFriend(friend, FriendshipStatus.REQUESTED)
    }

    @Test
    fun testAddFriend() {
        val addressee = user.getFriendships().first().addressee
        val status = user.getFriendships().first().status

        assertThat(friend).isEqualTo(addressee)
        assertThat(status).isEqualTo(FriendshipStatus.REQUESTED)
    }

    @Test
    fun testRemoveFriendship() {
        val friendship: Friendship = user.getFriendships().first()
        user.removeFriendship(friendship)

        assertThat(user.getFriends()).isEmpty()
    }
}