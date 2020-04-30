package com.eirsteir.coffeewithme.web;


import com.eirsteir.coffeewithme.domain.user.NewUserForm;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

//    @GetMapping
//    List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping(value = "/{id}")
//    @ResponseBody
//    User getUserById(@PathVariable Long id) {
//        return userService.findById(id)
//                .orElseThrow(IllegalArgumentException::new);
//    }


    @GetMapping
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    User createUser(@RequestBody @Valid NewUserForm newUserForm) {
        return userService.saveUser(newUserForm);
    }

    @PutMapping
    User updateUser(@RequestBody @Valid User user) {
        return userService.update(user);
    }
}
