package com.eirsteir.coffeewithme.domain.user;


import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateBaseModel;
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

    @NotBlank
    @Column(unique = true)
    private String username;

    @ToString.Exclude
    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters.")
    private String password;

    @ManyToMany
    private List<Role> roles;

    public void addRole(Role role) {
        roles.add(role);
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }
}
