package com.eirsteir.coffeewithme.controllers;

import com.eirsteir.coffeewithme.CoffeeWithMeApplication;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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

    private final String USER_NAME_ALEX = "Alex";
    private static final String EMAIL_ADDRESS_ALEX = "alex@email.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Validator validator;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
        user = User.builder()
                .username("alex")
                .build();

        userRepository.save(user);
    }

    @Test
    public void givenEmployees_whenGetEmployees_thenStatus200()
            throws Exception {

        mockMvc.perform(get("/users")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                                   .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username", is("alex")));
    }

    @Test
    public void testCreateUserWithNonUniqueUsername() throws Exception {

        User newUser = User.builder()
                .username(USER_NAME_ALEX)
                .emailAddress("unique@email.com")
                .firstName("Test")
                .lastName("Testesen")
                .password("12345678")
                .confirmPassword("12345678")
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(newUser);
        // then
        assertThat(violations).hasSize(1);
    }
}