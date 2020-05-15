package com.eirsteir.coffeewithme.web.api.user;


import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.repository.rsql.RqslVisitorImpl;
import com.eirsteir.coffeewithme.security.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.web.request.UpdateProfileRequest;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Api(tags = {"Users"})
@SwaggerDefinition(tags = {
        @Tag(name = "Users", description = "User management operations for this application")
})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/user")
    @ResponseBody
    UserDto user(Authentication authentication) {
        UserPrincipalImpl principal = (UserPrincipalImpl) authentication.getPrincipal();
        return modelMapper.map(principal.getUser(), UserDto.class);
    }

    @PutMapping("/user/update")
    UserDto updateUser(@RequestBody @Valid UpdateProfileRequest updateProfileRequest, Authentication authentication) {
        UserPrincipalImpl principal = (UserPrincipalImpl) authentication.getPrincipal();

        UserDto userDto = UserDto.builder()
                .email(principal.getEmail())
                .username(updateProfileRequest.getUsername())
                .build();

        return userService.updateProfile(userDto);
    }

    @GetMapping("/users")
    List<User> search(@RequestParam String search) {
        Node rootNode = new RSQLParser().parse(search);
        Specification<User> spec = rootNode.accept(new RqslVisitorImpl<>());
        return userRepository.findAll(spec);
    }
}
