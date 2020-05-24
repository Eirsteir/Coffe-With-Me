package com.eirsteir.coffeewithme.api.service.friendship;

import com.eirsteir.coffeewithme.api.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.api.domain.user.User;
import com.eirsteir.coffeewithme.api.dto.FriendshipDto;
import com.eirsteir.coffeewithme.api.dto.UserDto;
import com.eirsteir.coffeewithme.api.web.request.FriendRequest;

import java.util.List;

public interface FriendshipService {

    List<User> findFriends(Long id, FriendshipStatus status);

    FriendshipDto registerFriendship(FriendRequest friendRequest);

    void removeFriendship(FriendshipDto friendshipDto);

    FriendshipDto updateFriendship(FriendshipDto friendshipDto);

    List<UserDto> getFriends(UserDto user);

    List<UserDto> getAllFriendshipsWithStatus(UserDto user, FriendshipStatus status);

    List<UserDto> getFriendsOfWithStatus(UserDto userDto, FriendshipStatus status);
}
