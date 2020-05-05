package com.eirsteir.coffeewithme.web.auth;

import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.service.UserService;
import com.eirsteir.coffeewithme.web.request.SignUpRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/signup")
    public UserDto signUp(@RequestBody @Valid SignUpRequest signUpRequest){
        return userService.signUp(modelMapper.map(signUpRequest, UserDto.class));
    }

}