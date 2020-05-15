package com.eirsteir.coffeewithme.domain.role;

import com.eirsteir.coffeewithme.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements Serializable {

  private static final long serialVersionUID = -3444524593791786238L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true)
  private RoleType type;

  @JsonIgnore
  @ToString.Exclude
  @ManyToMany(mappedBy = "roles")
  private Collection<User> users;

}
