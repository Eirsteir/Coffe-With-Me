package com.eirsteir.coffeewithme.api.config;


import com.eirsteir.coffeewithme.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

  private boolean alreadySetup = false;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder encoder;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (alreadySetup) {
      return;
    }
//
//    User adminUser = User.builder()
//            .name("Admin")
//            .username("admin123")
//            .email("admin@test.com")
//            .password(encoder.encode("admin"))
//            .build();
//
//    log.info("[x] Preloading " + userRepository.save(adminUser));
//
//    User auditUser = User.builder()
//            .name("Audit")
//            .username("audit21")
//            .email("audit@test.com")
//            .password(encoder.encode("audit"))
//            .build();
//    log.info("[x] Preloading " + userRepository.save(auditUser));
//
//    User basicUser = User.builder()
//            .name("User")
//            .username("user01")
//            .email("user@test.com")
//            .password(encoder.encode("password"))
//            .build();
//
//    basicUser.addFriend(adminUser, FriendshipStatus.ACCEPTED);
//    basicUser.addFriend(auditUser, FriendshipStatus.REQUESTED);
//    log.info("[x] Preloading " + userRepository.save(basicUser));

    alreadySetup = true;
  }

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}
