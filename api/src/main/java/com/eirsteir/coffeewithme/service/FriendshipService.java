package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.FriendshipRequest;

import java.util.Collection;

public interface FriendshipService {

    FriendshipDto registerFriendship(FriendshipRequest friendshipRequest);

    void removeFriendship(FriendshipDto friendshipDto);

    FriendshipDto acceptFriendship(FriendshipDto friendshipDto);

    FriendshipDto blockFriendShip(FriendshipDto friendshipDto);

    Collection<UserDto> findFriendsOf(UserDto userDto);

}
