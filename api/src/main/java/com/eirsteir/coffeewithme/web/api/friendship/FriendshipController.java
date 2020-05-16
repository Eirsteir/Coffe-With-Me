package com.eirsteir.coffeewithme.web.api.friendship;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.security.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.web.request.FriendRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@Api(tags = {"Friends"})
@SwaggerDefinition(tags = {
        @Tag(name = "Friends", description = "Friendship management operations for this application")
})
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}/friends")
    @ResponseBody
    @ApiOperation("Find friends of user with given id")
    Collection<UserDto> getFriends(@PathVariable Long id) {
        UserDto userDto = modelMapper.map(userService.findUserById(id), UserDto.class);
        List<UserDto> friends = friendshipService.getFriends(userDto);

        if (friends.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "User with email - " +  userDto.getEmail() + " has no friends");

        return friends;
    }

    @PostMapping("/friends")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Send a new friend request")
    FriendshipDto addFriend(@RequestParam("to_friend") Long toFriend,
                                @AuthenticationPrincipal UserPrincipalImpl principal) {

        if (principal.getUser().getId().equals(toFriend))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot send friend requests to yourself");

        return friendshipService.registerFriendship(FriendRequest.builder()
                                                            .requesterId(principal.getUser().getId())
                                                            .addresseeId(toFriend)
                                                            .build());
    }

    @PutMapping("/friends")
    @ApiOperation("Update friendship")
    FriendshipDto updateFriendship(@RequestBody @Valid FriendshipDto friendshipDto,
                                   @AuthenticationPrincipal UserPrincipalImpl principal) {
        validateFriendshipDto(friendshipDto, principal);

        return friendshipService.updateFriendship(friendshipDto);
    }

    @DeleteMapping("/friends")
    @ApiOperation("Delete friendship")
    void deleteFriendship(@RequestBody @Valid FriendshipDto friendshipDto,
                                   @AuthenticationPrincipal UserPrincipalImpl principal) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/friends/requests")
    @ApiOperation("Get all friend requests of the currently logged in user")
    List<UserDto> getFriendRequests(@AuthenticationPrincipal UserPrincipalImpl principal) {
        UserDto userDto = modelMapper.map(principal.getUser(), UserDto.class);
        List<UserDto> friendRequests = friendshipService.getFriendshipsWithStatus(userDto,
                                                                                  FriendshipStatus.REQUESTED);

        if (friendRequests.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "User with email - " +  userDto.getEmail() + " has no friend requests");


        return friendRequests;
    }

    private void validateFriendshipDto(FriendshipDto friendshipDto, UserPrincipalImpl principal) {
        Long requesterId = friendshipDto.getId().getRequester().getId();
        Long addresseeId = friendshipDto.getId().getAddressee().getId();
        Long principalId = principal.getUser().getId();

        if (addresseeId.equals(principalId))
            return;
        else if (requesterId.equals(principalId))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot accept friend request sent by yourself");

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Friendship does not belong to current user");
    }
}
