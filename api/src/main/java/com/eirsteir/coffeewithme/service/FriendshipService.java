package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.web.dto.FriendRequestDto;
import com.eirsteir.coffeewithme.web.dto.FriendshipDto;

public interface FriendshipService {


    FriendshipDto addFriendship(FriendRequestDto friendRequestDto);
}
