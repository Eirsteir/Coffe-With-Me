package com.eirsteir.coffeewithme.authservice.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@Entity
public class Role implements Serializable {

  private static final long serialVersionUID = -3444524593791786238L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true)
  private RoleType type;

  public Role(RoleType type) {
    this.type = type;
  }
}
