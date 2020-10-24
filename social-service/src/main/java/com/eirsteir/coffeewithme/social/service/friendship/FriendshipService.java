package com.eirsteir.coffeewithme.social.service.friendship;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;

import java.util.List;

public interface FriendshipService {

    List<FriendshipDto> findFriendships(Long id, FriendshipStatus status);

    List<FriendshipDto> findFriendshipsAtUniversity(User user);

    FriendshipDto registerFriendship(FriendRequest friendRequest);

    void removeFriendship(FriendshipDto friendshipDto);

    FriendshipDto updateFriendship(FriendshipDto friendshipDto);

    List<FriendshipDto> findFriendships(UserDetailsDto user);

    List<FriendshipDto> getAllFriendshipsWithStatus(UserDetailsDto user, FriendshipStatus status);

    Integer getFriendsCount(Long userId);

}
