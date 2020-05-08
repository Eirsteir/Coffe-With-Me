package com.eirsteir.coffeewithme.web.api.friendship;

import com.eirsteir.coffeewithme.service.CWMUserPrincipal;
import com.eirsteir.coffeewithme.service.FriendshipService;
import com.eirsteir.coffeewithme.web.dto.FriendshipDto;
import com.eirsteir.coffeewithme.web.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.FriendshipRequest;
import com.eirsteir.coffeewithme.web.request.Request;
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
@RequestMapping("/api/user/friends")
@Api(tags = {"Swagger Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Swagger Resource", description = "User registration management operations for this application")
})
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    Collection<UserDto> allFriendships(Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();

        UserDto userDto = modelMapper.map(principal.getUser(), UserDto.class);
        return friendshipService.findFriendsOf(userDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    FriendshipDto addFriendship(@Valid FriendshipRequest friendshipRequest, Authentication authentication) {
        validateFriendshipRequest(friendshipRequest, authentication);

        return friendshipService.registerFriendship(friendshipRequest);
    }

    @PutMapping
    FriendshipDto acceptFriendship(@Valid FriendshipDto friendshipDto, Authentication authentication) {
        validateFriendshipRequest(friendshipDto, authentication);

        return friendshipService.acceptFriendship(friendshipDto);
    }

    private void validateFriendshipRequest(@Valid Request request, Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();

        if (request.getRequesterId().equals(principal.getUser().getId()))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot send friend requests from a different user");
    }
}
