package com.eirsteir.coffeewithme.social.config;


import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
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

    User adminUser = User.builder()
            .id(1000L)
            .name("Admin")
            .nickname("admin123")
            .email("admin@test.com")
            .build();

    log.info("[x] Preloading " + userRepository.save(adminUser));

    User auditUser = User.builder()
            .id(1001L)
            .name("Audit")
            .nickname("audit21")
            .email("audit@test.com")
            .build();
    log.info("[x] Preloading " + userRepository.save(auditUser));

    User basicUser = User.builder()
            .id(1002L)
            .name("User")
            .nickname("user01")
            .email("user@test.com")
            .build();

    basicUser.addFriend(adminUser, FriendshipStatus.ACCEPTED);
    basicUser.addFriend(auditUser, FriendshipStatus.REQUESTED);
    log.info("[x] Preloading " + userRepository.save(basicUser));

    alreadySetup = true;
  }

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}
