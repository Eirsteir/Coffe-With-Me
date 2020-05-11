package com.eirsteir.coffeewithme.web.api.friendship;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.security.AuthenticationSuccessHandlerImpl;
import com.eirsteir.coffeewithme.security.SecurityConfig;
import com.eirsteir.coffeewithme.service.UserDetailsServiceImpl;
import com.eirsteir.coffeewithme.service.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.web.request.FriendRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(FriendshipController.class)
@ExtendWith(SpringExtension.class)
class FriendshipControllerTest {

    private static final String REQUESTER_EMAIL = "requester@test.com";
    private static final String ADDRESSEE_EMAIL = "addressee@test.com";
    public static final String OTHER_USER_EMAIL = "other-user@test.com";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendshipService friendshipService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;

    @Autowired
    private ModelMapper modelMapper;
    private UserDetails userPrincipal;

    @TestConfiguration
    static class FriendshipControllerTestContextConfig {

        @Bean
        public UserDetailsService userDetailsService() {
            return new UserDetailsServiceImpl();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

    }

    @BeforeEach
    void setUp() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // TODO: 10.05.2020 Fix authorities
        userPrincipal = new UserPrincipalImpl(User.builder()
                                                      .id(1L)
                                                      .email("user@test.com")
                                                      .password("password")
                                                      .roles(new ArrayList<>())
                                                      .build());
        Mockito.when(authentication.getPrincipal())
                .thenReturn(userPrincipal);
    }

    @Test
    void testAddFriendshipWhenAddresseeExists_thenReturnHttp200() throws Exception {
        UserDto requesterDto = UserDto.builder()
                .id(1L)
                .email(REQUESTER_EMAIL)
                .build();
        FriendshipId friendshipIdDto = FriendshipId.builder()
                .requesterId(requesterDto.getId())
                .addresseeId(2L)
                .build();
        FriendshipDto mockFriendshipDto = FriendshipDto.builder()
                .id(friendshipIdDto)
                .status(FriendshipStatus.REQUESTED)
                .build();

        User mockUser = modelMapper.map(requesterDto, User.class);

        String mockAddresseeId = "2";

        when(userDetailsService.loadUserByUsername(REQUESTER_EMAIL))
                .thenReturn(new UserPrincipalImpl(mockUser));
        when(friendshipService.registerFriendship(Mockito.any(FriendRequest.class)))
                .thenReturn(mockFriendshipDto);

        mockMvc.perform(post("/user/friends")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("to_friend", mockAddresseeId)
                                .with(user(userPrincipal)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}