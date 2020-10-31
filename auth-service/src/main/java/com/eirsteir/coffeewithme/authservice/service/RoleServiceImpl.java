package com.eirsteir.coffeewithme.authservice.service;

import com.eirsteir.coffeewithme.authservice.domain.Role;
import com.eirsteir.coffeewithme.authservice.domain.RoleType;
import com.eirsteir.coffeewithme.authservice.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

  @Autowired private RoleRepository repository;

  @Override
  public Role getOrCreateRole(RoleType type) {

    return repository
        .findByType(type)
        .orElseGet(
            () -> {
              Role savedRole = repository.save(new Role(type));
              log.info("[x] Role not found, created new: {}", savedRole);
              return savedRole;
            });
  }
}
