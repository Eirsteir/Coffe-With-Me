package com.eirsteir.coffeewithme.api.service.friendship;

import com.eirsteir.coffeewithme.commons.dto.UserDetails;
import com.eirsteir.coffeewithme.api.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.api.domain.user.User;
import com.eirsteir.coffeewithme.api.dto.FriendshipDto;
import com.eirsteir.coffeewithme.api.web.request.FriendRequest;

import java.util.List;

public interface FriendshipService {

    List<User> findFriends(Long id, FriendshipStatus status);

    FriendshipDto registerFriendship(FriendRequest friendRequest);

    void removeFriendship(FriendshipDto friendshipDto);

    FriendshipDto updateFriendship(FriendshipDto friendshipDto);

    List<UserDetails> getFriends(UserDetails user);

    List<UserDetails> getAllFriendshipsWithStatus(UserDetails user, FriendshipStatus status);

    List<UserDetails> getFriendsOfWithStatus(UserDetails userDetails, FriendshipStatus status);
}
