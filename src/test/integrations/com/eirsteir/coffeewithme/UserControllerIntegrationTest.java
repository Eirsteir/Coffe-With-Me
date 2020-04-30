package com.eirsteir.coffeewithme.integrations;

import com.eirsteir.coffeewithme.CoffeeWithMeApplication;
import com.eirsteir.coffeewithme.domain.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = CoffeeWithMeApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private User user;
    private List<User> allUsers = new ArrayList<>();

    @Test
    public void givenEmployees_whenGetEmployees_thenStatus200()
            throws Exception {
        user = User.builder()
                .username("alex")
                .build();

        allUsers.add(user);

        mvc.perform(get("/users")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username", is("alex")));
    }}