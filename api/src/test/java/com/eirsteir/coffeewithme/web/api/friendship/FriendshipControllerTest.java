package com.eirsteir.coffeewithme.web.api.friendship;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.FriendshipIdDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.repository.RoleRepository;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.security.AuthenticationSuccessHandlerImpl;
import com.eirsteir.coffeewithme.security.SecurityConfig;
import com.eirsteir.coffeewithme.service.UserDetailsServiceImpl;
import com.eirsteir.coffeewithme.service.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.web.request.FriendRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(FriendshipController.class)
@Import(SecurityConfig.class)
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
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

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

    @Test
    void allFriendships() {
    }

    @Test
    @WithUserDetails(value = REQUESTER_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testAddFriendshipWhenAddresseeExists_thenReturnHttp200() throws Exception {
        UserDto requesterDto = UserDto.builder()
                .id(1L)
                .email(REQUESTER_EMAIL)
                .build();
        FriendshipIdDto friendshipIdDto = FriendshipIdDto.builder()
                .requester(requesterDto)
                .addressee(UserDto.builder().id(2L).email(ADDRESSEE_EMAIL).build())
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
                                .param("to_friend", mockAddresseeId))
                .andDo(print())
                .andExpect(jsonPath("$.statusCode", is("200")));
    }

    @Test
    void acceptFriendship() {
    }
}