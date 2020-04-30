package com.eirsteir.coffeewithme.web;

import com.eirsteir.coffeewithme.domain.user.NewUserForm;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    public static final String USERNAME_ALEX = "alex";
    public static final String EMAIL_ALEX = "alex@email.com";
    public static final String PASSWORD_ALEX = "12345678";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;
    private List<User> allUsers = new ArrayList<>();

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .username("alex")
                .email("alex@email.com")
                .password("12345678")
                .build();

        User otherUser = User.builder()
                .id(2L)
                .username("adam")
                .email("adam@email.com")
                .password("12345678")
                .build();

        allUsers.add(user);
        allUsers.add(otherUser);

        given(userService.getAllUsers()).willReturn(allUsers);

        given(userService.findById(1L)).willReturn(Optional.ofNullable(user));

        Mockito.when(userService.saveUser(Mockito.any(NewUserForm.class)))
                .thenReturn(user);

        Mockito.when(userService.update(Mockito.any(User.class)))
                .thenReturn(user);
    }

    @Test
    void testGetAllUsersReturnsJsonArrayWhenEmployeesGiven() throws Exception {

        mockMvc.perform(get("/users")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())));
    }

    @Test
    void testGetUserByIdReturnsUser() throws Exception {

        mockMvc.perform(get("/users/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
                .andExpect(content()
                                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    void testCreateUserReturnsCreatedUser() throws Exception {
        NewUserForm newUserForm = NewUserForm.builder()
                .username(USERNAME_ALEX)
                .email(EMAIL_ALEX)
                .verifyEmail(EMAIL_ALEX)
                .password(PASSWORD_ALEX)
                .verifyPassword(PASSWORD_ALEX)
                .build();

        mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(newUserForm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    void testUpdateUserReturnsUpdatedUser() throws Exception {
       user.setUsername("updatedUsername");

        mockMvc.perform(put("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(user)))
                .andExpect(status().isOk())
                .andExpect(content()
                                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is("updatedUsername")));
    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}