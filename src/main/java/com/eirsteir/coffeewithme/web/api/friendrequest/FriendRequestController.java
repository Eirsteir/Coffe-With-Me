package com.eirsteir.coffeewithme.web.api.friendrequest;

import com.eirsteir.coffeewithme.service.CWMUserPrincipal;
import com.eirsteir.coffeewithme.service.FriendRequestService;
import com.eirsteir.coffeewithme.service.FriendshipService;
import com.eirsteir.coffeewithme.web.dto.FriendRequestDto;
import com.eirsteir.coffeewithme.web.dto.FriendshipDto;
import com.eirsteir.coffeewithme.web.request.FriendRequestInquiry;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.modelmapper.ModelMapper;
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
    private FriendRequestService friendRequestService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private ModelMapper modelMapper;

    // TODO: 06.05.2020 does this belong in user?
    @PostMapping("/add_friend")
    FriendRequestDto addFriend(@RequestParam("to_friend") Long recipient, Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();

        FriendRequestDto friendRequestDto = FriendRequestDto.builder()
                .from(principal.getUser().getId())
                .to(recipient)
                .build();

        return friendRequestService.addFriendRequest(friendRequestDto);
    }

    @PostMapping("/accept_friend_request")
    FriendshipDto acceptFriendRequest(FriendRequestInquiry inquiry, Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();
        FriendRequestDto acceptedFriendRequestDto = friendRequestService
                .acceptFriendRequest(modelMapper.map(inquiry, FriendRequestDto.class));

        return friendshipService.addFriendship(acceptedFriendRequestDto);
    }

    @PostMapping("/reject_friend_request")
    FriendRequestDto rejectFriendRequest(FriendRequestInquiry inquiry, Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();
        return friendRequestService.rejectFriendRequest(modelMapper.map(inquiry, FriendRequestDto.class));
    }

    @PostMapping("/cancel_friend_request")
    FriendRequestDto cancelFriendRequest(FriendRequestInquiry inquiry, Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();
        return friendRequestService.cancelFriendRequest(modelMapper.map(inquiry, FriendRequestDto.class));
    }

}
