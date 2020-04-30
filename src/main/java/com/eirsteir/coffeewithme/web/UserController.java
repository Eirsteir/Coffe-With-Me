package com.eirsteir.coffeewithme.web;


import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    List<User> all() {

        return null;
    }

    @GetMapping("/{id}")
    ResponseEntity<User> one(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(IllegalArgumentException::new); // () -> new UserNotFoundException(id)

        return null;
    }

    @PostMapping
    ResponseEntity<User> create(@RequestBody User user) {
        return null;
    }
}
