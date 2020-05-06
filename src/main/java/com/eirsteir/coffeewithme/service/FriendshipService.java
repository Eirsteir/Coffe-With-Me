package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.web.dto.FriendRequestDto;

public interface FriendshipService {

    FriendRequestDto addFriendRequest(FriendRequestDto friendRequestDto);

    FriendRequestDto acceptFriendRequest(FriendRequestDto friendRequestDto);

    FriendRequestDto rejectFriendRequest(FriendRequestDto friendRequestDto);

    FriendRequestDto cancelFriendRequest(FriendRequestDto friendRequestDto);
}
