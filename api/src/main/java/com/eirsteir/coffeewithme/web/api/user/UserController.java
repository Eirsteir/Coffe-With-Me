package com.eirsteir.coffeewithme.web.api.user;


import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.repository.rsql.RqslVisitorImpl;
import com.eirsteir.coffeewithme.service.user.UserService;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@RestController("/users")
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

    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation("Get user details with given id")
    UserDto user(@RequestParam Long id) {
        User user = userService.findUserById(id);
        return modelMapper.map(user, UserDto.class);
    }

    @GetMapping
    @ApiOperation("Search for users")
    List<UserDto> search(@RequestParam String search) { // TODO: 15.05.2020 lowercase all or some queries?
        Node rootNode = new RSQLParser().parse(search);
        Specification<User> spec = rootNode.accept(new RqslVisitorImpl<>());
        List<UserDto> results = userService.searchUsers(spec);

        if (results.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "Search query '" + search + "' yielded no results");

        return results;
    }
}
