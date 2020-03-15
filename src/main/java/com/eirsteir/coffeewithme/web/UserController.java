package com.eirsteir.coffeewithme.web;


import com.eirsteir.coffeewithme.models.User;
import com.eirsteir.coffeewithme.repository.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository repository;

    private final UserModelAssembler assembler;

    public UserController(UserRepository repository, UserModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping
    CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> users = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(users,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    EntityModel<User> one(@PathVariable Long id) {
        User user = repository.findById(id)
                .orElseThrow(IllegalArgumentException::new); // () -> new UserNotFoundException(id)

        return assembler.toModel(user);
    }

    @PostMapping
    ResponseEntity<EntityModel<User>> create(@RequestBody User user) {
        return null;
    }
}
