package com.eirsteir.coffeewithme.domain.user;


import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateBaseModel;
import com.eirsteir.coffeewithme.validation.constraints.ContactNumberConstraint;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends CreatedUpdatedDateBaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Email
    @Column(unique = true)
    @NotBlank(message = "Email address may not be blank")
    private String emailAddress;

    @ToString.Exclude
    @NotBlank
    private String confirmEmailAddress;

    @NotBlank
    @Column(unique = true)
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

    @NotBlank
    @ContactNumberConstraint(message = "Phone number must be between 8-14 integers.")
    private String phoneNumber;

    @ManyToMany
    private List<Role> roles;

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }
}
