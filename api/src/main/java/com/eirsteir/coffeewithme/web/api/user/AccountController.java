package com.eirsteir.coffeewithme.web.api.user;

import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.security.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.web.request.UpdateProfileRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;



@Slf4j
@RestController
@RequestMapping("/me")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    UserDto account(Authentication authentication) {
        UserPrincipalImpl principal = (UserPrincipalImpl) authentication.getPrincipal();
        return modelMapper.map(principal.getUser(), UserDto.class);
    }

    @PutMapping("/update")
    @ResponseBody
    UserDto updateUser(@RequestBody @Valid UpdateProfileRequest updateProfileRequest, Authentication authentication) {
        UserPrincipalImpl principal = (UserPrincipalImpl) authentication.getPrincipal();

        UserDto userDto = UserDto.builder()
                .email(principal.getEmail())
                .username(updateProfileRequest.getUsername())
                .build();

        return userService.updateProfile(userDto);
    }
}
