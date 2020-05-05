package com.eirsteir.coffeewithme.config;


import com.eirsteir.coffeewithme.domain.user.Role;
import com.eirsteir.coffeewithme.domain.user.User;
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

    createRoleIfNotFound("ROLE_ADMIN");
    createRoleIfNotFound("ROLE_USER");
    createRoleIfNotFound("ROLE_READER");
    createRoleIfNotFound("ROLE_WRITER");

    Role adminRole = roleRepository.findByName("ROLE_ADMIN");
    Role basicRole = roleRepository.findByName("ROLE_USER");
    Role readerRole = roleRepository.findByName("ROLE_READER");
    Role writerRole = roleRepository.findByName("ROLE_WRITER");

    User user = User.builder()
      .name("Admin")
      .email("admin@test.com")
      .password(passwordEncoder.encode("admin"))
      .roles(Arrays.asList(adminRole, basicRole, readerRole, writerRole))
      .build();

    log.info("Preloading " + userRepository.save(user));

    User auditUser = User.builder()
      .name("Audit")
      .email("audit@test.com")
      .password(passwordEncoder.encode("audit"))
      .roles(Arrays.asList(adminRole, basicRole, readerRole))
      .build();
    log.info("Preloading " + userRepository.save(auditUser));

    User basicUser = User.builder()
      .name("User")
      .email("user@test.com")
      .password(passwordEncoder.encode("password"))
      .roles(Collections.singletonList(basicRole))
      .build();
    log.info("Preloading " + userRepository.save(basicUser));

    alreadySetup = true;
  }

  @Transactional
  void createRoleIfNotFound(String name) {
    Role role = roleRepository.findByName(name);
    if (role == null) {
      role = Role.builder()
        .name(name)
        .build();
      roleRepository.save(role);
    }
  }

}
