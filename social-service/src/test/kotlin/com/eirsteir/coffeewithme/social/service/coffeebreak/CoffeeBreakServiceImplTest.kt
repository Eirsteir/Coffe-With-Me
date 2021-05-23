package com.eirsteir.coffeewithme.social.service.coffeebreak

import com.eirsteir.coffeewithme.commons.domain.user.UserDetails
import com.eirsteir.coffeewithme.commons.exception.CWMException
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Import
import java.time.LocalTime
import java.util.*
import java.util.List
import java.util.function.Function
import kotlin.collections.MutableList

@ActiveProfiles("test")
@Import(ModelMapperConfig::class, BaseUnitTest::class, EventuateTestConfig::class)
@ExtendWith(
    SpringExtension::class
)
@ContextConfiguration(classes = [CoffeeBreakServiceImpl::class])
internal class CoffeeBreakServiceImplTest : BaseUnitTest() {
    private var currentUserDetails: UserDetailsImpl? = null
    private var currentUser: User? = null
    private var addresseeAtSameUniversity: User? = null
    private var addressees: MutableList<User?>? = null
    private var friendships: MutableList<FriendshipDto?>? = null
    private var requesterUserDetails: UserDetails? = null
    private var addresseesUserDetails: MutableList<UserDetails?>? = null
    private var campus: Campus? = null
    private var coffeeBreak: CoffeeBreak? = null

    @Autowired
    private val coffeeBreakService: CoffeeBreakService? = null

    @MockBean
    private val coffeeBreakRepository: CoffeeBreakRepository? = null

    @MockBean
    private val userRepository: UserRepository? = null

    @MockBean
    private val campusRepository: CampusRepository? = null

    @Autowired
    private val modelMapper: ModelMapper? = null
    @BeforeEach
    fun setUp() {
        currentUserDetails = UserDetailsImpl()
        currentUserDetails.setId(REQUESTER_ID)
        currentUser = User.builder().id(currentUserDetails.id).build()
        friendships = ADDRESSEE_IDS.stream()
            .map(
                Function<Long?, Any?> { id: Long? ->
                    FriendshipDto.builder()
                        .requester(builder().id(REQUESTER_ID).build())
                        .addressee(builder().id(id).build())
                        .build()
                })
            .collect(Collectors.toList<Any?>())
        addressees = ADDRESSEE_IDS.stream()
            .map(Function<Long?, Any?> { id: Long? -> User.builder().id(id).build() })
            .collect(Collectors.toList<Any?>())
        requesterUserDetails = builder().id(REQUESTER_ID).build()
        addresseesUserDetails = ADDRESSEE_IDS.stream()
            .map(Function<Long?, Any?> { id: Long? -> builder().id(id).build() })
            .collect(Collectors.toList<Any?>())
        campus = Campus()
        campus.setId(10L)
        val university = University()
        university.setId(10L)
        university.addCampus(campus)
        addresseeAtSameUniversity = addressees.get(0)
        addresseeAtSameUniversity.setUniversity(university)
        currentUser.setUniversity(university)
        currentUser.addFriend(addresseeAtSameUniversity, FriendshipStatus.ACCEPTED)
        coffeeBreak = CoffeeBreak.builder()
            .requester(currentUser)
            .addressees(addressees)
            .campus(campus)
            .scheduledTo(LocalTime.now())
            .build()
        Mockito.`when`(userRepository.findById(REQUESTER_ID)).thenReturn(Optional.ofNullable(currentUser))
        Mockito.`when`(userRepository.findAllById(ADDRESSEE_IDS)).thenReturn(addressees)
        Mockito.`when`(campusRepository.findById(CAMPUS_ID)).thenReturn(Optional.ofNullable(campus))
        Mockito.`when`(coffeeBreakRepository.save(Mockito.any(CoffeeBreak::class.java)))
            .thenAnswer(Answer<Any?> { i: InvocationOnMock? -> i.getArgument(0, CoffeeBreak::class.java) })
    }

    @Test
    fun testRegisterCoffeeBreakWhenValid_thenReturnRegisteredCoffeeBreakAddressedToFriendsAtSameUniversity() {
        val request: CoffeeBreakRequest = CoffeeBreakRequest.builder().campusId(campus.getId()).build()
        val savedCoffeeBreakDetails: CoffeeBreakDetails =
            coffeeBreakService.registerCoffeeBreak(request, currentUserDetails)
        val expectedUserDetails = modelMapper.map(addresseeAtSameUniversity, UserDetails::class.java)
        AssertionsForClassTypes.assertThat<UserDetails?>(savedCoffeeBreakDetails.requester).isIn(requesterUserDetails)
        AssertionsForInterfaceTypes.assertThat<UserDetails?>(savedCoffeeBreakDetails.addressees)
            .contains(expectedUserDetails)
    }

    @Test
    fun testRegisterCoffeeBreakWhenRequesterNotFound_thenThrowException() {
        Mockito.`when`(userRepository.findById(REQUESTER_ID))
            .thenThrow(CWMException.EntityNotFoundException::class.java)
        val request: CoffeeBreakRequest = CoffeeBreakRequest.builder().build()
        AssertionsForClassTypes.assertThatExceptionOfType(CWMException.EntityNotFoundException::class.java)
            .isThrownBy(ThrowingCallable { coffeeBreakService.registerCoffeeBreak(request, currentUserDetails) })
    }

    @Test
    fun testRegisterCoffeeBreakWhenCampusIsNull_thenRegisterWithCampusIsNull() {
        Mockito.`when`(campusRepository.findById(CAMPUS_ID)).thenReturn(Optional.empty())
        val request: CoffeeBreakRequest = CoffeeBreakRequest.builder().build()
        coffeeBreak.setCampus(null)
        val savedCoffeeBreakDetails: CoffeeBreakDetails =
            coffeeBreakService.registerCoffeeBreak(request, currentUserDetails)
        AssertionsForClassTypes.assertThat<CampusDetails?>(savedCoffeeBreakDetails.campus).isNull()
    }

    @Test
    fun testRegisterCoffeeBreakWhenScheduledToIsNotSet_thenSetToNow() {
        val request: CoffeeBreakRequest = CoffeeBreakRequest.builder().campusId(campus.getId()).build()
        val savedCoffeeBreakDetails: CoffeeBreakDetails =
            coffeeBreakService.registerCoffeeBreak(request, currentUserDetails)
        val now = LocalTime.now()
        AssertionsForClassTypes.assertThat(savedCoffeeBreakDetails.scheduledTo).isBefore(now)
    }

    @Test
    fun testRegisterCoffeeBreakWhenScheduledToIsIn30Minutes_thenSetTo30MinutesFromNow() {
        val in5MinutesFromNow = LocalTime.now().plusMinutes(5)
        coffeeBreak.setScheduledTo(in5MinutesFromNow)
        val request: CoffeeBreakRequest =
            CoffeeBreakRequest.builder().scheduledToInMinutes(5L).campusId(campus.getId()).build()
        val savedCoffeeBreakDetails: CoffeeBreakDetails =
            coffeeBreakService.registerCoffeeBreak(request, currentUserDetails)
        AssertionsForClassTypes.assertThat(savedCoffeeBreakDetails.scheduledTo)
            .isEqualToIgnoringSeconds(in5MinutesFromNow)
    }

    companion object {
        const val REQUESTER_ID = 1L
        val ADDRESSEE_IDS = List.of(2L, 3L)
        const val CAMPUS_ID = 10L
    }
}