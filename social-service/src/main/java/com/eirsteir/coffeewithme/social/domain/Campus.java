package com.eirsteir.coffeewithme.social.domain;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Campus {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @ManyToOne
  private University university;

}
