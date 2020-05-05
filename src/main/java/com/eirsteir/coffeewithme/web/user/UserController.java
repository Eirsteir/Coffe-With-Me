package com.eirsteir.coffeewithme.web.user;


import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.service.UserService;
import com.eirsteir.coffeewithme.web.request.UpdateProfileRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Api(tags = {"Swagger Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Swagger Resource", description = "User management operations for this application")
})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    UserDto user() {
        return UserDto.builder().email("eirik").build();
    }

    @PutMapping
    UserDto updateUser(@RequestBody @Valid UpdateProfileRequest updateProfileRequest) {
        UserDto userDto = new UserDto()
                .setUsername(updateProfileRequest.getUsername())
                .setEmail(updateProfileRequest.getEmail());

        return userService.updateProfile(userDto);
    }
}
