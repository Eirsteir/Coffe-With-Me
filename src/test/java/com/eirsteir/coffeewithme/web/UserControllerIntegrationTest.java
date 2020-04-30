package com.eirsteir.coffeewithme.web;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.service.UserService;

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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;
    private List<User> allUsers = new ArrayList<>();

    @BeforeEach
    public void setup() {
        user = User.builder()
                .username("alex")
                .build();

        User otherUser = User.builder()
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
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(user.getUsername())));
    }

    @Test
    void one() {
    }

    @Test
    void create() {
    }
}