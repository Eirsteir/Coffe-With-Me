package com.eirsteir.coffeewithme.web.v1;


import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.service.UserService;
import com.eirsteir.coffeewithme.web.v1.request.UpdateProfileRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/users")
@Api(tags = {"Swagger Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Swagger Resource", description = "User management operations for this application")
})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    UserDto login(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println(principal);
        UserDto userDto = UserDto.builder()
                .email(principal.getAttribute("email"))
                .name(principal.getAttribute("name"))
                .build();

        return userService.loginOrSignUp(userDto);
    }

    @PutMapping
    UserDto updateUser(@RequestBody @Valid UpdateProfileRequest updateProfileRequest) {
        UserDto userDto = new UserDto()
                .setUsername(updateProfileRequest.getUsername())
                .setEmail(updateProfileRequest.getEmail());

        return userService.updateProfile(userDto);
    }
}
