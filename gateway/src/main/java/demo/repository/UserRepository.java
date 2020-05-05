package demo.repository;

import demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);

  @Query("UPDATE User u SET u.lastLogin=:lastLogin WHERE u.email = ?#{ principal?.username }")
  @Modifying
  @Transactional
  void updateLastLogin(@Param("lastLogin") Date lastLogin);

}
