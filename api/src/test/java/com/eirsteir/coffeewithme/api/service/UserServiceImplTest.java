package com.eirsteir.coffeewithme.api.service;

import com.eirsteir.coffeewithme.api.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.api.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.api.domain.user.User;
import com.eirsteir.coffeewithme.api.dto.UserDto;
import com.eirsteir.coffeewithme.api.exception.CWMException;
import com.eirsteir.coffeewithme.api.repository.UserRepository;
import com.eirsteir.coffeewithme.api.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.api.service.user.UserService;
import com.eirsteir.coffeewithme.api.service.user.UserServiceImpl;
import com.eirsteir.coffeewithme.testconfig.MessageTemplateUtilTestConfig;
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


@Import({MessageTemplateUtilTestConfig.class, ModelMapperConfig.class})
@TestPropertySource("classpath:exception.properties")
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    public static final String EMAIL_ALEX = "alex@email.com";
    public static final String USERNAME_ALEX = "alex";
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
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .email(EMAIL_ALEX)
                .name(NAME_ALEX)
                .mobileNumber(MOBILE_NUMBER_ALEX)
                .build();

        userDto = UserDto.builder()
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
        UserDto foundUserDto = userService.findUserByEmail(EMAIL_ALEX);

        assertThat(foundUserDto.getEmail()).isEqualTo(EMAIL_ALEX);
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

        List<UserDto> results = userService.searchUsers(spec);

        assertThat(results).hasSize(1);
        assertThat(userDto).isIn(results);
    }

    @Test
    void testUpdateProfileUserReturnsUpdatedUserDyo() {
        UserDto updateProfileRequestDto = UserDto.builder()
                .username(USERNAME_ALEX)
                .email(EMAIL_ALEX)
                .build();

        UserDto updatedUserDto = userService.updateProfile(updateProfileRequestDto);

        assertThat(updatedUserDto.getUsername()).isEqualTo(USERNAME_ALEX);
    }

    @Test
    void testUpdateProfileWhenUserNotFound() {
        UserDto updateProfileRequestDto = UserDto.builder()
                .email("not.found@email.com")
                .username(USERNAME_ALEX)
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

        UserDto userDtoWithFriend = userService.findUserByIdWithIsFriend(friend.getId(), user);

        assertThat(userDtoWithFriend.getIsFriend()).isTrue();
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

        UserDto userDtoWithFriend = userService.findUserByIdWithIsFriend(friend.getId(), user);

        assertThat(userDtoWithFriend.getIsFriend()).isFalse();
    }
}
