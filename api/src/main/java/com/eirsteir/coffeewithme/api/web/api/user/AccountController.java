package com.eirsteir.coffeewithme.api.web.api.user;

import com.eirsteir.coffeewithme.api.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


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
    Principal account(Principal principal) {
//        UserPrincipalImpl principal =  authentication.getPrincipal();
        return principal;
    }

//    @PutMapping("/update")
//    @ResponseBody
//    UserDto updateUser(@RequestBody @Valid UpdateProfileRequest updateProfileRequest, Authentication authentication) {
//        UserPrincipalImpl principal = (UserPrincipalImpl) authentication.getPrincipal();
//
//        UserDto userDto = UserDto.builder()
//                .email(principal.getEmail())
//                .username(updateProfileRequest.getUsername())
//                .build();
//
//        return userService.updateProfile(userDto);
//    }
}
