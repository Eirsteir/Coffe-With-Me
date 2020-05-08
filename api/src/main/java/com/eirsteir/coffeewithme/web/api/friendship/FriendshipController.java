package com.eirsteir.coffeewithme.web.api.friendship;

import com.eirsteir.coffeewithme.service.CWMUserPrincipal;
import com.eirsteir.coffeewithme.service.UserService;
import com.eirsteir.coffeewithme.web.dto.UserDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/user/friends")
@Api(tags = {"Swagger Resource"})
@SwaggerDefinition(tags = {
        @Tag(name = "Swagger Resource", description = "User registration management operations for this application")
})
public class FriendshipController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    Collection<UserDto> allFriends(Authentication authentication) {
        CWMUserPrincipal principal = (CWMUserPrincipal) authentication.getPrincipal();

        UserDto userDto = modelMapper.map(principal.getUser(), UserDto.class);
        return userService.findAllFriends(userDto);
    }



}
