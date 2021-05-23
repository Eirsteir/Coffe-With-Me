package com.eirsteir.coffeewithme.social.service

import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Import
import java.util.*
import java.util.List
import java.util.stream.Stream
import kotlin.collections.MutableList

@Import(ModelMapperConfig::class, BaseUnitTest::class, EventuateTestConfig::class)
@ExtendWith(
    SpringExtension::class
)
@ContextConfiguration(classes = [FriendshipServiceImpl::class])
internal class FriendshipServiceImplTest : BaseUnitTest() {
    private var friendshipRequested: Friendship? = null
    private var acceptedFriendship: Friendship? = null
    private var friendshipId: FriendshipId? = null
    private var requester: User? = null
    private var addressee: User? = null
    private var friendRequest: FriendRequest? = null
    private var requesterDto: UserDetailsDto? = null

    @Autowired
    private val modelMapper: ModelMapper? = null

    @Autowired
    private val friendshipService: FriendshipService? = null

    @MockBean
    private val friendshipRepository: FriendshipRepository? = null

    @MockBean
    private val userService: UserService? = null
    @BeforeEach
    fun setUp() {
        requester = User.builder().id(1L).nickname(REQUESTER_NICKNAME).build()
        addressee = User.builder().id(2L).nickname(ADDRESSEE_NICKNAME).build()
        friendshipId = FriendshipId.builder().requester(requester).addressee(addressee).build()
        friendshipRequested = Friendship.builder().requester(requester).addressee(addressee).status(REQUESTED).build()
        acceptedFriendship = Friendship.builder().requester(requester).addressee(addressee).status(ACCEPTED).build()
        friendRequest = FriendRequest.builder()
            .requesterId(requester.getId())
            .addresseeId(addressee.getId())
            .build()
        requesterDto = modelMapper.map(requester, UserDetailsDto::class.java)
        Mockito.`when`(friendshipRepository.save(Mockito.any(Friendship::class.java))).thenReturn(friendshipRequested)
        Mockito.`when`(userService.findUserById(requester.getId())).thenReturn(requester)
        Mockito.`when`(userService.findUserById(addressee.getId())).thenReturn(addressee)
    }

    @Test
    fun testRegisterFriendship() {
        val savedFriendshipDto: FriendshipDto = friendshipService.registerFriendship(friendRequest)
        assertThat(savedFriendshipDto.getRequester().getId()).isEqualTo(requester.getId())
        assertThat(savedFriendshipDto.getAddressee().getId()).isEqualTo(addressee.getId())
        assertThat(savedFriendshipDto.getStatus()).isEqualTo(REQUESTED)
    }

    @Test
    fun testRegisterFriendshipWhenDuplicate() {
        Mockito.`when`(friendshipRepository.existsById(friendshipId)).thenReturn(true)
        Assertions.assertThatExceptionOfType(DuplicateEntityException::class.java)
            .isThrownBy(ThrowingCallable { friendshipService.registerFriendship(friendRequest) })
    }

    @Test
    fun testRegisterFriendshipWhenFriendshipIdReversedIsDuplicate() {
        Mockito.`when`(friendshipRepository.existsById(friendshipId)).thenReturn(true)
        Assertions.assertThatExceptionOfType(DuplicateEntityException::class.java)
            .isThrownBy(ThrowingCallable { friendshipService.registerFriendship(friendRequest) })
    }

