package com.eirsteir.coffeewithme.authservice.config;


import com.eirsteir.coffeewithme.authservice.domain.Account;
import com.eirsteir.coffeewithme.authservice.domain.Role;
import com.eirsteir.coffeewithme.authservice.domain.RoleType;
import com.eirsteir.coffeewithme.authservice.repository.AccountRepository;
import com.eirsteir.coffeewithme.authservice.repository.RoleRepository;
import com.eirsteir.coffeewithme.authservice.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private boolean alreadySetup = false;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private RoleService roleService;

  @Autowired
  private BCryptPasswordEncoder encoder;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (alreadySetup) {
      return;
    }

    roleService.getOrCreateRole(RoleType.ROLE_ADMIN);
    roleService.getOrCreateRole(RoleType.ROLE_USER);
    roleService.getOrCreateRole(RoleType.ROLE_READER);
    roleService.getOrCreateRole(RoleType.ROLE_WRITER);

    Role adminRole = roleRepository.findByType(RoleType.ROLE_ADMIN).get();
    Role basicRole = roleRepository.findByType(RoleType.ROLE_USER).get();
    Role readerRole = roleRepository.findByType(RoleType.ROLE_READER).get();
    Role writerRole = roleRepository.findByType(RoleType.ROLE_WRITER).get();

    Account account = new Account();
    account.setEmail("admin@test.com");
    account.setRoles(Arrays.asList(adminRole, readerRole, writerRole));
    account.setPassword(encoder.encode("admin"));

    log.info("[x] Preloading " + accountRepository.save(account));

    account = new Account();
    account.setEmail("user@test.com");
    account.setRoles(Collections.singletonList(basicRole));
    account.setPassword(encoder.encode("password"));

    log.info("[x] Preloading " + accountRepository.save(account));

    alreadySetup = true;
  }

}
