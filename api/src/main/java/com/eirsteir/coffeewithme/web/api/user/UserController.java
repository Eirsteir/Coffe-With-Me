package com.eirsteir.coffeewithme.web.api.user;


import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.repository.UserSpecificationsBuilder;
import com.eirsteir.coffeewithme.security.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.web.request.UpdateProfileRequest;
import com.eirsteir.coffeewithme.web.util.SearchOperation;
import com.google.common.base.Joiner;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Specification<User> spec = resolveSpecification(search);

        return userRepository.findAll(spec);
    }

    protected Specification<User> resolveSpecification(String searchParameters) {
        UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile(
                "(\\p{Punct}?)(\\w+?)("
                        + operationSetExper
                        + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(searchParameters + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3),
                         matcher.group(5), matcher.group(4), matcher.group(6));
        }

        return builder.build();
    }
}
