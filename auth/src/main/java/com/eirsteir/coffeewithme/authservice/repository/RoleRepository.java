package com.eirsteir.coffeewithme.authservice.repository;


import com.eirsteir.coffeewithme.authservice.domain.Role;
import com.eirsteir.coffeewithme.authservice.domain.RoleType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

  Optional<Role> findByType(RoleType type);

}
