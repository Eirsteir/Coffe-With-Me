package com.eirsteir.coffeewithme.web;


import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    List<User> getAllUsers() {

        return userService.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    User getUserById(@PathVariable Long id) {

        return userService.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    User createUser(@RequestBody @Valid User user) {

        return userService.saveUser(user);
    }

    @PutMapping
    User updateUser(@RequestBody @Valid User user) {

        return userService.update(user);
    }
}
