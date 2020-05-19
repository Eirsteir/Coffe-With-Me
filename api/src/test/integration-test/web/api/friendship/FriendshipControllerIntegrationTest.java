package web.api.friendship;

import com.eirsteir.coffeewithme.CoffeeWithMeApplication;
import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.testconfig.RedisTestConfig;
import com.eirsteir.coffeewithme.testconfig.SetupTestDataLoader;
import com.eirsteir.coffeewithme.util.JSONUtils;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@Import({RedisTestConfig.class, SetupTestDataLoader.class})
@SpringBootTest(classes = CoffeeWithMeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FriendshipControllerIntegrationTest {

    private static final String REQUESTER_EMAIL = "requester@test.com";
    private static final String ADDRESSEE_EMAIL = "addressee@test.com";
    public static final String OTHER_USER_EMAIL = "other-user@test.com";

    private User requester;
    private User addressee;
    private Friendship requestedFriendship;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        requester = userRepository.findByEmail(REQUESTER_EMAIL).get();
        addressee = userRepository.findByEmail(ADDRESSEE_EMAIL).get();
        User otherUser = userRepository.findByEmail(OTHER_USER_EMAIL).get();

        FriendshipId requestedFriendshipId = FriendshipId.builder()
                .requester(requester)
                .addressee(otherUser)
                .build();

        requestedFriendship = friendshipRepository.findById(requestedFriendshipId).get();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(value = REQUESTER_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testGetFriendsReturnsAcceptedFriendships() throws Exception {

        mvc.perform(get("/{id}/friends", requester.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", equalTo(ADDRESSEE_EMAIL)));
    }

    @Test
    @WithUserDetails(value = ADDRESSEE_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testGetFriendsWhenAddresseeIsRequesterReturnsAcceptedFriendships() throws Exception {

        mvc.perform(get("/{id}/friends", addressee.getId())
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", equalTo(REQUESTER_EMAIL)));
    }

    @Test
    @WithUserDetails(value = OTHER_USER_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testAddFriendWhenUserFoundReturnsFriendship() throws Exception {

        mvc.perform(post("/friends")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("to_friend", addressee.getId().toString()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id.requester.email", equalTo(OTHER_USER_EMAIL)))
                .andExpect(jsonPath("$.id.addressee.email", equalTo(ADDRESSEE_EMAIL)))
                .andExpect(jsonPath("$.status", equalTo(FriendshipStatus.REQUESTED.getStatus())));
    }

    @Test
    @WithUserDetails(value = REQUESTER_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testAddFriendToSelfReturnsHttp400() throws Exception {

        mvc.perform(post("/friends")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("to_friend", requester.getId().toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails(value = OTHER_USER_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testAcceptFriendshipReturnsUpdatedFriendship() throws Exception {
        requestedFriendship.setStatus(FriendshipStatus.ACCEPTED);
        FriendshipDto friendshipDto = modelMapper.map(requestedFriendship, FriendshipDto.class);

        mvc.perform(put("/friends")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONUtils.asJsonString(friendshipDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.requester.email", equalTo(REQUESTER_EMAIL)))
                .andExpect(jsonPath("$.id.addressee.email", equalTo(OTHER_USER_EMAIL)))
                .andExpect(jsonPath("$.status", equalTo(FriendshipStatus.ACCEPTED.getStatus())));
    }

    @Test
    @WithUserDetails(value = ADDRESSEE_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testAcceptFriendshipNotBelongingToUserReturnsHttp400() throws Exception {
        FriendshipDto friendshipDto = FriendshipDto.builder()
                .requesterId(requestedFriendship.getRequester().getId())
                .addresseeId(requestedFriendship.getAddressee().getId())
                .build();

        mvc.perform(put("/friends")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONUtils.asJsonString(friendshipDto)))
                .andExpect(status().isBadRequest());
    }
}