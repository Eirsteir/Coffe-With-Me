package com.eirsteir.coffeewithme.domain.user;


import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateBaseModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@Entity
public class User extends CreatedUpdatedDateBaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email
    @Column(unique = true)
    @NotBlank(message = "Email address may not be blank")
    private String emailAddress;

    @Column(unique = true)
    @NotBlank
    private String username;

    @NotBlank(message = "First name may not be blank")
    private String firstName;

    @NotBlank(message = "First name may not be blank")
    private String lastName;

    @ToString.Exclude
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters.")
    private String password;

    @ToString.Exclude
    @NotBlank
    private String confirmPassword;

    @ManyToMany
    private List<Role> roles;

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }
}
