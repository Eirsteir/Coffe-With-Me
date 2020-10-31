package com.eirsteir.coffeewithme.config;

import com.eirsteir.coffeewithme.social.repository.*;
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakService;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FriendshipControllerTestConfiguration {

  @MockBean private CoffeeBreakRepository coffeeBreakRepository;

  @MockBean private UserRepository userRepository;
  @MockBean private CampusRepository campusRepository;
  @MockBean private FriendshipRepository friendshipRepository;
  @MockBean private UniversityRepository universityRepository;

  @Bean
  public FriendshipService friendshipService() {
    return Mockito.mock(FriendshipService.class);
  }

  @Bean
  public UserService userService() {
    return Mockito.mock(UserService.class);
  }

  @Bean
  public CoffeeBreakService coffeeBreakService() {
    return Mockito.mock(CoffeeBreakService.class);
  }
}
