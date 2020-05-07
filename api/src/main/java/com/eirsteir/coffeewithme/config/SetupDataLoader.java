package com.eirsteir.coffeewithme.config;


import com.eirsteir.coffeewithme.domain.role.Role;
import com.eirsteir.coffeewithme.domain.role.RoleType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.domain.user.UserType;
import com.eirsteir.coffeewithme.repository.RoleRepository;
import com.eirsteir.coffeewithme.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

@Component
@Slf4j
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private boolean alreadySetup = false;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (alreadySetup) {
      return;
    }

    createRoleIfNotFound(RoleType.ROLE_ADMIN);
    createRoleIfNotFound(RoleType.ROLE_USER);
    createRoleIfNotFound(RoleType.ROLE_READER);
    createRoleIfNotFound(RoleType.ROLE_WRITER);

    Role adminRole = roleRepository.findByType(RoleType.ROLE_ADMIN);
    Role basicRole = roleRepository.findByType(RoleType.ROLE_USER);
    Role readerRole = roleRepository.findByType(RoleType.ROLE_READER);
    Role writerRole = roleRepository.findByType(RoleType.ROLE_WRITER);

    User user = User.builder()
      .name("Admin")
      .email("admin@test.com")
      .password(passwordEncoder.encode("admin"))
      .userType(UserType.LOCAL)
      .roles(Arrays.asList(adminRole, basicRole, readerRole, writerRole))
      .build();

    log.info("[x] Preloading " + userRepository.save(user));

    User auditUser = User.builder()
      .name("Audit")
      .email("audit@test.com")
      .password(passwordEncoder.encode("audit"))
      .userType(UserType.LOCAL)
      .roles(Arrays.asList(adminRole, basicRole, readerRole))
      .build();
    log.info("[x] Preloading " + userRepository.save(auditUser));

    User basicUser = User.builder()
      .name("User")
      .email("user@test.com")
      .password(passwordEncoder.encode("password"))
      .userType(UserType.LOCAL)
      .roles(Collections.singletonList(basicRole))
      .build();
    log.info("[x] Preloading " + userRepository.save(basicUser));

    alreadySetup = true;
  }

  @Transactional
  void createRoleIfNotFound(RoleType type) {
    Role role = roleRepository.findByType(type);
    if (role == null) {
      role = Role.builder()
        .type(type)
        .build();
      log.info("[x] Preloading " + roleRepository.save(role));
    }
  }

}
