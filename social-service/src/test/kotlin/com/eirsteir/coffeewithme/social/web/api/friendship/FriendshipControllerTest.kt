package com.eirsteir.coffeewithme.social.web.api.friendship

import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus.ACCEPTED
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import java.util.*

@ActiveProfiles("test")
@Import(SecurityConfig::class, ModelMapperConfig::class, EventuateTestConfig::class)
@ContextConfiguration(classes = [FriendshipControllerTestConfiguration::class])
@WebMvcTest(controllers = [FriendshipController::class])
internal class FriendshipControllerTest {
    private val REQUESTER_ID: Long? = 1L
    private val ADDRESSEE_ID: Long? = 2L
    private var requester: User? = null
    private var friendshipDto: FriendshipDto? = null

    @Autowired
    private val mockMvc: MockMvc? = null

    @Autowired
    private val friendshipService: FriendshipService? = null

    @Autowired
    private val userService: UserService? = null

    @Autowired
    private val jwtConfig: JwtConfig? = null
    private var requesterToken: String? = null
    @BeforeEach
    fun setUp() {
        val userDetails: UserDetailsImpl = builder().id(REQUESTER_ID).email(REQUESTER_EMAIL).build()
        val authentication: Authentication = Mockito.mock<Authentication?>(
            Authentication::class.java
        )
        Mockito.`when`<Any?>(authentication.principal).thenReturn(userDetails)
        val securityContext: SecurityContext = Mockito.mock<SecurityContext?>(
            SecurityContext::class.java
        )
        Mockito.`when`<Authentication?>(securityContext.authentication).thenReturn(authentication)
        SecurityContextHolder.setContext(securityContext)
        val principal: UserDetailsImpl = authentication.principal as UserDetailsImpl
        requesterToken = createJwtToken(jwtConfig, authentication, principal)
        requester = User.builder().id(REQUESTER_ID).email(REQUESTER_EMAIL).build()
        friendshipDto = FriendshipDto.builder()
            .requester(builder().id(REQUESTER_ID).build())
            .addressee(builder().id(ADDRESSEE_ID).build())
            .status(REQUESTED)
            .build()
    }

