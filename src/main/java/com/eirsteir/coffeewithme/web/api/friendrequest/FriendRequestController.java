package com.eirsteir.coffeewithme.web.api.friendrequest;

import com.eirsteir.coffeewithme.service.CMEUserPrincipal;
import com.eirsteir.coffeewithme.service.FriendRequestService;
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
@RequestMapping("/api")
@Api(tags = {"Swagger Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Swagger Resource", description = "User registration management operations for this application")
})
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/add_friend")
    void addFriend(@RequestParam("to_friend") Long toFriend, Authentication authentication) {
        CMEUserPrincipal principal = (CMEUserPrincipal) authentication.getPrincipal();
        friendRequestService.createFriendRequestFrom(principal.getEmail(), toFriend);
    }
}
