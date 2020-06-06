package com.eirsteir.coffeewithme.social.web.api.user;

import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.dto.UserProfile;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.UpdateProfileRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/me")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    UserProfile me(@AuthenticationPrincipal UserDetailsImpl principal) {
        UserProfile profile = modelMapper.map(userService.findUserById(principal.getId()), UserProfile.class);
        Integer friendsCount = friendshipService.getFriendsCount(profile.getId());
        profile.setFriendsCount(friendsCount);

        return profile;
    }

    @PutMapping
    UserProfile update(@Valid @RequestBody UpdateProfileRequest updateProfileRequest,
                       @AuthenticationPrincipal UserDetailsImpl principal) {
        UserProfile profile = userService.updateProfile(updateProfileRequest, principal);
        Integer friendsCount = friendshipService.getFriendsCount(profile.getId());
        profile.setFriendsCount(friendsCount);

        return profile;
    }
}
