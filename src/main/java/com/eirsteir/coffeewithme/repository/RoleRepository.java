package com.eirsteir.coffeewithme.repository;


import com.eirsteir.coffeewithme.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByName(String name);

}
