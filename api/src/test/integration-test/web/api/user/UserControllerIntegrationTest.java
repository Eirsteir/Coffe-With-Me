package web.api.user;

import com.eirsteir.coffeewithme.CoffeeWithMeApplication;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.testconfig.RedisTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RedisTestConfig.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CoffeeWithMeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    public static final String JOHN_EMAIL = "john@doe.com";
    public static final String TOM_EMAIL = "tom@doe.com";

    private User userJohn;
    private User userTom;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        userJohn = User.builder()
                .name("John Doe")
                .email(JOHN_EMAIL)
                .username("doe")
                .build();
        userRepository.save(userJohn);

        userTom = User.builder()
                .name("Tom Doe")
                .email(TOM_EMAIL)
                .build();
        userRepository.save(userJohn);

    }

    @Test
    @WithUserDetails(value = JOHN_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testGetFriendsReturnsAcceptedFriendships() throws Exception {

        mvc.perform(get("/users", userJohn.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)));
    }
    @Test
    @WithUserDetails(value = JOHN_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    public void givenFirstAndLastName_whenGettingListOfUsers_thenCorrect() throws Exception {

        mvc.perform(get("/?search=name:doe", userJohn.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)))
                .andExpect(jsonPath("$[1].email", equalTo(TOM_EMAIL)));
    }

    @Test
    @WithUserDetails(value = JOHN_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    public void givenFirstNameInverse_whenGettingListOfUsers_thenCorrect() throws Exception {

        mvc.perform(get("/?search=name!john+doe", userJohn.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", equalTo(TOM_EMAIL)));
    }

    @Test
    @WithUserDetails(value = JOHN_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    public void givenFirstNamePrefix_whenGettingListOfUsers_thenCorrect() throws Exception{

        mvc.perform(get("/?search=name:jo*", userJohn.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)));
    }

    @Test
    @WithUserDetails(value = JOHN_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    public void givenFirstNameSuffix_whenGettingListOfUsers_thenCorrect() throws Exception {

        mvc.perform(get("/?search=name:*e", userJohn.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)))
                .andExpect(jsonPath("$[0].email", equalTo(TOM_EMAIL)));
    }

    @Test
    @WithUserDetails(value = JOHN_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    public void givenFirstNameSubstring_whenGettingListOfUsers_thenCorrect() throws Exception {

        mvc.perform(get("/?search=name:*oh*", userJohn.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)));
    }

    @Test
    @WithUserDetails(value = JOHN_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    public void givenFirstOrLastName_whenGettingListOfUsers_thenCorrect() throws Exception {

        mvc.perform(get("/?search=name:john,'username:doe", userJohn.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", equalTo(JOHN_EMAIL)));
    }
}