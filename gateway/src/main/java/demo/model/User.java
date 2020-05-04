package demo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  private String password;

  private Date lastLogin;

  private Boolean enabled = true;

  private Boolean accountExpired = false;

  private Boolean accountLocked = false;

  private Boolean credentialsExpired = false;

}
