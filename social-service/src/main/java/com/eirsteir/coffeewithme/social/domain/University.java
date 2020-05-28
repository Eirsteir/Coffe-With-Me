package com.eirsteir.coffeewithme.social.domain;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class University {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "university")
  private List<Campus> campuses;

}
