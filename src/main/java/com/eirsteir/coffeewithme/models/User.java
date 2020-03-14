package com.eirsteir.coffeewithme.models;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
    public class User extends CreatedUpdatedDateBaseModel {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    @ManyToMany
    private List<Role> roles;

    @Email
    @NotNull
    private String emailAddress;

    @NotNull
    private String username;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    public User() {
    }

}
