package com.eirsteir.coffeewithme.social.web.api.user

import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.eirsteir.coffeewithme.social.dto.UserProfile
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService
import com.eirsteir.coffeewithme.social.service.user.UserService
import com.eirsteir.coffeewithme.social.web.request.UpdateProfileRequest
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/me")
class ProfileController(
    private val userService: UserService,
    private val friendshipService: FriendshipService,
    private val modelMapper: ModelMapper
) {

    @GetMapping
    fun me(@AuthenticationPrincipal principal: UserDetailsImpl): UserProfile {
        val profile = modelMapper.map(userService.findUserById(principal.id), UserProfile::class.java)
        val friendsCount = friendshipService.getFriendsCount(profile.id)
        profile.friendsCount = friendsCount
        return profile
    }

    @PutMapping
    fun update(
        @RequestBody updateProfileRequest: @Valid UpdateProfileRequest,
        @AuthenticationPrincipal principal: UserDetailsImpl
    ): UserProfile {
        val profile: UserProfile = userService.updateProfile(updateProfileRequest, principal)
        val friendsCount: Int = friendshipService.getFriendsCount(profile.id)
        profile.friendsCount = friendsCount
        return profile
    }
}