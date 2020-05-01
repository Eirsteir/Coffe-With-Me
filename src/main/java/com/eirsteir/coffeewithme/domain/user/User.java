package com.eirsteir.coffeewithme.domain.user;


import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateBaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;


@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User extends CreatedUpdatedDateBaseModel {

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

}
