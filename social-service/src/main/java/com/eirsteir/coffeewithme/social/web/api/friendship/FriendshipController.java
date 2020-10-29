package com.eirsteir.coffeewithme.social.web.api.friendship;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@Slf4j
@RestController
@AllArgsConstructor
public class FriendshipController {

    private FriendshipService friendshipService;

    private UserService userService;

    private ModelMapper modelMapper;

    @GetMapping("/friends")
    @ResponseBody
    Collection<FriendshipDto> getFriends(@AuthenticationPrincipal UserDetailsImpl principal) {
        UserDetailsDto UserDetailsDto = modelMapper.map(userService.findUserById(principal.getId()), UserDetailsDto.class);
        List<FriendshipDto> friendships = friendshipService.findFriendshipsOf(UserDetailsDto);

        if (friendships.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "User with email - " +  UserDetailsDto.getEmail() + " has no friends");

        return friendships;
    }

    @PostMapping("/friends")
    @ResponseStatus(HttpStatus.CREATED)
    FriendshipDto addFriend(@RequestParam("to_friend") Long toFriend,
                            @AuthenticationPrincipal UserDetailsImpl principal) {
        log.debug("[x] Request to /friends?to_friend={}", toFriend);

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
        return friendshipService.updateFriendship(friendshipDto);
    }

    @DeleteMapping("/friends")
    void deleteFriendship(@RequestBody @Valid FriendshipDto friendshipDto,
                          @AuthenticationPrincipal UserDetailsImpl principal) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/friends/requests")
    List<FriendshipDto> getFriendRequests(@AuthenticationPrincipal UserDetailsImpl principal) {
        UserDetailsDto userDto = modelMapper.map(principal, UserDetailsDto.class);
        List<FriendshipDto> friendRequests = friendshipService.findFriendshipsOf(userDto.getId(),
                                                                                 FriendshipStatus.REQUESTED);

        if (friendRequests.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "User with email - " +  userDto.getEmail() + " has no friend requests");


        return friendRequests;
    }

    private void validateFriendshipDto(FriendshipDto friendshipDto, UserDetailsImpl principal) {
        Long requesterId = friendshipDto.getRequester().getId();
        Long addresseeId = friendshipDto.getAddressee().getId();
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
