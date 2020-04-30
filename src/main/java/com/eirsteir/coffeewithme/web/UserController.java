package com.eirsteir.coffeewithme.web;


import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<User> createUser(@RequestBody User user) {

        return new ResponseEntity<User>(userService.saveUser(user), HttpStatus.CREATED);
    }
}
