package com.eirsteir.coffeewithme.web.api.user;

import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.security.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.web.request.UpdateProfileRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/me")
@Api(tags = {"Account"})
@SwaggerDefinition(tags = {
        @Tag(name = "Account", description = "Account management operations for this application")
})
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    @ApiOperation("Get the currently logged in users details")
    UserDto account(Authentication authentication) {
        UserPrincipalImpl principal = (UserPrincipalImpl) authentication.getPrincipal();
        return modelMapper.map(principal.getUser(), UserDto.class);
    }

    @PutMapping("/update")
    @ResponseBody
    @ApiOperation("Update the currently logged in user")
    UserDto updateUser(@RequestBody @Valid UpdateProfileRequest updateProfileRequest, Authentication authentication) {
        UserPrincipalImpl principal = (UserPrincipalImpl) authentication.getPrincipal();

        UserDto userDto = UserDto.builder()
                .email(principal.getEmail())
                .username(updateProfileRequest.getUsername())
                .build();

        return userService.updateProfile(userDto);
    }
}
