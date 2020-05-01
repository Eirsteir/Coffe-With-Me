package com.eirsteir.coffeewithme.web;

import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.service.UserService;
import com.eirsteir.coffeewithme.web.v1.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.eirsteir.coffeewithme.util.JSONUtils.asJsonString;
import static com.eirsteir.coffeewithme.util.OAuthUtils.createOAuth2User;
import static com.eirsteir.coffeewithme.util.OAuthUtils.getOauthAuthenticationFor;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    public static final String USERNAME_ALEX = "alex";
    public static final String EMAIL_ALEX = "alex@email.com";
    public static final String NAME_ALEX = "Alex";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    private UserService userService;

    private UserDto userDto;
    private OAuth2User principal;
    private Authentication authenticationToken;

    @BeforeEach
    public void setup() {
        principal = createOAuth2User(NAME_ALEX, EMAIL_ALEX);
        authenticationToken = getOauthAuthenticationFor(principal);

        userDto = UserDto.builder()
                .username("alex")
                .email("alex@email.com")
                .name(NAME_ALEX)
                .build();

        Mockito.when(userService.loginOrSignUp(Mockito.any(UserDto.class)))
                .thenReturn(userDto);

        Mockito.when(userService.updateProfile(Mockito.any(UserDto.class)))
                .thenReturn(userDto);
    }


    @Test
    void testLoginAuthenticated() throws Exception {

        mockMvc.perform(get("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(authentication(authenticationToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(NAME_ALEX)))
                .andExpect(jsonPath("$.email", is(EMAIL_ALEX)));
    }

    @Test
    void testLoginUnAuthenticated() throws Exception {

        mockMvc.perform(get("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateProfileReturnsUpdatedUserDto() throws Exception {
       userDto.setUsername(USERNAME_ALEX);

        mockMvc.perform(put("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userDto))
                                .with(authentication(authenticationToken)))
                .andExpect(status().isOk());

    }

}