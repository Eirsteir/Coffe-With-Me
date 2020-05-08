package com.eirsteir.coffeewithme.domain.user;


import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateTimeBaseModel;
import com.eirsteir.coffeewithme.domain.role.Role;
import lombok.*;
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
public class User extends CreatedUpdatedDateTimeBaseModel implements Serializable {

    private static final long serialVersionUID = 3966996285633364335L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;

    @ToString.Exclude
    private String password;

    private String name;

    private String mobileNumber;

    private UserType userType;

    private Date lastLogin;

    @ToString.Exclude
    @Builder.Default
    private Boolean enabled = true;

    @ToString.Exclude
    @Builder.Default
    private Boolean accountExpired = false;

    @ToString.Exclude
    @Builder.Default
    private Boolean accountLocked = false;

    @ToString.Exclude
    @Builder.Default
    private Boolean credentialsExpired = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

}
