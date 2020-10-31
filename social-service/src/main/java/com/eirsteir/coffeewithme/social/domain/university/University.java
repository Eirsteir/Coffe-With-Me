package com.eirsteir.coffeewithme.social.domain.university;

import com.eirsteir.coffeewithme.social.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Cascade;

@Data
@Entity
public class University {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "university")
  @ToString.Exclude
  @JsonIgnore
  private List<User> attendees;

  @OneToMany(
      fetch = FetchType.EAGER,
      mappedBy = "university",
      cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
      orphanRemoval = true)
  @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
  private List<Campus> campuses;

  public Campus addCampus(Campus campus) {
    if (campuses == null) campuses = new ArrayList<>();
    ;

    campuses.add(campus);
    campus.setUniversity(this);
    return campus;
  }
}
