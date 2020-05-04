package demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true)
  private String role;
}
