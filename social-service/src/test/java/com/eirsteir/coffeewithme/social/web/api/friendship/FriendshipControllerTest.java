package com.eirsteir.coffeewithme.social.web.api.friendship;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.commons.security.JwtConfig;
import com.eirsteir.coffeewithme.commons.security.JwtUtils;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.config.EventuateTestConfig;
import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.repository.*;
import com.eirsteir.coffeewithme.social.security.SecurityConfig;
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakService;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;
import com.eirsteir.coffeewithme.util.JSONUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;

import static com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus.ACCEPTED;
import static com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus.REQUESTED;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Import({SecurityConfig.class, ModelMapperConfig.class, EventuateTestConfig.class})
@WebMvcTest(controllers = FriendshipController.class)
class FriendshipControllerTest {

  private final Long REQUESTER_ID = 1L;
  private final Long ADDRESSEE_ID = 2L;
  private static final String REQUESTER_EMAIL = "requester@test.com";
  private static final String ADDRESSEE_EMAIL = "addressee@test.com";

  private User requester;
  private FriendshipDto friendshipDto;

  @Autowired private MockMvc mockMvc;

  @MockBean private UserDetailsService userDetailsService;

  @Autowired private FriendshipService friendshipService;

  @Autowired private UserService userService;

  @TestConfiguration
  static class FriendshipControllerTestConfiguration {

    @MockBean
    private CoffeeBreakRepository coffeeBreakRepository;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CampusRepository campusRepository;
    @MockBean
    private FriendshipRepository friendshipRepository;
    @MockBean
    private UniversityRepository universityRepository;
    @Bean
    public FriendshipService friendshipService()
    {
      return Mockito.mock(FriendshipService.class);
    }
    @Bean
    public UserService userService()
    {
      return Mockito.mock(UserService.class);
    }
    @Bean
    public CoffeeBreakService coffeeBreakService()
    {
      return Mockito.mock(CoffeeBreakService.class);
    }

  }

  @Autowired private JwtConfig jwtConfig;

  private String requesterToken;

  @BeforeEach
  void setUp() {
    UserDetailsImpl userDetails = UserDetailsImpl.builder().id(REQUESTER_ID).email(REQUESTER_EMAIL).build();
    Authentication authentication = Mockito.mock(Authentication.class);

    when(authentication.getPrincipal()).thenReturn(userDetails);

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
    requesterToken = JwtUtils.createJwtToken(jwtConfig, authentication, principal);
    requester = User.builder().id(REQUESTER_ID).email(REQUESTER_EMAIL).build();

    friendshipDto =
        FriendshipDto.builder()
            .requester(UserDetailsDto.builder().id(REQUESTER_ID).build())
            .addressee(UserDetailsDto.builder().id(ADDRESSEE_ID).build())
            .status(REQUESTED)
            .build();
  }

  @Test
  void testAddFriendWhenAddresseeExists_thenReturnHttp200() throws Exception {
    when(userService.findUserById(requester.getId()))
            .thenReturn(requester);
    when(friendshipService.registerFriendship(Mockito.any(FriendRequest.class)))
        .thenReturn(friendshipDto);

    mockMvc
        .perform(
            post("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .param("to_friend", String.valueOf(ADDRESSEE_ID))
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + requesterToken))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.requester.id", equalTo((REQUESTER_ID.intValue()))))
        .andExpect(jsonPath("$.addressee.id", equalTo(ADDRESSEE_ID.intValue())))
        .andExpect(jsonPath("$.status", equalTo(FriendshipStatus.REQUESTED.getStatus())));
  }

