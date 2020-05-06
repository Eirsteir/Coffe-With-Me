package com.eirsteir.coffeewithme.web.controller.user;


import com.eirsteir.coffeewithme.web.dto.UserDto;
import com.eirsteir.coffeewithme.service.CMEUserPrincipal;
import com.eirsteir.coffeewithme.service.UserService;
import com.eirsteir.coffeewithme.web.request.UpdateProfileRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/user")
@Api(tags = {"Swagger Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Swagger Resource", description = "User management operations for this application")
})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    UserDto user(Authentication authentication) {
        CMEUserPrincipal principal = (CMEUserPrincipal) authentication.getPrincipal();
        log.info("[x] Principal: {}", principal);
        return modelMapper.map(principal.getUser(), UserDto.class);
    }

    @PutMapping("/update")
    UserDto updateUser(@RequestBody @Valid UpdateProfileRequest updateProfileRequest) {
        UserDto userDto = new UserDto()
                .setUsername(updateProfileRequest.getUsername())
                .setEmail(updateProfileRequest.getEmail());

        return userService.updateProfile(userDto);
    }
}