    @Test
    fun testRemoveFriendshipWhenNotFound() {
        Mockito.`when`(
            friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
                Mockito.anyLong(), Mockito.anyLong()
            )
        )
            .thenReturn(Optional.empty())
        val friendshipDto: FriendshipDto = modelMapper.map(friendshipRequested, FriendshipDto::class.java)
        Assertions.assertThatExceptionOfType(CWMException.EntityNotFoundException::class.java)
            .isThrownBy(ThrowingCallable { friendshipService.removeFriendship(friendshipDto) })
    }

    @ParameterizedTest
    @MethodSource("statusChangeFromRequestedProvider")
    fun testUpdateFriendshipFromRequestedStatus(newStatus: FriendshipStatus?) {
        Mockito.`when`(
            friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
                requester.getId(), addressee.getId()
            )
        )
            .thenReturn(Optional.ofNullable(friendshipRequested))
        val friendshipDto: FriendshipDto = modelMapper.map(friendshipRequested, FriendshipDto::class.java)
        friendshipDto.setStatus(newStatus)
        val acceptedFriendship: FriendshipDto = friendshipService.updateFriendship(friendshipDto)
        assertThat(acceptedFriendship.getStatus()).isEqualTo(newStatus)
    }

    @Test
    fun testUpdateFriendshipFromAcceptedToBlocked() {
        val friendshipAccepted: Friendship = friendshipRequested.setStatus(ACCEPTED)
        Mockito.`when`(
            friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
                requester.getId(), addressee.getId()
            )
        )
            .thenReturn(Optional.of(friendshipAccepted))
        val friendshipDto: FriendshipDto = FriendshipDto.builder()
            .requester(modelMapper.map(friendshipAccepted.getRequester(), UserDetailsDto::class.java))
            .addressee(modelMapper.map(friendshipAccepted.getAddressee(), UserDetailsDto::class.java))
            .status(friendshipAccepted.getStatus())
            .build()
        friendshipDto.setStatus(BLOCKED)
        val acceptedFriendship: FriendshipDto = friendshipService.updateFriendship(friendshipDto)
        assertThat(acceptedFriendship.getStatus()).isEqualTo(BLOCKED)
    }

    @Test
    fun testUpdateFriendshipFromAcceptedToAccepted() {
        val friendshipAccepted: Friendship = friendshipRequested.setStatus(ACCEPTED)
        Mockito.`when`(
            friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
                requester.getId(), addressee.getId()
            )
        )
            .thenReturn(Optional.of(friendshipAccepted))
        val friendshipDto: FriendshipDto = FriendshipDto.builder()
            .requester(modelMapper.map(friendshipAccepted.getRequester(), UserDetailsDto::class.java))
            .addressee(modelMapper.map(friendshipAccepted.getAddressee(), UserDetailsDto::class.java))
            .status(friendshipAccepted.getStatus())
            .build()
        friendshipDto.setStatus(ACCEPTED)
        Assertions.assertThatExceptionOfType(InvalidStatusChangeException::class.java)
            .isThrownBy(ThrowingCallable { friendshipService.updateFriendship(friendshipDto) })
    }

    @ParameterizedTest
    @MethodSource("invalidStatusChangeFromAcceptedProvider")
    fun testUpdateFriendshipFromAcceptedStatusToInvalidStatus(newStatus: FriendshipStatus?) {
        val friendshipAccepted: Friendship = friendshipRequested.setStatus(ACCEPTED)
        Mockito.`when`(
            friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
                requester.getId(), addressee.getId()
            )
        )
            .thenReturn(Optional.of(friendshipAccepted))
        val friendshipDto: FriendshipDto = FriendshipDto.builder()
            .requester(modelMapper.map(friendshipAccepted.getRequester(), UserDetailsDto::class.java))
            .addressee(modelMapper.map(friendshipAccepted.getAddressee(), UserDetailsDto::class.java))
            .status(friendshipAccepted.getStatus())
            .build()
        friendshipDto.setStatus(newStatus)
        Assertions.assertThatExceptionOfType(InvalidStatusChangeException::class.java)
            .isThrownBy(ThrowingCallable { friendshipService.updateFriendship(friendshipDto) })
    }

    @Test
    fun testUpdateFriendshipWhenNotFound() {
        Mockito.`when`(
            friendshipRepository.findByIdRequesterIdAndIdAddresseeId(
                requester.getId(), addressee.getId()
            )
        )
            .thenReturn(Optional.empty())
        val friendshipDto: FriendshipDto = FriendshipDto.builder()
            .requester(modelMapper.map(friendshipRequested.getRequester(), UserDetailsDto::class.java))
            .addressee(modelMapper.map(friendshipRequested.getAddressee(), UserDetailsDto::class.java))
            .status(friendshipRequested.getStatus())
            .build()
        Assertions.assertThatExceptionOfType(CWMException.EntityNotFoundException::class.java)
            .isThrownBy(ThrowingCallable { friendshipService.updateFriendship(friendshipDto) })
    }

    @Test
    fun testFindFriendshipsOfWhenUserNotFound() {
        Mockito.`when`(userService.findUserById(requester.getId()))
            .thenThrow(CWMException.EntityNotFoundException::class.java)
        val friendshipsFound: MutableList<FriendshipDto?> = friendshipService.findFriendshipsOf(requesterDto)
        Assertions.assertThat<MutableList<FriendshipDto?>?>(friendshipsFound).isEmpty()
    }

    @Test
    fun testFindFriendsWhenUserHasFriendshipsReturnsFriendshipDtosOfOnlyAcceptedFriendships() {
        val acceptedFriendship: Friendship =
            Friendship.builder().requester(requester).addressee(addressee).status(ACCEPTED).build()
        Mockito.`when`(friendshipRepository.findByUserAndStatus(requester.getId(), ACCEPTED))
            .thenReturn(List.of(acceptedFriendship))
        val friendsFound: MutableList<FriendshipDto?> = friendshipService.findFriendshipsOf(requesterDto)
        Assertions.assertThat<MutableList<FriendshipDto?>?>(friendsFound).hasSize(1)
    }

    @Test
    fun testGetFriendsWhenUserHasNoFriendshipsReturnsEmptyList() {
        Mockito.`when`(friendshipRepository.findByUserAndStatus(requester.getId(), ACCEPTED))
            .thenReturn(ArrayList<E?>())
        val friendsFound: MutableList<FriendshipDto?> = friendshipService.findFriendshipsOf(requesterDto)
        Assertions.assertThat<MutableList<FriendshipDto?>?>(friendsFound).isEmpty()
    }

    @Test
    fun testFindFriendshipsWithStatusRequestedReturnsFriendshipsWithStatusRequested() {
        Mockito.`when`(friendshipRepository.findByUserAndStatus(requester.getId(), ACCEPTED))
            .thenReturn(List.of(acceptedFriendship))
        val friendsFound: MutableList<FriendshipDto?> = friendshipService.findFriendshipsOf(requesterDto)
        Assertions.assertThat<MutableList<FriendshipDto?>?>(friendsFound).hasSize(1)
    }

    companion object {
        private val REQUESTER_NICKNAME: String? = "requester"
        private val ADDRESSEE_NICKNAME: String? = "addressee"
        fun statusChangeFromRequestedProvider(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of(REQUESTED),
                Arguments.of(ACCEPTED),
                Arguments.of(DECLINED),
                Arguments.of(BLOCKED)
            )
        }

        fun invalidStatusChangeFromAcceptedProvider(): Stream<Arguments?>? {
            return Stream.of(Arguments.of(REQUESTED), Arguments.of(DECLINED))
        }
    }
}