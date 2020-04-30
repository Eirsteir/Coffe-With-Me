package com.eirsteir.coffeewithme.web;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    private User user;
    private List<User> users = new ArrayList<>();

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("alex");
        User otherUser = new User();
        otherUser.setUsername("adam");

        users.add(user);
        users.add(otherUser);
    }

    @Test
    void testGetAllUsersReturnsJsonArray() {

    }

    @Test
    void one() {
    }

    @Test
    void create() {
    }
}