    @Test
    @Throws(Exception::class)
    fun testAddFriendWhenAddresseeExists_thenReturnHttp200() {
        Mockito.`when`(userService.findUserById(requester.getId())).thenReturn(requester)
        Mockito.`when`(friendshipService.registerFriendship(Mockito.any(FriendRequest::class.java)))
            .thenReturn(friendshipDto)
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("to_friend", ADDRESSEE_ID.toString())
                    .header(jwtConfig.header, jwtConfig.prefix + requesterToken)
            )
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(
                MockMvcResultMatchers.jsonPath<Int?>(
                    "$.requester.id", Matchers.equalTo(
                        REQUESTER_ID.toInt()
                    )
                )
            )
            .andExpect(MockMvcResultMatchers.jsonPath<Int?>("$.addressee.id", Matchers.equalTo(ADDRESSEE_ID.toInt())))
            .andExpect(
                MockMvcResultMatchers.jsonPath(
                    "$.status",
                    Matchers.equalTo(FriendshipStatus.REQUESTED.getStatus())
                )
            )
    }

    @Test
    @Throws(Exception::class)
    fun testAddFriendWhenAddresseeDoesNotExists_thenReturnHttp404() {
        Mockito.`when`(friendshipService.registerFriendship(Mockito.any(FriendRequest::class.java)))
            .thenThrow(CWMException.EntityNotFoundException::class.java)
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("to_friend", ADDRESSEE_ID.toString())
                    .header(jwtConfig.header, jwtConfig.prefix + requesterToken)
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @Test
    @Throws(Exception::class)
    fun testAddFriendWhenFriendshipAlreadyExists_thenReturnHttp400() {
        Mockito.`when`(friendshipService.registerFriendship(Mockito.any(FriendRequest::class.java)))
            .thenThrow(DuplicateEntityException::class.java)
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("to_friend", ADDRESSEE_ID.toString())
                    .header(jwtConfig.header, jwtConfig.prefix + requesterToken)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
    }

    @Test
    @Throws(Exception::class)
    fun testAddFriendWhenUnauthorized_thenReturnHttp401() {
        mockMvc
            .perform(
                MockMvcRequestBuilders.post("/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .param("to_friend", ADDRESSEE_ID.toString())
            )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
    }

    @Test
    @Throws(Exception::class)
    fun testGetFriendsWhenUserHasNoFriendships_thenReturnHttp204() {
        Mockito.`when`(userService.findUserById(requester.getId())).thenReturn(requester)
        Mockito.`when`(friendshipService.findFriendshipsOf(Mockito.any(UserDetailsDto::class.java)))
            .thenReturn(ArrayList<E?>())
        mockMvc
            .perform(
                MockMvcRequestBuilders.get("/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(jwtConfig.header, jwtConfig.prefix + requesterToken)
            )
            .andExpect(MockMvcResultMatchers.status().isNoContent())
            .andExpect(
                MockMvcResultMatchers.jsonPath<Any?>(
                    "$.message",
                    Matchers.equalTo<Any?>("User with email - " + requester.getEmail().toString() + " has no friends")
                )
            )
    }

    @Test
    @Throws(Exception::class)
    fun testGetFriendsWhenUnauthorized_thenReturnHttp401() {
        mockMvc
            .perform(MockMvcRequestBuilders.get("/friends").contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
    }

    @Test
    @Throws(Exception::class)
    fun testGetFriendsWhenUserHasFriendships_thenReturnHttp200WithFriends() {
        Mockito.`when`(userService.findUserById(requester.getId())).thenReturn(requester)
        Mockito.`when`(friendshipService.findFriendshipsOf(Mockito.any(UserDetailsDto::class.java)))
            .thenReturn(
                listOf(
                    FriendshipDto.builder()
                        .requester(builder().id(REQUESTER_ID).build())
                        .addressee(builder().id(ADDRESSEE_ID).build())
                        .build()
                )
            )
        mockMvc
            .perform(
                get("/friends", requester.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(jwtConfig.header, jwtConfig.prefix + requesterToken)
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.jsonPath<Int?>(
                    "$[0].requester.id", Matchers.equalTo(
                        REQUESTER_ID.toInt()
                    )
                )
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath<Int?>(
                    "$[0].addressee.id",
                    Matchers.equalTo(ADDRESSEE_ID.toInt())
                )
            )
    }

    @Test
    @Throws(Exception::class)
    fun testGetFriendRequestsWhenUserHasFriendshipsWithStatusRequested_thenReturnListOfFriendships() {
        Mockito.`when`(userService.findUserById(requester.getId())).thenReturn(requester)
        Mockito.`when`(
            friendshipService.findFriendshipsOf(
                Mockito.any(Long::class.java),
                ArgumentMatchers.eq(REQUESTED)
            )
        )
            .thenReturn(
                listOf(
                    FriendshipDto.builder()
                        .requester(builder().id(REQUESTER_ID).build())
                        .addressee(builder().id(ADDRESSEE_ID).build())
                        .build()
                )
            )
        mockMvc
            .perform(
                get("/friends/requests", requester.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(jwtConfig.header, jwtConfig.prefix + requesterToken)
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(
                MockMvcResultMatchers.jsonPath<Int?>(
                    "$[0].requester.id", Matchers.equalTo(
                        REQUESTER_ID.toInt()
                    )
                )
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath<Int?>(
                    "$[0].addressee.id",
                    Matchers.equalTo(ADDRESSEE_ID.toInt())
                )
            )
    }

    @Test
    @Throws(Exception::class)
    fun testGetFriendRequestsWhenUserHasNoFriendshipsWithStatusRequested_thenReturnHttp204() {
        Mockito.`when`(userService.findUserById(requester.getId())).thenReturn(requester)
        Mockito.`when`(
            friendshipService.findFriendshipsOf(
                Mockito.any(Long::class.java),
                ArgumentMatchers.eq(REQUESTED)
            )
        )
            .thenReturn(ArrayList<E?>())
        mockMvc
            .perform(
                get("/friends/requests", requester.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(jwtConfig.header, jwtConfig.prefix + requesterToken)
            )
            .andExpect(MockMvcResultMatchers.status().isNoContent())
            .andExpect(
                MockMvcResultMatchers.jsonPath<String?>(
                    "$.message",
                    Matchers.equalTo("User with email - " + REQUESTER_EMAIL + " has no friend requests")
                )
            )
    }

    @Test
    @Throws(Exception::class)
    fun testAcceptFriendshipWhenFriendshipExistsAndStatusIsRequested_thenReturnHttp200() {
        val userDetails: UserDetailsImpl = builder().id(ADDRESSEE_ID).email(ADDRESSEE_EMAIL).build()
        val authentication: Authentication = Mockito.mock<Authentication?>(
            Authentication::class.java
        )
        Mockito.`when`<Any?>(authentication.principal).thenReturn(userDetails)
        val securityContext: SecurityContext = Mockito.mock<SecurityContext?>(
            SecurityContext::class.java
        )
        Mockito.`when`<Authentication?>(securityContext.authentication).thenReturn(authentication)
        SecurityContextHolder.setContext(securityContext)
        val principal: UserDetailsImpl = authentication.principal as UserDetailsImpl
        val addresseeToken: String = createJwtToken(jwtConfig, authentication, principal)
        Mockito.`when`(friendshipService.updateFriendship(Mockito.any(FriendshipDto::class.java)))
            .thenReturn(friendshipDto.setStatus(ACCEPTED))
        mockMvc
            .perform(
                MockMvcRequestBuilders.put("/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONUtils.asJsonString(friendshipDto))
                    .header(jwtConfig.header, jwtConfig.prefix + addresseeToken)
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.equalTo(ACCEPTED.getStatus())))
    }

    @Test
    @Throws(Exception::class)
    fun testAcceptFriendshipWhenFriendshipDoesNotBelongToUser_thenReturnHttp400() {
        friendshipDto
            .setRequester(builder().id(100L).build())
            .setAddressee(builder().id(101L).build())
        mockMvc
            .perform(
                MockMvcRequestBuilders.put("/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONUtils.asJsonString(friendshipDto))
                    .header(jwtConfig.header, jwtConfig.prefix + requesterToken)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(
                MockMvcResultMatchers.jsonPath<String?>(
                    "$.message",
                    Matchers.equalTo("Friendship does not belong to current user")
                )
            )
    }

    @Test
    @Throws(Exception::class)
    fun testAcceptFriendshipSentBySelf_thenReturnHttp400() {
        mockMvc
            .perform(
                MockMvcRequestBuilders.put("/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONUtils.asJsonString(friendshipDto))
                    .header(jwtConfig.header, jwtConfig.prefix + requesterToken)
            )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(
                MockMvcResultMatchers.jsonPath<String?>(
                    "$.message",
                    Matchers.equalTo("Cannot accept friend request sent by yourself")
                )
            )
    }

    @Test
    @Throws(Exception::class)
    fun testAcceptFriendshipWhenFriendshipNotFound_thenReturnHttp404() {
        val userDetails: UserDetailsImpl = builder().id(ADDRESSEE_ID).email(ADDRESSEE_EMAIL).build()
        val authentication: Authentication = Mockito.mock<Authentication?>(
            Authentication::class.java
        )
        Mockito.`when`<Any?>(authentication.principal).thenReturn(userDetails)
        val securityContext: SecurityContext = Mockito.mock<SecurityContext?>(
            SecurityContext::class.java
        )
        Mockito.`when`<Authentication?>(securityContext.authentication).thenReturn(authentication)
        SecurityContextHolder.setContext(securityContext)
        val principal: UserDetailsImpl = authentication.principal as UserDetailsImpl
        val addresseeToken: String = createJwtToken(jwtConfig, authentication, principal)
        Mockito.`when`(friendshipService.updateFriendship(Mockito.any(FriendshipDto::class.java)))
            .thenThrow(CWMException.EntityNotFoundException::class.java)
        mockMvc
            .perform(
                MockMvcRequestBuilders.put("/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONUtils.asJsonString(friendshipDto))
                    .header(jwtConfig.header, jwtConfig.prefix + addresseeToken)
            )
            .andExpect(MockMvcResultMatchers.status().isNotFound())
    }

    @Test
    @Throws(Exception::class)
    fun testAcceptFriendshipWhenUnauthorized_thenReturnHttp401() {
        mockMvc
            .perform(
                MockMvcRequestBuilders.put("/friends")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JSONUtils.asJsonString(friendshipDto))
            )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
    }

    companion object {
        private val REQUESTER_EMAIL: String? = "requester@test.com"
        private val ADDRESSEE_EMAIL: String? = "addressee@test.com"
    }
}