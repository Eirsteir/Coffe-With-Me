package com.eirsteir.coffeewithme.social.web.api.user;


import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.UserProfile;
import com.eirsteir.coffeewithme.social.repository.rsql.RqslVisitorImpl;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.web.request.UpdateProfileRequest;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @ResponseBody
    UserDetailsDto user(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl principal) {

        return userService.findUserByIdWithIsFriend(id, principal.getId());
    }

    @PutMapping
    UserProfile update(@Valid @RequestBody UpdateProfileRequest updateProfileRequest,
                       @AuthenticationPrincipal UserDetailsImpl principal) {
        return userService.updateProfile(updateProfileRequest, principal);
    }

    @GetMapping
    List<UserDetailsDto> search(@RequestParam String search) {
        Node rootNode = new RSQLParser().parse(search);
        Specification<User> spec = rootNode.accept(new RqslVisitorImpl<>());
        List<UserDetailsDto> results = userService.searchUsers(spec);

        if (results.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "Search query '" + search + "' yielded no results");

        return results;
    }
}
