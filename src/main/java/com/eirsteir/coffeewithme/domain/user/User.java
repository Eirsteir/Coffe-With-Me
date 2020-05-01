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

    @Column
    private String username;

    @Column
    private String name;

    @Column
    private String mobileNumber;

    @Column
    private UserType userType;

}
