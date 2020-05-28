package com.eirsteir.coffeewithme.social.service;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;


@Import({ ModelMapperConfig.class})
@TestPropertySource("classpath:exception.properties")
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    public static final String EMAIL_ALEX = "alex@email.com";
    public static final String NICKNAME_ALEX = "alex";
    public static final String NAME_ALEX = "Alex";
    public static final String MOBILE_NUMBER_ALEX = "12345678";

    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FriendshipService friendshipService;

    private User user;
    private UserDetailsDto UserDetailsDto;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email(EMAIL_ALEX)
                .name(NAME_ALEX)
                .mobileNumber(MOBILE_NUMBER_ALEX)
                .build();

        UserDetailsDto = UserDetailsDto.builder()
                .email(EMAIL_ALEX)
                .name(NAME_ALEX)
                .mobileNumber(MOBILE_NUMBER_ALEX)
                .build();

         when(userRepository.findByEmail(EMAIL_ALEX))
                .thenReturn(Optional.of(user));

        when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
    }

    @Test
    void testFindUserByEmailWhenFoundReturnsUserDto() {
        UserDetailsDto foundUserDetails = userService.findUserByEmail(EMAIL_ALEX);

        assertThat(foundUserDetails.getEmail()).isEqualTo(EMAIL_ALEX);
    }


    @Test
    void testFindUserByEmailNotFound() {
        assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
                .isThrownBy(() -> userService.findUserByEmail("not.found@email.com"))
                .withMessage("Requested user with email - not.found@email.com does not exist");
    }

    @Mock
    private Specification<User> spec;

    @Test
    @SuppressWarnings("unchecked")
    void testSearchUsersWithMatch_thenReturnListOfUserDto() {
        when(userRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Collections.singletonList(user));

        List<UserDetailsDto> results = userService.searchUsers(spec);

        assertThat(results).hasSize(1);
        assertThat(UserDetailsDto).isIn(results);
    }

    @Test
    void testUpdateProfileUserReturnsUpdatedUserDyo() {
        UserDetailsDto updateProfileRequestDto = UserDetailsDto.builder()
                .nickname(NICKNAME_ALEX)
                .email(EMAIL_ALEX)
                .build();

        UserDetailsDto updatedUserDetails = userService.updateProfile(updateProfileRequestDto);

        assertThat(updatedUserDetails.getUsername()).isEqualTo(NICKNAME_ALEX);
    }

    @Test
    void testUpdateProfileWhenUserNotFound() {
        UserDetailsDto updateProfileRequestDto = UserDetailsDto.builder()
                .email("not.found@email.com")
                .nickname(NICKNAME_ALEX)
                .build();
        assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
                .isThrownBy(() -> userService.updateProfile(updateProfileRequestDto))
                .withMessage("Requested user with email - not.found@email.com does not exist");
    }

    @Test
    void testFindFriendsByIdWithCurrentUserWhenAreFriends_thenReturnIsFriendsTrue() {
        User friend = User.builder()
                .id(100L)
                .build();
        when(friendshipService.findFriends(user.getId(), FriendshipStatus.ACCEPTED))
                .thenReturn(Collections.singletonList(friend));
        when(userRepository.findById(friend.getId()))
                .thenReturn(Optional.of(friend));

        UserDetailsDto userDetailsWithFriend = userService.findUserByIdWithIsFriend(friend.getId(), user.getId());

        assertThat(userDetailsWithFriend.getIsFriend()).isTrue();
    }

    @Test
    void testFindFriendsByIdWithCurrentUserWhenAreNotFriends_thenReturnIsFriendsFalse() {
        User friend = User.builder()
                .id(100L)
                .build();
        when(friendshipService.findFriends(user.getId(), FriendshipStatus.ACCEPTED))
                .thenReturn(new ArrayList<>());
        when(userRepository.findById(friend.getId()))
                .thenReturn(Optional.of(friend));

        UserDetailsDto userDetailsWithFriend = userService.findUserByIdWithIsFriend(friend.getId(), user.getId());

        assertThat(userDetailsWithFriend.getIsFriend()).isFalse();
    }
}
