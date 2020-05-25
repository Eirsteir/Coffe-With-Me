package com.eirsteir.coffeewithme.api.domain.user;

import com.eirsteir.coffeewithme.api.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.api.domain.friendship.FriendshipStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;
    private User friend;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .build();

        friend = User.builder()
                .id(2L)
                .build();

        user.addFriend(friend, FriendshipStatus.REQUESTED);
    }

    @Test
    void testAddFriend() {
        User addressee = user.getFriends().get(0).getAddressee();
        User requester = friend.getFriendsOf().get(0).getRequester();
        FriendshipStatus status = user.getFriends().get(0).getStatus();

        assertThat(friend).isEqualTo(addressee);
        assertThat(user).isEqualTo(requester);
        assertThat(status).isEqualTo(FriendshipStatus.REQUESTED);
    }

    @Test
    void testRemoveFriendship() {
        Friendship friendship = user.getFriends().get(0);
        user.removeFriendship(friendship);

        assertThat(user.getFriends()).isEmpty();
        assertThat(friend.getFriendsOf()).isEmpty();
    }
}