package com.eirsteir.coffeewithme.social.web.api.user;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eirsteir.coffeewithme.config.EventuateTestConfig;
import com.eirsteir.coffeewithme.config.SetupTestDataLoader;
import com.eirsteir.coffeewithme.social.SocialServiceApplication;
import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.security.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Import({ModelMapperConfig.class, SetupTestDataLoader.class})
@SpringBootTest(
    classes = {SocialServiceApplication.class, SecurityConfig.class, EventuateTestConfig.class},
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

  public static final String JOHN_EMAIL = "john@doe.com";
  public static final String TOM_EMAIL = "tom@doe.com";
  private static final String ADDRESSEE_EMAIL = "addressee@test.com";

  private Long addresseeId;

  @Autowired private UserRepository userRepository;

  @Autowired private WebApplicationContext context;

  private MockMvc mvc;

  @BeforeEach
  public void setup() {
    addresseeId = userRepository.findByEmail(ADDRESSEE_EMAIL).get().getId();
    mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  @WithMockUser
  void testSearchByName_thenReturnMatchingUsers() throws Exception {

    mvc.perform(get("/users?search=name=='John Doe'").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)));
  }

  @Test
  @WithMockUser
  void testSearchByNameInverse_thenReturnMatchingUsers() throws Exception {

    mvc.perform(get("/users?search=name!='John Doe'").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(jsonPath("$[0].email", equalTo(TOM_EMAIL)));
  }

  @Test
  @WithMockUser
  void testSearchByNamePrefix_thenReturnMatchingUsers() throws Exception {

    mvc.perform(get("/users?search=name==Jo*").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)));
  }

  @Test
  @WithMockUser
  void testSearchByNameSuffix_thenReturnMatchingUsers() throws Exception {

    mvc.perform(get("/users?search=name==*e").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)))
        .andExpect(jsonPath("$[1].email", equalTo(TOM_EMAIL)));
  }

  @Test
  @WithMockUser
  void testSearchByNameSubstring_thenReturnMatchingUsers() throws Exception {

    mvc.perform(get("/users?search=name==*oh*").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)));
  }

  @Test
  @WithMockUser
  void testSearchByNameOrNickname_thenReturnMatchingUsers() throws Exception {

    mvc.perform(
            get("/users?search=name==john,nickname==johndoe")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)));
  }

  @Test
  @WithMockUser
  void testSearchByNameAndNickname_thenReturnMatchingUsers() throws Exception {

    mvc.perform(
            get("/users?search=name=='John Doe';nickname==johndoe")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)));
  }

  @Test
  @WithMockUser
  void testSearchByNameWhenNoResults_thenReturnHttp204() throws Exception {
    String query = "name==john";
    mvc.perform(get("/users?search=" + query).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andExpect(
            jsonPath("$.message", equalTo("Search query '" + query + "' yielded no results")));
  }

  @Test
  @WithMockUser
  void testUserWhenCurrentUserIsFriend_thenReturnHttp200WithIsFriendEqualToTrue() throws Exception {

    mvc.perform(get("/users/" + addresseeId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isFriend", equalTo(true)));
  }

  @Test
  @WithMockUser(value = JOHN_EMAIL)
  void testUserWhenCurrentUserIsNotFriend_thenReturnHttp200WithIsFriendEqualToFalse()
      throws Exception {

    mvc.perform(get("/users/" + addresseeId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isFriend", equalTo(false)));
  }
}
