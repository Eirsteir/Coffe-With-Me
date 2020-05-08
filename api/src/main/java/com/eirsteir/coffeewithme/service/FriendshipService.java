package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.web.dto.FriendRequestDto;
import com.eirsteir.coffeewithme.web.dto.FriendshipDto;
import com.eirsteir.coffeewithme.web.dto.UserDto;

import java.util.Collection;

public interface FriendshipService {

    FriendshipDto addFriendship(FriendRequestDto friendRequestDto);

}
