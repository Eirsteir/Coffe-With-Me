package com.eirsteir.coffeewithme.web.api.friendship;

import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.service.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.web.request.FriendRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/user/friends")
@Api(tags = {"Swagger Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Swagger Resource", description = "Friendship management operations for this application")
})
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    Collection<UserDto> allFriendships(Authentication authentication) {
        UserPrincipalImpl principal = (UserPrincipalImpl) authentication.getPrincipal();

        UserDto userDto = modelMapper.map(principal.getUser(), UserDto.class);
        return friendshipService.findFriendsOf(userDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    FriendshipDto addFriendship(@RequestParam("to_friend") Long toFriend, Authentication authentication) {
        UserPrincipalImpl principal = (UserPrincipalImpl) authentication.getPrincipal();

        if (principal.getUser().getId().equals(toFriend))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot send friend requests to yourself");

        return friendshipService.registerFriendship(FriendRequest.builder()
                                                            .requesterId(principal.getUser().getId())
                                                            .addresseeId(toFriend)
                                                            .build());
    }

    @PutMapping
    FriendshipDto acceptFriendship(@RequestBody @Valid FriendshipDto friendshipDto, Authentication authentication) {
        validateFriendshipRequest(friendshipDto, authentication);

        return friendshipService.acceptFriendship(friendshipDto);
    }

    private void validateFriendshipRequest(FriendshipDto friendshipDto, Authentication authentication) {
        UserPrincipalImpl principal = (UserPrincipalImpl) authentication.getPrincipal();

        Long requesterId = friendshipDto.getId().getRequester().getId();
        Long addresseeId = friendshipDto.getId().getAddressee().getId();
        Long principalId = principal.getUser().getId();

        if (requesterId.equals(principalId) || addresseeId.equals(principalId))
            return;

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Friendship does not belong to current user");
    }
}
