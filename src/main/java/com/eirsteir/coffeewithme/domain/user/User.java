package com.eirsteir.coffeewithme.domain.user;


import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateBaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends CreatedUpdatedDateBaseModel  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;

    private String password;

    private String name;

    private String mobileNumber;

    private UserType userType;

    private Date lastLogin;

    @Builder.Default
    private Boolean enabled = true;

    @Builder.Default
    private Boolean accountExpired = false;

    @Builder.Default
    private Boolean accountLocked = false;

    @Builder.Default
    private Boolean credentialsExpired = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

}
