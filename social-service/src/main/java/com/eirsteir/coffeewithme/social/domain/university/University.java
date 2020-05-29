package com.eirsteir.coffeewithme.social.domain.university;


import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class University {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @OneToMany(fetch = FetchType.EAGER,
          mappedBy = "university",
          cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
          orphanRemoval=true)
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private List<Campus> campuses;

  public Campus addCampus(Campus campus) {
    if (campuses == null)
      campuses = new ArrayList<>();;

    campuses.add(campus);
    campus.setUniversity(this);
    return campus;
  }

}
