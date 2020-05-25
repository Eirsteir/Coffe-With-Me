package com.eirsteir.coffeewithme.social.web.api.friendship;

import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.FriendshipDto;
import com.eirsteir.coffeewithme.social.exception.CWMException;
import com.eirsteir.coffeewithme.social.security.SecurityConfig;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.web.request.FriendRequest;
import com.eirsteir.coffeewithme.commons.dto.UserDetails;
import com.eirsteir.coffeewithme.testconfig.MessageTemplateUtilTestConfig;
import com.eirsteir.coffeewithme.util.JSONUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;

import static com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus.ACCEPTED;
import static com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus.REQUESTED;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, MessageTemplateUtilTestConfig.class, ModelMapperConfig.class})
@TestPropertySource("classpath:exception.properties")
@WebMvcTest(FriendshipController.class)
@ExtendWith(SpringExtension.class)
class FriendshipControllerTest {

    private final Long REQUESTER_ID = 1L;
    private final Long ADDRESSEE_ID = 2L;
    private static final String REQUESTER_EMAIL = "requester@test.com";
    private static final String ADDRESSEE_EMAIL = "addressee@test.com";

    private User requester;
    private FriendshipDto friendshipDto;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendshipService friendshipService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @MockBean
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        requester = User.builder()
                .id(REQUESTER_ID)
                .email(REQUESTER_EMAIL)
                .password("password")
                .build();

        friendshipDto = FriendshipDto.builder()
                .requesterId(REQUESTER_ID)
                .addresseeId(ADDRESSEE_ID)
                .status(REQUESTED)
                .build();
    }

    @Test
    void testAddFriendWhenAddresseeExists_thenReturnHttp200() throws Exception {
        when(friendshipService.registerFriendship(Mockito.any(FriendRequest.class)))
                .thenReturn(friendshipDto);

        mockMvc.perform(post("/friends")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("to_friend", String.valueOf(ADDRESSEE_ID)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requesterId", equalTo(requester.getId().intValue())))
                .andExpect(jsonPath("$.addresseeId", equalTo(ADDRESSEE_ID.intValue())))
                .andExpect(jsonPath("$.status", equalTo(FriendshipStatus.REQUESTED.getStatus())));
    }

    @Test
    void testAddFriendWhenAddresseeDoesNotExists_thenReturnHttp404() throws Exception {
        when(friendshipService.registerFriendship(Mockito.any(FriendRequest.class)))
                .thenThrow(CWMException.EntityNotFoundException.class);

        mockMvc.perform(post("/friends")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("to_friend", String.valueOf(ADDRESSEE_ID)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddFriendWhenFriendshipAlreadyExists_thenReturnHttp404() throws Exception {
        when(friendshipService.registerFriendship(Mockito.any(FriendRequest.class)))
                .thenThrow(CWMException.DuplicateEntityException.class);

        mockMvc.perform(post("/friends")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("to_friend", String.valueOf(ADDRESSEE_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddFriendWhenUnauthorized_thenReturnHttp401() throws Exception {

        mockMvc.perform(post("/friends")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("to_friend", String.valueOf(ADDRESSEE_ID)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetFriendsWhenUserNotFound_thenReturnHttp404() throws Exception {
        Long userNotFoundId = 100L;
        when(userService.findUserById(userNotFoundId))
                .thenThrow(CWMException.EntityNotFoundException.class);

        mockMvc.perform(get("/{id}/friends", userNotFoundId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetFriendsWhenUserHasNoFriendships_thenReturnHttp204() throws Exception {
        when(userService.findUserById(requester.getId()))
                .thenReturn(requester);
        when(friendshipService.getFriends(Mockito.any(UserDetails.class)))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/{id}/friends", requester.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", equalTo("User with email - " +
                                                                 requester.getEmail() +
                                                                 " has no friends")));
    }

    @Test
    void testGetFriendsWhenUnauthorized_thenReturnHttp401() throws Exception {

        mockMvc.perform(get("/friends")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetFriendsWhenUserHasFriendships_thenReturnHttp200WithFriends() throws Exception {
        when(userService.findUserById(requester.getId()))
                .thenReturn(requester);
        when(friendshipService.getFriends(Mockito.any(UserDetails.class)))
                .thenReturn(Arrays.asList(
                        UserDetails.builder()
                        .email(REQUESTER_EMAIL)
                        .build(),
                        UserDetails.builder()
                        .email(ADDRESSEE_EMAIL)
                        .build()));

        mockMvc.perform(get("/{id}/friends", requester.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email", equalTo(REQUESTER_EMAIL)))
                .andExpect(jsonPath("$[1].email", equalTo(ADDRESSEE_EMAIL)));
    }

    @Test
    void testGetFriendRequestsWhenUserHasFriendshipsWithStatusRequested_thenReturnHttp200WithFriends() throws Exception {
        when(userService.findUserById(requester.getId()))
                .thenReturn(requester);
        when(friendshipService.getFriendsOfWithStatus(Mockito.any(UserDetails.class), eq(REQUESTED)))
                .thenReturn(Arrays.asList(
                        UserDetails.builder()
                        .email(REQUESTER_EMAIL)
                        .build(),
                        UserDetails.builder()
                        .email(ADDRESSEE_EMAIL)
                        .build()));

        mockMvc.perform(get("/friends/requests", requester.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email", equalTo(REQUESTER_EMAIL)))
                .andExpect(jsonPath("$[1].email", equalTo(ADDRESSEE_EMAIL)));
    }

    @Test
    void testGetFriendRequestsWhenUserHasNoFriendshipsWithStatusRequested_thenReturnHttp204() throws Exception {
        when(userService.findUserById(requester.getId()))
                .thenReturn(requester);
        when(friendshipService.getFriendsOfWithStatus(Mockito.any(UserDetails.class), eq(REQUESTED)))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/friends/requests", requester.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", equalTo("User with email - " +
                                                                 REQUESTER_EMAIL +
                                                                 " has no friend requests")));
    }

    @Test
    void testAcceptFriendshipWhenFriendshipExistsAndStatusIsRequested_thenReturnHttp200() throws Exception {
        when(friendshipService.updateFriendship(Mockito.any(FriendshipDto.class)))
                .thenReturn(friendshipDto.setStatus(ACCEPTED));

        mockMvc.perform(put("/friends")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JSONUtils.asJsonString(friendshipDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(ACCEPTED.getStatus())));
    }

    @Test
    void testAcceptFriendshipWhenFriendshipDoesNotBelongToUser_thenReturnHttp400() throws Exception {
        friendshipDto.setRequesterId(100L)
                .setAddresseeId(101L);

        mockMvc.perform(put("/friends")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JSONUtils.asJsonString(friendshipDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                                    equalTo("Friendship does not belong to current user")));
    }

    @Test
    void testAcceptFriendshipSentBySelf_thenReturnHttp400() throws Exception {

        mockMvc.perform(put("/friends")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JSONUtils.asJsonString(friendshipDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                                    equalTo("Cannot accept friend request sent by yourself")));
    }

    @Test
    void testAcceptFriendshipWhenFriendshipNotFound_thenReturnHttp404() throws Exception {

        when(friendshipService.updateFriendship(Mockito.any(FriendshipDto.class)))
                .thenThrow(CWMException.EntityNotFoundException.class);

        mockMvc.perform(put("/friends")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JSONUtils.asJsonString(friendshipDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAcceptFriendshipWhenUnauthorized_thenReturnHttp401() throws Exception {

        mockMvc.perform(put("/friends")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JSONUtils.asJsonString(friendshipDto)))
                .andExpect(status().isUnauthorized());
    }
}