package com.eirsteir.coffeewithme.social.service

import com.eirsteir.coffeewithme.commons.exception.CWMException
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ActiveProfiles("test")
@Import(ModelMapperConfig::class, BaseUnitTest::class, EventuateTestConfig::class)
@ExtendWith(
    SpringExtension::class
)
@ContextConfiguration(classes = [UserServiceImpl::class, BCryptPasswordEncoder::class])
internal class UserServiceImplTest : BaseUnitTest() {
    private var userDetails: UserDetailsImpl? = null
    private var user: User? = null
    private var userDetailsDto: UserDetailsDto? = null

    @Autowired
    private val userService: UserService? = null

    @Autowired
    private val modelMapper: ModelMapper? = null

    @MockBean
    private val userRepository: UserRepository? = null

    @MockBean
    private val friendshipRepository: FriendshipRepository? = null

    @MockBean
    private val universityRepository: UniversityRepository? = null
    @BeforeEach
    fun setUp() {
        user = User.builder().id(1L).email(EMAIL_ALEX).name(NAME_ALEX).nickname(NICKNAME_ALEX).build()
        userDetailsDto = modelMapper.map(user, UserDetailsDto::class.java)
        userDetails = builder().id(1L).build()
        Mockito.`when`(userRepository.findByEmail(EMAIL_ALEX)).thenReturn(Optional.of(user))
        Mockito.`when`(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user))
        Mockito.`when`(userRepository.save(Mockito.any(User::class.java))).thenReturn(user)
    }

    @Test
    fun testFindUserByEmailWhenFoundReturnsUserDto() {
        val foundUserDetails: UserDetailsDto = userService.findUserByEmail(EMAIL_ALEX)
        Assertions.assertThat(foundUserDetails.email).isEqualTo(EMAIL_ALEX)
    }

    @Test
    fun testFindUserByEmailNotFound() {
        Assertions.assertThatExceptionOfType(CWMException.EntityNotFoundException::class.java)
            .isThrownBy(ThrowingCallable { userService.findUserByEmail("not.found@email.com") })
    }

    @Mock
    private val spec: Specification<User?>? = null
    @Test
    fun testSearchUsersWithMatch_thenReturnListOfUserDto() {
        Mockito.`when`(userRepository.findAll(Mockito.any(Specification::class.java)))
            .thenReturn(listOf(user))
        val results: MutableList<UserDetailsDto?> = userService.searchUsers(spec)
        Assertions.assertThat<UserDetailsDto?>(results).hasSize(1)
        Assertions.assertThat<UserDetailsDto?>(userDetailsDto).isIn(results)
    }

    @Test
    fun testUpdateProfileUserReturnsUpdatedUserDyo() {
        val updateProfileRequestDto = UpdateProfileRequest(NICKNAME_ALEX, 1L)
        val updatedProfile: UserProfile = userService.updateProfile(updateProfileRequestDto, userDetails)
        assertThat(updatedProfile.getNickname()).isEqualTo(NICKNAME_ALEX)
    }

    @Test
    fun testFindFriendsByIdWithCurrentUserWhenAreFriends_thenReturnIsFriendsTrue() {
        val friend: User = User.builder().id(100L).email("email").build()
        Mockito.`when`(userRepository.findById(Mockito.eq(friend.getId())))
            .thenReturn(Optional.of(friend))
            .thenReturn(Optional.of(user))
        user.addFriend(friend, FriendshipStatus.ACCEPTED)
        val userDetailsWithFriend: UserDetailsDto = userService.findUserById(friend.getId(), user.getId())
        assertThat(userDetailsWithFriend.getIsFriend()).isTrue()
    }

    @Test
    fun testFindFriendsByIdWithCurrentUserWhenAreNotFriends_thenReturnIsFriendsFalse() {
        val friend: User = User.builder().id(100L).build()
        //        when(friendshipRepository.findFriendshipsOf(user.getId(), FriendshipStatus.ACCEPTED))
        //                .thenReturn(new ArrayList<>());
        Mockito.`when`(userRepository.findById(friend.getId())).thenReturn(Optional.of(friend))
        val userDetailsWithFriend: UserDetailsDto = userService.findUserById(friend.getId(), user.getId())
        assertThat(userDetailsWithFriend.getIsFriend()).isFalse()
    }

    companion object {
        val EMAIL_ALEX: String? = "alex@email.com"
        val NICKNAME_ALEX: String? = "alex"
        val NAME_ALEX: String? = "Alex"
    }
}