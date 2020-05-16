package com.eirsteir.coffeewithme.service.friendship;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.FriendRequest;

import java.util.List;

public interface FriendshipService {

    List<User> findFriends(Long id, FriendshipStatus status);

    FriendshipDto registerFriendship(FriendRequest friendRequest);

    void removeFriendship(FriendshipDto friendshipDto);

    FriendshipDto updateFriendship(FriendshipDto friendshipDto);

    List<UserDto> getFriends(UserDto user);

    List<UserDto> getFriendshipsWithStatus(UserDto user, FriendshipStatus status);

}
