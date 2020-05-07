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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/friends/requests")
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
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    FriendRequestDto addFriend(@RequestParam("to_friend") Long recipient, Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();

        FriendRequestDto friendRequestDto = FriendRequestDto.builder()
                .from(principal.getUser().getId())
                .to(recipient)
                .build();

        return friendRequestService.addFriendRequest(friendRequestDto);
    }

    // TODO: 06.05.2020 consider using only id
    @PostMapping("/accept")
    FriendshipDto acceptFriendRequest(@RequestBody FriendRequestInquiry inquiry, Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();
        FriendRequestDto acceptedFriendRequestDto = friendRequestService
                .acceptFriendRequest(modelMapper.map(inquiry, FriendRequestDto.class));

        return friendshipService.addFriendship(acceptedFriendRequestDto);
    }

    @PostMapping("/reject")
    FriendRequestDto rejectFriendRequest(@RequestBody FriendRequestInquiry inquiry, Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();
        return friendRequestService.rejectFriendRequest(modelMapper.map(inquiry, FriendRequestDto.class));
    }

    @PostMapping("/cancel")
    FriendRequestDto cancelFriendRequest(@RequestBody FriendRequestInquiry inquiry, Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();

        System.out.println(principal.getUser());
        System.out.println(inquiry);
        if (principal.getUser().getId().equals(inquiry.getFrom()))
            return friendRequestService.cancelFriendRequest(modelMapper.map(inquiry, FriendRequestDto.class));

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Friend request does not belong to this user");
    }

}
