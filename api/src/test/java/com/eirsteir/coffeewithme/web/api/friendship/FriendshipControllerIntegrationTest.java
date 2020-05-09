package com.eirsteir.coffeewithme.web.api.friendship;

import com.eirsteir.coffeewithme.domain.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.util.JSONUtils;
import config.RedisTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(RedisTestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FriendshipControllerIntegrationTest {


    private static final String REQUESTER_EMAIL = "requester@test.com";
    private static final String ADDRESSEE_EMAIL = "addressee@test.com";
    private static final String REQUESTER_USERNAME = "requester";
    private static final String ADDRESSEE_USERNAME = "addressee";
    public static final String OTHER_USER_EMAIL = "other-user@test.com";
    public static final String OTHER_USER_USERNAME = "other-user";

    private User requester;
    private User addressee;
    private User otherUser;
    private FriendshipId friendshipId;
    private FriendshipId requestedFriendshipId;
    private Friendship friendship;
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
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @PostConstruct
    void setupTestData() {
        userRepository.saveAndFlush(User.builder()
                                                        .email(REQUESTER_EMAIL)
                                                        .username(REQUESTER_USERNAME)
                                                        .build());
        userRepository.saveAndFlush(User.builder()
                                                        .email(ADDRESSEE_EMAIL)
                                                        .username(ADDRESSEE_USERNAME)
                                                        .build());
        userRepository.saveAndFlush(User.builder()
                                                             .email(OTHER_USER_EMAIL)
                                                             .username(OTHER_USER_USERNAME)
                                                             .build());
        requester = userRepository.findByEmail(REQUESTER_EMAIL).get();
        addressee = userRepository.findByEmail(ADDRESSEE_EMAIL).get();
        otherUser = userRepository.findByEmail(OTHER_USER_EMAIL).get();

        friendshipId = FriendshipId.builder()
                .requester(requester)
                .addressee(addressee)
                .build();

        friendship = friendshipRepository.saveAndFlush(Friendship.builder()
                                                               .id(friendshipId)
                                                               .status(FriendshipStatus.ACCEPTED)
                                                               .build());

        requestedFriendshipId = FriendshipId.builder()
                .requester(requester)
                .addressee(otherUser)
                .build();
        friendshipRepository.saveAndFlush(Friendship.builder()
                                                               .id(requestedFriendshipId)
                                                               .status(FriendshipStatus.REQUESTED)
                                                               .build());
        requestedFriendship = friendshipRepository.findById(requestedFriendshipId).get();
    }

    @Test
    @WithUserDetails(value = REQUESTER_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testAllFriendships() throws Exception {

        mvc.perform(get("/api/user/friends")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id.requester.email", equalTo(REQUESTER_EMAIL)));
    }

    @Test
    @WithUserDetails(value = ADDRESSEE_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testAllFriendshipsWhenAddresseeIsRequester() throws Exception {

        mvc.perform(get("/api/user/friends")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id.addressee.email", equalTo(ADDRESSEE_EMAIL)));
    }

    @Test
    @WithUserDetails(value = OTHER_USER_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testAddFriendshipWhenUserFound() throws Exception {

        mvc.perform(post("/api/user/friends")
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("to_friend", addressee.getId().toString()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id.requester.email", equalTo(OTHER_USER_EMAIL)))
                .andExpect(jsonPath("$.id.addressee.email", equalTo(ADDRESSEE_EMAIL)))
                .andExpect(jsonPath("$.status", equalTo(FriendshipStatus.REQUESTED.getStatus())));
    }

    @Test
    @WithUserDetails(value = OTHER_USER_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void acceptFriendship() throws Exception {
        FriendshipDto friendshipDto = modelMapper.map(requestedFriendship, FriendshipDto.class);

        mvc.perform(put("/api/user/friends")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONUtils.asJsonString(friendshipDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id.requester.email", equalTo(REQUESTER_EMAIL)))
                .andExpect(jsonPath("$.id.addressee.email", equalTo(OTHER_USER_EMAIL)))
                .andExpect(jsonPath("$.status", equalTo(FriendshipStatus.ACCEPTED.getStatus())));
    }
}