package com.eirsteir.coffeewithme.authservice.config;


import com.eirsteir.coffeewithme.authservice.domain.Role;
import com.eirsteir.coffeewithme.authservice.domain.RoleType;
import com.eirsteir.coffeewithme.authservice.domain.UserCredentials;
import com.eirsteir.coffeewithme.authservice.repository.RoleRepository;
import com.eirsteir.coffeewithme.authservice.repository.UserCredentialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private boolean alreadySetup = false;

  @Autowired
  private UserCredentialsRepository userCredentialsRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private BCryptPasswordEncoder encoder;

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

    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setEmail("admin@test.com");
    userCredentials.setPassword(encoder.encode("admin"));

    log.info("[x] Preloading " + userCredentialsRepository.save(userCredentials));

    userCredentials = new UserCredentials();
    userCredentials.setEmail("user@test.com");
    userCredentials.setPassword(encoder.encode("password"));

    log.info("[x] Preloading " + userCredentialsRepository.save(userCredentials));

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
