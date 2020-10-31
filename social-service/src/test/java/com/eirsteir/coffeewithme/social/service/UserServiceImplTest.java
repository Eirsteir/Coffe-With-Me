package com.eirsteir.coffeewithme.social.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.config.BaseUnitTestClass;
import com.eirsteir.coffeewithme.config.EventuateTestConfig;
import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.UserProfile;
import com.eirsteir.coffeewithme.social.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.social.repository.UniversityRepository;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.service.user.UserServiceImpl;
import com.eirsteir.coffeewithme.social.web.request.UpdateProfileRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@Import({ModelMapperConfig.class, BaseUnitTestClass.class, EventuateTestConfig.class})
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserServiceImpl.class, BCryptPasswordEncoder.class})
class UserServiceImplTest extends BaseUnitTestClass {

  public static final String EMAIL_ALEX = "alex@email.com";
  public static final String NICKNAME_ALEX = "alex";
  public static final String NAME_ALEX = "Alex";

  private UserDetailsImpl userDetails;
  private User user;
  private UserDetailsDto userDetailsDto;

  @Autowired private UserService userService;

  @Autowired private ModelMapper modelMapper;

  @MockBean private UserRepository userRepository;

  @MockBean private FriendshipRepository friendshipRepository;

  @MockBean private UniversityRepository universityRepository;

  @BeforeEach
  public void setUp() {
    user = User.builder().id(1L).email(EMAIL_ALEX).name(NAME_ALEX).nickname(NICKNAME_ALEX).build();

    userDetailsDto = modelMapper.map(user, UserDetailsDto.class);

    userDetails = UserDetailsImpl.builder().id(1L).build();

    when(userRepository.findByEmail(EMAIL_ALEX)).thenReturn(Optional.of(user));

    when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

    when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
  }

  @Test
  void testFindUserByEmailWhenFoundReturnsUserDto() {
    UserDetailsDto foundUserDetails = userService.findUserByEmail(EMAIL_ALEX);

    assertThat(foundUserDetails.getEmail()).isEqualTo(EMAIL_ALEX);
  }

  @Test
  void testFindUserByEmailNotFound() {
    assertThatExceptionOfType(CWMException.EntityNotFoundException.class)
        .isThrownBy(() -> userService.findUserByEmail("not.found@email.com"));
  }

  @Mock private Specification<User> spec;

  @Test
  @SuppressWarnings("unchecked")
  void testSearchUsersWithMatch_thenReturnListOfUserDto() {
    when(userRepository.findAll(Mockito.any(Specification.class)))
        .thenReturn(Collections.singletonList(user));

    List<UserDetailsDto> results = userService.searchUsers(spec);

    assertThat(results).hasSize(1);
    assertThat(userDetailsDto).isIn(results);
  }

  @Test
  void testUpdateProfileUserReturnsUpdatedUserDyo() {
    UpdateProfileRequest updateProfileRequestDto = new UpdateProfileRequest(NICKNAME_ALEX, 1L);

    UserProfile updatedProfile = userService.updateProfile(updateProfileRequestDto, userDetails);

    assertThat(updatedProfile.getNickname()).isEqualTo(NICKNAME_ALEX);
  }

  @Test
  void testFindFriendsByIdWithCurrentUserWhenAreFriends_thenReturnIsFriendsTrue() {
    User friend = User.builder().id(100L).email("email").build();
    when(userRepository.findById(Mockito.eq(friend.getId())))
        .thenReturn(Optional.of(friend))
        .thenReturn(Optional.of(user));

    user.addFriend(friend, FriendshipStatus.ACCEPTED);
    UserDetailsDto userDetailsWithFriend = userService.findUserById(friend.getId(), user.getId());

    assertThat(userDetailsWithFriend.getIsFriend()).isTrue();
  }

  @Test
  void testFindFriendsByIdWithCurrentUserWhenAreNotFriends_thenReturnIsFriendsFalse() {
    User friend = User.builder().id(100L).build();
    //        when(friendshipRepository.findFriendshipsOf(user.getId(), FriendshipStatus.ACCEPTED))
    //                .thenReturn(new ArrayList<>());
    when(userRepository.findById(friend.getId())).thenReturn(Optional.of(friend));

    UserDetailsDto userDetailsWithFriend = userService.findUserById(friend.getId(), user.getId());

    assertThat(userDetailsWithFriend.getIsFriend()).isFalse();
  }
}
