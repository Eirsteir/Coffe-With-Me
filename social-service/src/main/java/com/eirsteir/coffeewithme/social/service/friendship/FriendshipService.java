package com.eirsteir.coffeewithme.social.service.friendship;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;

import java.util.List;

public interface FriendshipService {

    List<User> findFriends(Long id, FriendshipStatus status);

    FriendshipDto registerFriendship(FriendRequest friendRequest);

    void removeFriendship(FriendshipDto friendshipDto);

    FriendshipDto updateFriendship(FriendshipDto friendshipDto);

    List<UserDetailsDto> getFriends(UserDetailsDto user);

    List<UserDetailsDto> getAllFriendshipsWithStatus(UserDetailsDto user, FriendshipStatus status);

    List<UserDetailsDto> getFriendsOfWithStatus(UserDetailsDto UserDetailsDto, FriendshipStatus status);
}
