package com.eirsteir.coffeewithme.repository;


import com.eirsteir.coffeewithme.domain.role.Role;
import com.eirsteir.coffeewithme.domain.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByType(RoleType type);

}
