package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.web.dto.UserDto;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@SpringBootTest
@RunWith(SpringRunner.class)
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
    }

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

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

        Mockito.when(userRepository.findByEmail(EMAIL_ALEX))
                .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
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
                .withMessage("Requested user with email - not.found@email.com does not exist.");
    }

    @Test
    void testLoginOrSignUpWhenSignUpReturnsSavedUserDto() {
        String newUserEmail = "not.saved@email.com";
        User newUser = User.builder()
                .email(newUserEmail)
                .name("irrelevant")
                .mobileNumber("12345678")
                .build();

        Mockito.when(userRepository.findByEmail(newUserEmail))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(newUser);

//        UserDto signedUpUserDto = userService.signUp(modelMapper.map(newUser, UserDto.class));
//
//        assertThat(signedUpUserDto.getEmail()).isEqualTo(newUserEmail);
    }

    @Test
    void testLoginOrSignUpWhenLoginReturnsSavedUserDto() {
//        UserDto loggedInUserDto = userService.loginOrSignUp(userDto);
//
//        assertThat(loggedInUserDto.getEmail()).isEqualTo(EMAIL_ALEX);
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
                .withMessage("Requested user with email - not.found@email.com does not exist.");
    }
}