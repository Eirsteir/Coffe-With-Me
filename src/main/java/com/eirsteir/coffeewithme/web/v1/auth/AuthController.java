package com.eirsteir.coffeewithme.web.v1.auth;

import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.service.UserService;
import com.eirsteir.coffeewithme.web.v1.request.SignUpRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

//    @GetMapping("/login")
//    public String loadLoginPage(){
//        return "login";
//    }

    @PostMapping("/signup")
    public String signUp(@RequestBody @Valid SignUpRequest signUpRequest){
        return userService.signUp(modelMapper.map(signUpRequest, UserDto.class));
    }

}