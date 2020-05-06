package com.eirsteir.coffeewithme.web.api.friendrequest;

import com.eirsteir.coffeewithme.service.CMEUserPrincipal;
import com.eirsteir.coffeewithme.service.FriendshipService;
import com.eirsteir.coffeewithme.web.dto.FriendRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friends")
@Api(tags = {"Swagger Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Swagger Resource", description = "User registration management operations for this application")
})
public class FriendRequestController {

    @Autowired
    private FriendshipService friendshipService;

    // TODO: 06.05.2020 does this belong in user?
    @PostMapping("/add_friend")
    FriendRequestDto addFriend(@RequestParam("to_user") String recipient, Authentication authentication) {
        CMEUserPrincipal principal = (CMEUserPrincipal) authentication.getPrincipal();
        FriendRequestDto friendRequestDto = FriendRequestDto.builder()
                .from(principal.getEmail())
                .to(recipient)
                .build();

        return friendshipService.addFriendRequest(friendRequestDto);
    }

    @PostMapping("/accept_friend_request")
    FriendRequestDto acceptFriendRequest(FriendRequestDto friendRequestDto, Authentication authentication) {
        CMEUserPrincipal principal = (CMEUserPrincipal) authentication.getPrincipal();
        return friendshipService.acceptFriendRequest(friendRequestDto);
    }

    @PostMapping("/reject_friend_request")
    FriendRequestDto rejectFriendRequest(FriendRequestDto friendRequestDto, Authentication authentication) {
        CMEUserPrincipal principal = (CMEUserPrincipal) authentication.getPrincipal();
        return friendshipService.rejectFriendRequest(friendRequestDto);
    }

    @PostMapping("/cancel_friend_request")
    FriendRequestDto cancelFriendRequest(FriendRequestDto friendRequestDto, Authentication authentication) {
        CMEUserPrincipal principal = (CMEUserPrincipal) authentication.getPrincipal();
        return friendshipService.cancelFriendRequest(friendRequestDto);
    }

}
