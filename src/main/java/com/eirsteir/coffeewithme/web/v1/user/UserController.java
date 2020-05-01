package com.eirsteir.coffeewithme.web.v1.user;


import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.service.UserService;
import com.eirsteir.coffeewithme.web.v1.request.UpdateProfileRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    List<UserDto> allUsers() {
        return userService.findAll();
    }

    @PutMapping
    UserDto updateUser(@RequestBody @Valid UpdateProfileRequest updateProfileRequest) {
        UserDto userDto = new UserDto()
                .setUsername(updateProfileRequest.getUsername())
                .setEmail(updateProfileRequest.getEmail());

        return userService.updateProfile(userDto);
    }
}
