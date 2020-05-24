package com.eirsteir.coffeewithme.authservice.domain;


import lombok.Data;

import javax.persistence.*;
import java.util.Collection;


@Data
@Entity
public class UserCredentials {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cwmId;

    @Column(unique = true)
    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "cwm_id", referencedColumnName = "cwmId"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

}
