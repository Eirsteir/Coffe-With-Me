package com.eirsteir.coffeewithme.social.service.friendship;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;
import java.util.List;

public interface FriendshipService {

  List<FriendshipDto> findFriendshipsOf(Long id, FriendshipStatus status);

  List<FriendshipDto> findFriendshipsAtUniversity(User user);

  FriendshipDto registerFriendship(FriendRequest friendRequest);

  boolean friendshipExists(FriendshipId friendshipId);

  void removeFriendship(FriendshipDto friendshipDto);

  FriendshipDto updateFriendship(FriendshipDto friendshipDto);

  List<FriendshipDto> findFriendshipsOf(UserDetailsDto user);

  List<FriendshipDto> findAllFriendshipsWithStatus(UserDetailsDto user, FriendshipStatus status);

  Integer getFriendsCount(Long userId);
}
