package com.eirsteir.coffeewithme.web;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

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
                .build();

        User otherUser = User.builder()
                .id(2L)
                .username("adam")
                .build();

        allUsers.add(user);
        allUsers.add(otherUser);
    }

    @Test
    void testGetAllUsersReturnsJsonArrayWhenEmployeesGiven() throws Exception {
        given(userService.getAllUsers()).willReturn(allUsers);

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
        given(userService.findById(1L)).willReturn(Optional.ofNullable(user));

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
        User newUser = User.builder()
                .id(3L)
                .username("john")
                .build();

        given(userService.saveUser(newUser)).willReturn(newUser);

        mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content()
                                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(newUser.getId().intValue())))
                .andExpect(jsonPath("$.username", is(newUser.getUsername())));
    }

//    @Test
//    void testUpdateUserReturnsUpdatedUser() throws Exception {
//        User newUser = User.builder()
//                .id(3L)
//                .username("john")
//                .build();
//
//        given(userService.update(newUser)).willReturn(newUser);
//
//        mockMvc.perform(post("/users")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(asJsonString(newUser)))
//                .andExpect(status().isCreated())
//                .andExpect(content()
//                                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id", is(newUser.getId().intValue())))
//                .andExpect(jsonPath("$.username", is(newUser.getUsername())));
//    }

    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}