  @Test
  void testAddFriendWhenAddresseeDoesNotExists_thenReturnHttp404() throws Exception {
    when(friendshipService.registerFriendship(Mockito.any(FriendRequest.class)))
        .thenThrow(CWMException.EntityNotFoundException.class);

    mockMvc
        .perform(
            post("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .param("to_friend", String.valueOf(ADDRESSEE_ID))
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + requesterToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testAddFriendWhenFriendshipAlreadyExists_thenReturnHttp400() throws Exception {
    when(friendshipService.registerFriendship(Mockito.any(FriendRequest.class)))
        .thenThrow(CWMException.DuplicateEntityException.class);

    mockMvc
        .perform(
            post("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .param("to_friend", String.valueOf(ADDRESSEE_ID))
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + requesterToken))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testAddFriendWhenUnauthorized_thenReturnHttp401() throws Exception {

    mockMvc
        .perform(
            post("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .param("to_friend", String.valueOf(ADDRESSEE_ID)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void testGetFriendsWhenUserHasNoFriendships_thenReturnHttp204() throws Exception {
    when(userService.findUserById(requester.getId()))
            .thenReturn(requester);
    when(friendshipService.findFriendshipsOf(Mockito.any(UserDetailsDto.class)))
        .thenReturn(new ArrayList<>());

    mockMvc
        .perform(get("/friends")
                         .contentType(MediaType.APPLICATION_JSON)
                         .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + requesterToken))
        .andExpect(status().isNoContent())
        .andExpect(
            jsonPath(
                "$.message",
                equalTo("User with email - " + requester.getEmail() + " has no friends")));
  }

  @Test
  void testGetFriendsWhenUnauthorized_thenReturnHttp401() throws Exception {

    mockMvc
        .perform(get("/friends").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void testGetFriendsWhenUserHasFriendships_thenReturnHttp200WithFriends() throws Exception {
    when(userService.findUserById(requester.getId())).thenReturn(requester);
    when(friendshipService.findFriendshipsOf(Mockito.any(UserDetailsDto.class)))
        .thenReturn(
            Collections.singletonList(
                FriendshipDto.builder()
                    .requester(UserDetailsDto.builder().id(REQUESTER_ID).build())
                    .addressee(UserDetailsDto.builder().id(ADDRESSEE_ID).build())
                    .build()));

    mockMvc
        .perform(get("/friends", requester.getId())
                         .contentType(MediaType.APPLICATION_JSON)
                         .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + requesterToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].requester.id", equalTo((REQUESTER_ID.intValue()))))
        .andExpect(jsonPath("$[0].addressee.id", equalTo(ADDRESSEE_ID.intValue())));
  }

  @Test
  void testGetFriendRequestsWhenUserHasFriendshipsWithStatusRequested_thenReturnListOfFriendships()
      throws Exception {
    when(userService.findUserById(requester.getId()))
            .thenReturn(requester);
    when(friendshipService.findFriendshipsOf(Mockito.any(Long.class), eq(REQUESTED)))
        .thenReturn(
            Collections.singletonList(
                FriendshipDto.builder()
                    .requester(UserDetailsDto.builder().id(REQUESTER_ID).build())
                    .addressee(UserDetailsDto.builder().id(ADDRESSEE_ID).build())
                    .build()));

    mockMvc
        .perform(
            get("/friends/requests", requester.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + requesterToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].requester.id", equalTo((REQUESTER_ID.intValue()))))
        .andExpect(jsonPath("$[0].addressee.id", equalTo(ADDRESSEE_ID.intValue())));
  }

  @Test
  void testGetFriendRequestsWhenUserHasNoFriendshipsWithStatusRequested_thenReturnHttp204()
      throws Exception {
    when(userService.findUserById(requester.getId())).thenReturn(requester);
    when(friendshipService.findFriendshipsOf(Mockito.any(Long.class), eq(REQUESTED)))
        .thenReturn(new ArrayList<>());

    mockMvc
        .perform(
            get("/friends/requests", requester.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + requesterToken))
        .andExpect(status().isNoContent())
        .andExpect(
            jsonPath(
                "$.message",
                equalTo("User with email - " + REQUESTER_EMAIL + " has no friend requests")));
  }

  @Test
  void testAcceptFriendshipWhenFriendshipExistsAndStatusIsRequested_thenReturnHttp200()
      throws Exception {
    UserDetailsImpl userDetails = UserDetailsImpl.builder().id(ADDRESSEE_ID).email(ADDRESSEE_EMAIL).build();
    Authentication authentication = Mockito.mock(Authentication.class);

    when(authentication.getPrincipal()).thenReturn(userDetails);

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
    String addresseeToken = JwtUtils.createJwtToken(jwtConfig, authentication, principal);

    when(friendshipService.updateFriendship(Mockito.any(FriendshipDto.class)))
        .thenReturn(friendshipDto.setStatus(ACCEPTED));

    mockMvc
        .perform(
            put("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtils.asJsonString(friendshipDto))
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + addresseeToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo(ACCEPTED.getStatus())));
  }

  @Test
  void testAcceptFriendshipWhenFriendshipDoesNotBelongToUser_thenReturnHttp400() throws Exception {
    friendshipDto
        .setRequester(UserDetailsDto.builder().id(100L).build())
        .setAddressee(UserDetailsDto.builder().id(101L).build());

    mockMvc
        .perform(
            put("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtils.asJsonString(friendshipDto))
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + requesterToken))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", equalTo("Friendship does not belong to current user")));
  }

  @Test
  void testAcceptFriendshipSentBySelf_thenReturnHttp400() throws Exception {

    mockMvc
        .perform(
            put("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtils.asJsonString(friendshipDto))
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + requesterToken))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", equalTo("Cannot accept friend request sent by yourself")));
  }

  @Test
  void testAcceptFriendshipWhenFriendshipNotFound_thenReturnHttp404() throws Exception {
    UserDetailsImpl userDetails = UserDetailsImpl.builder().id(ADDRESSEE_ID).email(ADDRESSEE_EMAIL).build();
    Authentication authentication = Mockito.mock(Authentication.class);

    when(authentication.getPrincipal()).thenReturn(userDetails);

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
    String addresseeToken = JwtUtils.createJwtToken(jwtConfig, authentication, principal);

    when(friendshipService.updateFriendship(Mockito.any(FriendshipDto.class)))
        .thenThrow(CWMException.EntityNotFoundException.class);

    mockMvc
        .perform(
            put("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtils.asJsonString(friendshipDto))
                .header(jwtConfig.getHeader(), jwtConfig.getPrefix() + addresseeToken))
        .andExpect(status().isNotFound());
  }

  @Test
  void testAcceptFriendshipWhenUnauthorized_thenReturnHttp401() throws Exception {

    mockMvc
        .perform(
            put("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtils.asJsonString(friendshipDto)))
        .andExpect(status().isUnauthorized());
  }
}
