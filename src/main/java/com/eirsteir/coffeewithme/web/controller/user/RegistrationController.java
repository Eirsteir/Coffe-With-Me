package com.eirsteir.coffeewithme.web.controller.user;

import com.eirsteir.coffeewithme.service.UserService;
import com.eirsteir.coffeewithme.web.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.UserRegistrationRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
@Api(tags = {"Swagger Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Swagger Resource", description = "User registration management operations for this application")
})
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest){
        return userService.registerUser(userRegistrationRequest);
    }

}