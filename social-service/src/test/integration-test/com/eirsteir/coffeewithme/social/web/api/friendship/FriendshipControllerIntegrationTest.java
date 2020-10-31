package com.eirsteir.coffeewithme.social.web.api.friendship;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.config.EventuateTestConfig;
import com.eirsteir.coffeewithme.config.SetupTestDataLoader;
import com.eirsteir.coffeewithme.social.SocialServiceApplication;
import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.repository.CoffeeBreakRepository;
import com.eirsteir.coffeewithme.social.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.security.SecurityConfig;
import com.eirsteir.coffeewithme.util.JSONUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Requires a running database. Add role authentication
 * with @WithMockUser(username="admin",roles={"USER","ADMIN"})
 */
@Import({ModelMapperConfig.class, SetupTestDataLoader.class})
@SpringBootTest(
    classes = {SocialServiceApplication.class, SecurityConfig.class, EventuateTestConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FriendshipControllerIntegrationTest {

  private static final String REQUESTER_EMAIL = "requester@test.com";
  private static final String ADDRESSEE_EMAIL = "addressee@test.com";
  public static final String OTHER_USER_EMAIL = "other-user@test.com";

  private User requester;
  private User addressee;
  private User otherUser;
  private Friendship requestedFriendship;

  @Autowired private WebApplicationContext context;

  private MockMvc mvc;

  @Autowired private UserRepository userRepository;

  @Autowired private FriendshipRepository friendshipRepository;

  @Autowired private CoffeeBreakRepository coffeeBreakRepository;

  @Autowired private ModelMapper modelMapper;

  @BeforeEach
  public void setup() {
    requester = userRepository.findByEmail(REQUESTER_EMAIL).get();
    addressee = userRepository.findByEmail(ADDRESSEE_EMAIL).get();
    otherUser = userRepository.findByEmail(OTHER_USER_EMAIL).get();

    FriendshipId requestedFriendshipId =
        FriendshipId.builder().requester(requester).addressee(otherUser).build();

    requestedFriendship = friendshipRepository.findById(requestedFriendshipId).get();

    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  @WithMockUser(value = REQUESTER_EMAIL)
  void testGetFriendsReturnsAcceptedFriendships() throws Exception {

    mvc.perform(get("/friends").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].email", equalTo(ADDRESSEE_EMAIL)));
  }

  @Test
  @WithMockUser(value = ADDRESSEE_EMAIL)
  void testGetFriendsWhenAddresseeIsRequesterReturnsAcceptedFriendships() throws Exception {

    mvc.perform(get("/{id}/friends", addressee.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[1].email", equalTo(REQUESTER_EMAIL)));
  }

  @Test
  @WithMockUser(value = OTHER_USER_EMAIL)
  void testAddFriendWhenUserFoundReturnsFriendship() throws Exception {

    mvc.perform(
            post("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .param("to_friend", addressee.getId().toString()))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.requesterId", equalTo(otherUser.getId().intValue())))
        .andExpect(jsonPath("$.addresseeId", equalTo(addressee.getId().intValue())))
        .andExpect(jsonPath("$.status", equalTo(FriendshipStatus.REQUESTED.getStatus())));
  }

  @Test
  @WithMockUser(value = REQUESTER_EMAIL)
  void testAddFriendToSelfReturnsHttp400() throws Exception {

    mvc.perform(
            post("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .param("to_friend", requester.getId().toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(value = OTHER_USER_EMAIL)
  void testAcceptFriendshipReturnsUpdatedFriendship() throws Exception {
    requestedFriendship.setStatus(FriendshipStatus.ACCEPTED);
    FriendshipDto friendshipDto = modelMapper.map(requestedFriendship, FriendshipDto.class);

    mvc.perform(
            put("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtils.asJsonString(friendshipDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.requesterId", equalTo(requester.getId().intValue())))
        .andExpect(jsonPath("$.addresseeId", equalTo(otherUser.getId().intValue())))
        .andExpect(jsonPath("$.status", equalTo(FriendshipStatus.ACCEPTED.getStatus())));
  }

  @Test
  @WithMockUser(value = ADDRESSEE_EMAIL)
  void testAcceptFriendshipNotBelongingToUserReturnsHttp400() throws Exception {
    FriendshipDto friendshipDto =
        FriendshipDto.builder()
            .requester(modelMapper.map(requestedFriendship.getRequester(), UserDetailsDto.class))
            .addressee(modelMapper.map(requestedFriendship.getAddressee(), UserDetailsDto.class))
            .build();

    mvc.perform(
            put("/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtils.asJsonString(friendshipDto)))
        .andExpect(status().isBadRequest());
  }
}
