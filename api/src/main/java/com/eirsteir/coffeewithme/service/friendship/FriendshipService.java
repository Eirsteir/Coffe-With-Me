package com.eirsteir.coffeewithme.service.friendship;

import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.FriendRequest;

import javax.validation.Valid;
import java.util.List;

public interface FriendshipService {

    FriendshipDto registerFriendship(@Valid FriendRequest friendRequest);

    void removeFriendship(FriendshipDto friendshipDto);

    FriendshipDto acceptFriendship(FriendshipDto friendshipDto);

    FriendshipDto blockFriendShip(FriendshipDto friendshipDto);

    List<UserDto> findFriendsOf(UserDto userDto);

}
