package com.eirsteir.coffeewithme.web;

import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.service.UserService;
import com.eirsteir.coffeewithme.web.v1.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    public static final String USERNAME_ALEX = "alex";
    public static final String EMAIL_ALEX = "alex@email.com";
    public static final String PASSWORD_ALEX = "12345678";
    public static final String NAME_ALEX = "Alex";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDto userDto;
    private OAuth2User principal;

    @BeforeEach
    public void setup() {
        principal = new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                return Map.of(
                        "name", NAME_ALEX,
                        "id", "123"
                );
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getName() {
                return NAME_ALEX;
            }
        };

        userDto = UserDto.builder()
                .username("alex")
                .email("alex@email.com")
                .name(NAME_ALEX)
                .build();

        Mockito.when(userService.signUp(Mockito.any(UserDto.class)))
                .thenReturn(userDto);

        Mockito.when(userService.updateProfile(Mockito.any(UserDto.class)))
                .thenReturn(userDto);
    }


    @Test
    void testCreateUserReturnsCreatedUser() throws Exception {

        mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(principal)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(NAME_ALEX)));
    }

    @Test
    void testUpdateProfileReturnsUpdatedUserDto() throws Exception {
       userDto.setUsername(USERNAME_ALEX);

        mockMvc.perform(put("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content()
                                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is(EMAIL_ALEX)))
                .andExpect(jsonPath("$.username", is(USERNAME_ALEX)));
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}