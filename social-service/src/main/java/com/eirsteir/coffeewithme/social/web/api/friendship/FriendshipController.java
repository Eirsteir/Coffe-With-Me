package com.eirsteir.coffeewithme.social.web.api.friendship;

import com.eirsteir.coffeewithme.commons.dto.UserDetails;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@Slf4j
@RestController
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}/friends")
    @ResponseBody
    Collection<UserDetails> getFriends(@PathVariable Long id) {
        UserDetails userDetails = modelMapper.map(userService.findUserById(id), UserDetails.class);
        List<UserDetails> friends = friendshipService.getFriends(userDetails);

        if (friends.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "User with email - " +  userDetails.getEmail() + " has no friends");

        return friends;
    }

    @PostMapping("/friends")
    @ResponseStatus(HttpStatus.CREATED)
    FriendshipDto addFriend(@RequestParam("to_friend") Long toFriend,
                            @AuthenticationPrincipal UserDetailsImpl principal) {

        User currentUser = userService.findUserById(principal.getId());
        if (currentUser.getId().equals(toFriend))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot send friend requests to yourself");

        return friendshipService.registerFriendship(FriendRequest.builder()
                                                            .requesterId(currentUser.getId())
                                                            .addresseeId(toFriend)
                                                           .build());
    }

    @PutMapping("/friends")
    FriendshipDto updateFriendship(@RequestBody @Valid FriendshipDto friendshipDto,
                                   @AuthenticationPrincipal UserDetailsImpl principal) {
        validateFriendshipDto(friendshipDto, principal);
        FriendshipDto updatedFriendshipDto = friendshipService.updateFriendship(friendshipDto);

//        if (updatedFriendshipDto.getStatus() == FriendshipStatus.ACCEPTED)
//            notificationService.notify(principal.getUser().getId(),
//                                       principal.getUser(),
//                                       NotificationType.FRIENDSHIP_ACCEPTED);

        return  updatedFriendshipDto;
    }

    @DeleteMapping("/friends")
    void deleteFriendship(@RequestBody @Valid FriendshipDto friendshipDto,
                          @AuthenticationPrincipal UserDetailsImpl principal) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/friends/requests")
    List<UserDetails> getFriendRequests(@AuthenticationPrincipal UserDetailsImpl principal) {
        UserDetails userDto = modelMapper.map(principal, UserDetails.class);
        List<UserDetails> friendRequests = friendshipService.getFriendsOfWithStatus(userDto,
                                                                                FriendshipStatus.REQUESTED);

        if (friendRequests.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "User with email - " +  userDto.getEmail() + " has no friend requests");


        return friendRequests;
    }

    private void validateFriendshipDto(FriendshipDto friendshipDto, UserDetailsImpl principal) {
        Long requesterId = friendshipDto.getRequesterId();
        Long addresseeId = friendshipDto.getAddresseeId();
        Long principalId = principal.getId();

        if (addresseeId.equals(principalId))
            return;
        else if (requesterId.equals(principalId))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot accept friend request sent by yourself");

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Friendship does not belong to current user");
    }
}
